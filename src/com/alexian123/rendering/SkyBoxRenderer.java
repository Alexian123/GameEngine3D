package com.alexian123.rendering;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.alexian123.engine.GameManager;
import com.alexian123.game.Camera;
import com.alexian123.game.Clock;
import com.alexian123.loader.Loader;
import com.alexian123.model.ModelMesh;
import com.alexian123.shader.ShaderProgram;
import com.alexian123.util.Constants;
import com.alexian123.util.enums.TimeOfDay;
import com.alexian123.util.enums.UniformName;
import com.alexian123.util.gl.GLControl;
import com.alexian123.util.gl.TextureSampler;
import com.alexian123.util.gl.uniforms.UniformFloat;
import com.alexian123.util.gl.uniforms.UniformInt;
import com.alexian123.util.gl.uniforms.UniformMat4;
import com.alexian123.util.gl.uniforms.UniformVec3;

public class SkyBoxRenderer {
	
	private static final float SIZE = 500f;
	
	private static final float[] VERTICES = {
			-SIZE,  SIZE, -SIZE,
		    -SIZE, -SIZE, -SIZE,
		     SIZE, -SIZE, -SIZE,
		     SIZE, -SIZE, -SIZE,
		     SIZE,  SIZE, -SIZE,
		    -SIZE,  SIZE, -SIZE,

		    -SIZE, -SIZE,  SIZE,
		    -SIZE, -SIZE, -SIZE,
		    -SIZE,  SIZE, -SIZE,
		    -SIZE,  SIZE, -SIZE,
		    -SIZE,  SIZE,  SIZE,
		    -SIZE, -SIZE,  SIZE,

		     SIZE, -SIZE, -SIZE,
		     SIZE, -SIZE,  SIZE,
		     SIZE,  SIZE,  SIZE,
		     SIZE,  SIZE,  SIZE,
		     SIZE,  SIZE, -SIZE,
		     SIZE, -SIZE, -SIZE,

		    -SIZE, -SIZE,  SIZE,
		    -SIZE,  SIZE,  SIZE,
		     SIZE,  SIZE,  SIZE,
		     SIZE,  SIZE,  SIZE,
		     SIZE, -SIZE,  SIZE,
		    -SIZE, -SIZE,  SIZE,
	
		    -SIZE,  SIZE, -SIZE,
		     SIZE,  SIZE, -SIZE,
		     SIZE,  SIZE,  SIZE,
		     SIZE,  SIZE,  SIZE,
		    -SIZE,  SIZE,  SIZE,
		    -SIZE,  SIZE, -SIZE,
	
		    -SIZE, -SIZE, -SIZE,
		    -SIZE, -SIZE,  SIZE,
		     SIZE, -SIZE, -SIZE,
		     SIZE, -SIZE, -SIZE,
		    -SIZE, -SIZE,  SIZE,
		     SIZE, -SIZE,  SIZE
	};
	
	private static final String DAY_TEXTURE_DIR = "skybox/day/";
	private static final String[] DAY_TEXTURE_FILES = { 
			DAY_TEXTURE_DIR + "right", 
			DAY_TEXTURE_DIR + "left", 
			DAY_TEXTURE_DIR + "top", 
			DAY_TEXTURE_DIR + "bottom", 
			DAY_TEXTURE_DIR + "back", 
			DAY_TEXTURE_DIR + "front" 
	};
	
	private static final String NIGHT_TEXTURE_DIR = "skybox/night/";
	private static final String[] NIGHT_TEXTURE_FILES = { 
			NIGHT_TEXTURE_DIR + "right", 
			NIGHT_TEXTURE_DIR + "left", 
			NIGHT_TEXTURE_DIR + "top", 
			NIGHT_TEXTURE_DIR + "bottom", 
			NIGHT_TEXTURE_DIR + "back", 
			NIGHT_TEXTURE_DIR + "front" 
	};
	
	private static final String VERTEX_SHADER_FILE = "skybox";
	private static final String FRAGMENT_SHADER_FILE = "skybox";
	
	private static final float ROTATION_INCREMENT = 0.1f;
	
	private static final int NUM_TEXTURES = 2;
	
	private final TextureSampler[] textures = new TextureSampler[NUM_TEXTURES];
	
	private float rotation = 0f;
	
	private TextureSampler dayTextureID, nightTextureID;
	private float blend;
	
	private ModelMesh cube;
	private Clock clock;

	private final ShaderProgram shader;
	private final UniformMat4 projectionMatrix, viewMatrix;
	private final UniformVec3 fogColor;
	private final UniformFloat blendFactor, lowerLimit, upperLimit;
	private final UniformInt cubeMap0, cubeMap1;
	
	
	public SkyBoxRenderer(Loader loader, Clock clock) {
		this.clock = clock;
		
		cube = loader.loadToVao(VERTICES, 3);
		dayTextureID = loader.loadCubeMap(DAY_TEXTURE_FILES);
		nightTextureID = loader.loadCubeMap(NIGHT_TEXTURE_FILES);
		
		shader = new ShaderProgram(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
		int id = shader.getProgramID();
		projectionMatrix = new UniformMat4(UniformName.PROJECTION_MATRIX, id);
		viewMatrix = new UniformMat4(UniformName.VIEW_MATRIX, id);
		fogColor = new UniformVec3(UniformName.FOG_COLOR, id);
		blendFactor = new UniformFloat(UniformName.BLEND_FACTOR, id);
		lowerLimit = new UniformFloat(UniformName.LOWER_LIMIT, id);
		upperLimit = new UniformFloat(UniformName.UPPER_LIMIT, id);
		cubeMap0 = new UniformInt(UniformName.CUBE_MAP_0, id);
		cubeMap1 = new UniformInt(UniformName.CUBE_MAP_1, id);
		
		shader.start();
		projectionMatrix.load(Constants.PROJECTION_MATRIX);
		cubeMap0.load(0);
		cubeMap1.load(1);
		lowerLimit.load(0.0f);
		upperLimit.load(30.0f);
		shader.stop();
	}
	
	public void render(Camera camera) {
		shader.start();
		viewMatrix.load(getModifiedViewMatrix(camera));
		fogColor.load(Constants.FOG_COLOR);
		cube.getVao().bind(0);
		bindTextures();
		GLControl.drawArraysT(cube.getVertexCount());
		cube.getVao().unbind(0);
		shader.stop();
	}

	public void cleanup() {
		shader.cleanup();
	}
	
	private Matrix4f getModifiedViewMatrix(Camera camera) {
		Matrix4f matrix = new Matrix4f(camera.getViewMatrix());
		// disable translation
		matrix.m30 = 0;
		matrix.m31 = 0;
		matrix.m32 = 0;
		// add rotation
		rotation += ROTATION_INCREMENT * GameManager.getFrameTimeSeconds();
		Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0, 1, 0), matrix, matrix);
		return matrix;
	}
	
	private void bindTextures() {
		setTexturesBasedOnTime();
		for (int i = 0; i < NUM_TEXTURES; ++i) {
			textures[i].bindToUnit(i);
		}
		blendFactor.load(blend);
	}
	
	private void setTexturesBasedOnTime() {
		float preciseTime = clock.getPreciseTime();
		switch (clock.tellTime()) {
			case DAWN:
				textures[0] = nightTextureID;
				textures[1] = dayTextureID;
				blend = (preciseTime - TimeOfDay.DAWN.getValue()) / (TimeOfDay.MORNING.getValue() - TimeOfDay.DAWN.getValue());
				break;
			case MORNING:
				textures[0] = dayTextureID;
				textures[1] = dayTextureID;
				blend = (preciseTime - TimeOfDay.MORNING.getValue()) / (TimeOfDay.NOON.getValue() - TimeOfDay.MORNING.getValue());
				break;
			case NOON:
				textures[0] = dayTextureID;
				textures[1] = dayTextureID;
				blend = (preciseTime - TimeOfDay.NOON.getValue()) / (TimeOfDay.AFTERNOON.getValue() - TimeOfDay.NOON.getValue());
				break;
			case AFTERNOON:
				textures[0] = dayTextureID;
				textures[1] = dayTextureID;
				blend = (preciseTime - TimeOfDay.AFTERNOON.getValue()) / (TimeOfDay.EVENING.getValue() - TimeOfDay.AFTERNOON.getValue());
				break;
			case EVENING:
				textures[0] = dayTextureID;
				textures[1] = nightTextureID;
				blend = (preciseTime - TimeOfDay.EVENING.getValue()) / (TimeOfDay.NIGHT.getValue() - TimeOfDay.EVENING.getValue());
				break;
			case NIGHT:
				textures[0] = nightTextureID;
				textures[1] = nightTextureID;
				blend = preciseTime / TimeOfDay.MORNING.getValue();
				break;
			case MAX_TIME:
			default:
				break;
		}
	}
}
