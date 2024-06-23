package com.alexian123.rendering;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.alexian123.engine.GameManager;
import com.alexian123.game.Camera;
import com.alexian123.lighting.Light;
import com.alexian123.loader.Loader;
import com.alexian123.model.ModelMesh;
import com.alexian123.shader.ShaderProgram;
import com.alexian123.util.enums.UniformName;
import com.alexian123.util.gl.GLControl;
import com.alexian123.util.gl.TextureSampler;
import com.alexian123.util.gl.uniforms.UniformArrayVec3;
import com.alexian123.util.gl.uniforms.UniformFloat;
import com.alexian123.util.gl.uniforms.UniformInt;
import com.alexian123.util.gl.uniforms.UniformMat4;
import com.alexian123.util.gl.uniforms.UniformVec3;
import com.alexian123.util.mathematics.MatrixCreator;
import com.alexian123.water.Water;
import com.alexian123.water.WaterFrameBuffers;

public class WaterRenderer {
	
	private static final String DUDV_MAP_FILE = "maps/waterDUDV";
	private static final String NORMAL_MAP_FILE = "maps/waterNormal";
	
	private static final String VERTEX_SHADER_FILE = "water";
	private static final String FRAGMENT_SHADER_FILE = "water";
	
	private static final float[] VERTICES = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
	
	private static final int NUM_TEXTURES = 5;
	
	private final TextureSampler[] textures = new TextureSampler[NUM_TEXTURES];
	
	private float move = 0;
	
	private final ModelMesh quad;

	private final TextureSampler dudvTexture;
	private final TextureSampler normalTexture;
	
	private final ShaderProgram shader;
	private final UniformMat4 projectionMatrix, viewMatrix, modelMatrix;
	private final UniformVec3 fogColor, cameraPosition;
	private final UniformArrayVec3 lightPosition, lightColor, attenuation;
	private final UniformFloat nearPlane, farPlane, moveFactor, waveStrength, shineDamper, reflectivity, 
								tilingFactor, fogDensity, fogGradient;
	private final UniformInt dudvMap, normalMap, reflection, refraction, depthMap;
	
	
	public WaterRenderer(Loader loader) {
		quad = loader.loadToVao(VERTICES, 2);
		
		dudvTexture = loader.loadTexture(DUDV_MAP_FILE);
		normalTexture = loader.loadTexture(NORMAL_MAP_FILE);
		
		shader = new ShaderProgram(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
		int id = shader.getProgramID();
		projectionMatrix = new UniformMat4(UniformName.PROJECTION_MATRIX, id);
		viewMatrix = new UniformMat4(UniformName.VIEW_MATRIX, id);
		modelMatrix = new UniformMat4(UniformName.MODEL_MATRIX, id);
		fogColor = new UniformVec3(UniformName.FOG_COLOR, id);
		cameraPosition = new UniformVec3(UniformName.CAMERA_POSITION, id);
		lightPosition = new UniformArrayVec3(UniformName.LIGHT_POSITION, GameManager.SETTINGS.maxLights, id);
		lightColor = new UniformArrayVec3(UniformName.LIGHT_COLOR, GameManager.SETTINGS.maxLights, id);
		attenuation = new UniformArrayVec3(UniformName.ATTENUATION, GameManager.SETTINGS.maxLights, id);
		nearPlane = new UniformFloat(UniformName.NEAR_PLANE, id);
		farPlane = new UniformFloat(UniformName.FAR_PLANE, id);
		moveFactor = new UniformFloat(UniformName.MOVE_FACTOR, id);
		waveStrength = new UniformFloat(UniformName.WAVE_STRENGTH, id);
		shineDamper = new UniformFloat(UniformName.SHINE_DAMPER, id);
		reflectivity = new UniformFloat(UniformName.REFLECTIVITY, id);
		tilingFactor = new UniformFloat(UniformName.TILING_FACTOR, id);
		fogDensity = new UniformFloat(UniformName.FOG_DENSITY, id);
		fogGradient = new UniformFloat(UniformName.FOG_GRADIENT, id);
		dudvMap = new UniformInt(UniformName.DUDV_MAP, id);
		normalMap = new UniformInt(UniformName.NORMAL_MAP, id);
		reflection = new UniformInt(UniformName.REFLECTION_TEXTURE, id);
		refraction = new UniformInt(UniformName.REFRACTION_TEXTURE, id);
		depthMap = new UniformInt(UniformName.DEPTH_MAP, id);
		
		shader.start();
		dudvMap.load(0);
		normalMap.load(1);
		reflection.load(2);
		refraction.load(3);
		depthMap.load(4);
		projectionMatrix.load(GameManager.SETTINGS.projectionMatrix.getValue());
		shineDamper.load(GameManager.SETTINGS.waterShineDamper);
		reflectivity.load(GameManager.SETTINGS.waterReflectivity);
		nearPlane.load(GameManager.SETTINGS.nearPlane);
		farPlane.load(GameManager.SETTINGS.farPlane);
		tilingFactor.load(GameManager.SETTINGS.waterTilingFactor);
		waveStrength.load(GameManager.SETTINGS.waterWaveStrength);
		shader.stop();
		
		textures[0] = dudvTexture;
		textures[1] = normalTexture;
	}

	public void render(List<Water> waters, Camera camera, List<Light> lights) {
		move += GameManager.SETTINGS.waterWaveSpeed * GameManager.getFrameTimeSeconds();
		move %= 1;
		for (Water water : waters) {
			shader.start();
			prepareRender(camera, water.getFbos(), lights);
			Matrix4f matrix = MatrixCreator.createTransformationMatrix(
					new Vector3f(water.getX(), water.getHeight(), water.getZ()), new Vector3f(0, 0, 0), GameManager.SETTINGS.waterTileSize);
			modelMatrix.load(matrix);
			GLControl.drawArraysT(quad.getVertexCount());
			quad.getVao().unbind(0);
			shader.stop();
		}
		GLControl.disableBlending();
	}

	public void cleanup() {
		shader.cleanup();
	}

	private void prepareRender(Camera camera, WaterFrameBuffers fbos, List<Light> lights) {
		viewMatrix.load(camera.getViewMatrix());
		cameraPosition.load(camera.getPosition());
		moveFactor.load(move);
		loadLights(lights);
		fogColor.load(GameManager.SETTINGS.fogColor.getValue());
		fogDensity.load(GameManager.SETTINGS.fogDensity);
		fogGradient.load(GameManager.SETTINGS.fogGradient);
		quad.getVao().bind(0);
		textures[2] = fbos.getReflectionTexture();
		textures[3] = fbos.getRefractionTexture();
		textures[4] = fbos.getRefractionDepthTexture();
		for (int i = 0; i < NUM_TEXTURES; ++i) {
			textures[i].bindToUnit(i);
		}
		GLControl.enableBlending();
	}
	
	private void loadLights(List<Light> lights) {
		Vector3f[] positions = new Vector3f[GameManager.SETTINGS.maxLights];
		Vector3f[] colors = new Vector3f[GameManager.SETTINGS.maxLights];
		Vector3f[] attenuations = new Vector3f[GameManager.SETTINGS.maxLights];
		for (int i = 0; i < GameManager.SETTINGS.maxLights; ++i) {
			Light light = (i < lights.size()) ? lights.get(i) : Light.NO_LIGHT;
			positions[i] = light.getPosition();
			colors[i] = light.getColor();
			attenuations[i] = light.getAttenuation();
		}
		lightPosition.load(positions);
		lightColor.load(colors);
		attenuation.load(attenuations);
	}
}
