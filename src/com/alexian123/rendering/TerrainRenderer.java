package com.alexian123.rendering;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.alexian123.game.Camera;
import com.alexian123.lighting.Light;
import com.alexian123.model.ModelMesh;
import com.alexian123.shader.ShaderProgram;
import com.alexian123.terrain.Terrain;
import com.alexian123.texture.TerrainTexturePack;
import com.alexian123.util.Constants;
import com.alexian123.util.enums.AttributeName;
import com.alexian123.util.enums.UniformName;
import com.alexian123.util.gl.GLControl;
import com.alexian123.util.gl.TextureSampler;
import com.alexian123.util.gl.uniforms.UniformArrayVec3;
import com.alexian123.util.gl.uniforms.UniformFloat;
import com.alexian123.util.gl.uniforms.UniformInt;
import com.alexian123.util.gl.uniforms.UniformMat4;
import com.alexian123.util.gl.uniforms.UniformVec3;
import com.alexian123.util.gl.uniforms.UniformVec4;
import com.alexian123.util.mathematics.MatrixCreator;

public class TerrainRenderer {
	
	private static final String VERTEX_SHADER_FILE = "terrain";
	private static final String FRAGMENT_SHADER_FILE = "terrain";
	
	private static final int NUM_TEXTURES = 6;
	
	private final Map<Integer, AttributeName> attributes = new HashMap<>();
	
	protected final TextureSampler[] textures = new TextureSampler[NUM_TEXTURES];
	
	private final ShaderProgram shader;
	private final UniformMat4 projectionMatrix, viewMatrix, transformationMatrix, toShadowMapSpace;
	private final UniformVec4 clipPlane;
	private final UniformVec3 fogColor;
	private final UniformArrayVec3 lightPosition, lightColor, attenuation;
	private final UniformFloat fogDensity, fogGradient, shineDamper, reflectivity, shadowDistance, shadowTransition,
								ambientLight;
	private final UniformInt shadowMapSize, pcfCount, bgTexture, rTexture, gTexture, bTexture, blendMap, shadowMap;
	
	public TerrainRenderer(TextureSampler shadowMapSampler) {
		attributes.put(0, AttributeName.POSITION);
		attributes.put(1, AttributeName.TEXTURE_COORD);
		attributes.put(2, AttributeName.NORMAL);
		
		shader = new ShaderProgram(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE, attributes);
		int id = shader.getProgramID();
		projectionMatrix = new UniformMat4(UniformName.PROJECTION_MATRIX, id);
		viewMatrix = new UniformMat4(UniformName.VIEW_MATRIX, id);
		transformationMatrix = new UniformMat4(UniformName.TRANSFORMATION_MATRIX, id);
		toShadowMapSpace = new UniformMat4(UniformName.TO_SHADOW_MAP_SPACE, id);
		clipPlane = new UniformVec4(UniformName.CLIP_PLANE, id);
		fogColor = new UniformVec3(UniformName.FOG_COLOR, id);
		lightPosition = new UniformArrayVec3(UniformName.LIGHT_POSITION, Constants.MAX_LIGHTS, id);
		lightColor = new UniformArrayVec3(UniformName.LIGHT_COLOR, Constants.MAX_LIGHTS, id);
		attenuation = new UniformArrayVec3(UniformName.ATTENUATION, Constants.MAX_LIGHTS, id);
		fogDensity = new UniformFloat(UniformName.FOG_DENSITY, id);
		fogGradient = new UniformFloat(UniformName.FOG_GRADIENT, id);
		shineDamper = new UniformFloat(UniformName.SHINE_DAMPER, id);
		reflectivity = new UniformFloat(UniformName.REFLECTIVITY, id);
		shadowDistance = new UniformFloat(UniformName.SHADOW_DISTANCE, id);
		shadowTransition = new UniformFloat(UniformName.SHADOW_TRANSITION, id);
		ambientLight = new UniformFloat(UniformName.AMBIENT_LIGHT, id);
		shadowMapSize = new UniformInt(UniformName.SHADOW_MAP_SIZE, id);
		pcfCount = new UniformInt(UniformName.PCF_COUNT, id);
		bgTexture = new UniformInt(UniformName.BG_TEXTURE, id);
		rTexture = new UniformInt(UniformName.R_TEXTURE, id);
		gTexture = new UniformInt(UniformName.G_TEXTURE, id);
		bTexture = new UniformInt(UniformName.B_TEXTURE, id);
		blendMap = new UniformInt(UniformName.BLEND_MAP, id);
		shadowMap = new UniformInt(UniformName.SHADOW_MAP, id);
		
		shader.start();
		projectionMatrix.load(Constants.PROJECTION_MATRIX);
		shadowDistance.load(Constants.SHADOW_DISTANCE);
		shadowTransition.load(Constants.SHADOW_TRANSITION);
		shadowMapSize.load(Constants.SHADOW_MAP_SIZE);
		pcfCount.load(Constants.PCF_COUNT);
		ambientLight.load(Constants.AMBIENT_LIGHT);
		bgTexture.load(0);
		rTexture.load(1);
		gTexture.load(2);
		bTexture.load(3);
		blendMap.load(4);
		shadowMap.load(5);
		textures[NUM_TEXTURES - 1] = shadowMapSampler;
		shader.stop();
	}
	
	public void render(List<Terrain> terrains, List<Light> lights, Camera camera, Vector4f clipPlaneVal, Matrix4f toShadowMapSpaceMatrix) {
		shader.start();
		clipPlane.load(clipPlaneVal);
		toShadowMapSpace.load(toShadowMapSpaceMatrix);
		fogColor.load(Constants.FOG_COLOR);
		fogDensity.load(Constants.FOG_DENSITY);
		fogGradient.load(Constants.FOG_GRADIENT);
		loadLights(lights);
		viewMatrix.load(camera.getViewMatrix());
		for (Terrain terrain : terrains) {
			prepareTerrain(terrain);
			loadModelMatrix(terrain);
			GLControl.drawElementsT(terrain.getModel().getVertexCount());
			terrain.getModel().getVao().unbind(0, 1, 2);
		}
		shader.stop();
	}
	
	public void cleanup() {
		shader.cleanup();
	}
	
	private void loadLights(List<Light> lights) {
		Vector3f[] positions = new Vector3f[Constants.MAX_LIGHTS];
		Vector3f[] colors = new Vector3f[Constants.MAX_LIGHTS];
		Vector3f[] attenuations = new Vector3f[Constants.MAX_LIGHTS];
		for (int i = 0; i < Constants.MAX_LIGHTS; ++i) {
			Light light = (i < lights.size()) ? lights.get(i) : Light.NO_LIGHT;
			positions[i] = light.getPosition();
			colors[i] = light.getColor();
			attenuations[i] = light.getAttenuation();
		}
		lightPosition.load(positions);
		lightColor.load(colors);
		attenuation.load(attenuations);
	}
	
	private void prepareTerrain(Terrain terrain) {
		ModelMesh rawModel = terrain.getModel();
		rawModel.getVao().bind(0, 1, 2);
		TerrainTexturePack pack = terrain.getTexturePack();
		textures[0] = pack.getBackgroundTexture();
		textures[1] = pack.getRedTexture();
		textures[2] = pack.getGreenTexture();
		textures[3] = pack.getBlueTexture();
		textures[4] = terrain.getBlendMap();
		for (int i = 0; i < NUM_TEXTURES; ++i) {
			textures[i].bindToUnit(i);
		}
		shineDamper.load(pack.getShineDamper());
		reflectivity.load(pack.getReflectivity());
	}
	
	private void loadModelMatrix(Terrain terrain) {
		Matrix4f matrix = MatrixCreator.createTransformationMatrix(new Vector3f(terrain.getX(), 0, 
				terrain.getZ()), new Vector3f(0, 0, 0), 1);
		transformationMatrix.load(matrix);
	}
}
