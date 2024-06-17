package com.alexian123.rendering;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.alexian123.entity.Camera;
import com.alexian123.loader.Loader;
import com.alexian123.model.RawModel;
import com.alexian123.shader.SkyBoxShader;
import com.alexian123.util.Clock;
import com.alexian123.util.Constants;
import com.alexian123.util.enums.TimeOfDay;

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
	
	private static final int MAX_NUM_TEXTURES = 2;
	
	private final SkyBoxShader shader = new SkyBoxShader();
	
	private final int numTextures;
	private final int[] textures = new int[MAX_NUM_TEXTURES];
	
	private RawModel cube;
	private int dayTextureID, nightTextureID;
	private Clock clock;
	
	private float blendFactor;
	
	public SkyBoxRenderer(Loader loader, Clock clock) {
		this.cube = loader.loadToVao(VERTICES, 3);
		this.dayTextureID = loader.loadCubeMap(DAY_TEXTURE_FILES);
		this.nightTextureID = loader.loadCubeMap(NIGHT_TEXTURE_FILES);
		this.clock = clock;
		shader.start();
		shader.loadProjectionMatrix(Constants.PROJECTION_MATRIX);
		this.numTextures = shader.connectTextureUnits();
		shader.loadLimits(0.0f, 30.0f);
		shader.stop();
	}
	
	public void render(Camera camera) {
		shader.start();
		shader.loadViewMatrix(camera);
		shader.loadFogColor(Constants.FOG_COLOR);
		GL30.glBindVertexArray(cube.getVaoID());
		for (int i = 0; i < shader.getNumAttributes(); ++i) {
			GL20.glEnableVertexAttribArray(i);
		}
		bindTextures();
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
		for (int i = 0; i < shader.getNumAttributes(); ++i) {
			GL20.glDisableVertexAttribArray(i);
		}
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	public void cleanup() {
		shader.cleanup();
	}
	
	private void bindTextures() {
		setTexturesBasedOnTime();
		for (int i = 0; i < numTextures; ++i) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0 + i);
			GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, textures[i]);
		}
		shader.loadBlendFactor(blendFactor);
	}
	
	private void setTexturesBasedOnTime() {
		float preciseTime = clock.getPreciseTime();
		switch (clock.tellTime()) {
			case DAWN:
				textures[0] = nightTextureID;
				textures[1] = dayTextureID;
				blendFactor = (preciseTime - TimeOfDay.DAWN.getValue()) / (TimeOfDay.MORNING.getValue() - TimeOfDay.DAWN.getValue());
				break;
			case MORNING:
				textures[0] = dayTextureID;
				textures[1] = dayTextureID;
				blendFactor = (preciseTime - TimeOfDay.MORNING.getValue()) / (TimeOfDay.NOON.getValue() - TimeOfDay.MORNING.getValue());
				break;
			case NOON:
				textures[0] = dayTextureID;
				textures[1] = dayTextureID;
				blendFactor = (preciseTime - TimeOfDay.NOON.getValue()) / (TimeOfDay.AFTERNOON.getValue() - TimeOfDay.NOON.getValue());
				break;
			case AFTERNOON:
				textures[0] = dayTextureID;
				textures[1] = dayTextureID;
				blendFactor = (preciseTime - TimeOfDay.AFTERNOON.getValue()) / (TimeOfDay.EVENING.getValue() - TimeOfDay.AFTERNOON.getValue());
				break;
			case EVENING:
				textures[0] = dayTextureID;
				textures[1] = nightTextureID;
				blendFactor = (preciseTime - TimeOfDay.EVENING.getValue()) / (TimeOfDay.NIGHT.getValue() - TimeOfDay.EVENING.getValue());
				break;
			case NIGHT:
				textures[0] = nightTextureID;
				textures[1] = nightTextureID;
				blendFactor = preciseTime / TimeOfDay.MORNING.getValue();
				break;
			case MAX_TIME:
			default:
				break;
		}
	}
}
