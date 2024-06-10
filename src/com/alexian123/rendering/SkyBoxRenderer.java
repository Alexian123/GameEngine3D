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
import com.alexian123.util.TimeOfDay;

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
	
	private final SkyBoxShader shader = new SkyBoxShader();
	
	private RawModel cube;
	private int dayTextureID, nightTextureID;
	private Clock clock;
	
	private int texture0, texture1;
	private float blendFactor;
	
	public SkyBoxRenderer(Loader loader, Clock clock) {
		this.cube = loader.loadToVao(VERTICES, 3);
		this.dayTextureID = loader.loadCubeMap(DAY_TEXTURE_FILES);
		this.nightTextureID = loader.loadCubeMap(NIGHT_TEXTURE_FILES);
		this.clock = clock;
		shader.start();
		shader.loadProjectionMatrix(Constants.PROJECTION_MATRIX);
		shader.connectTextureUnits();
		shader.loadLimits(0.0f, 30.0f);
		shader.stop();
	}
	
	public void render(Camera camera) {
		shader.start();
		shader.loadViewMatrix(camera);
		shader.loadFogColor(Constants.FOG_COLOR);
		GL30.glBindVertexArray(cube.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		bindTextures();
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	public void cleanup() {
		shader.cleanup();
	}
	
	private void bindTextures() {
		setTexturesBasedOnTime();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture0);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1);
		shader.loadBlendFactor(blendFactor);
	}
	
	private void setTexturesBasedOnTime() {
		float preciseTime = clock.getPreciseTime();
		switch (clock.tellTime()) {
			case DAWN:
				texture0 = nightTextureID;
				texture1 = dayTextureID;
				blendFactor = (preciseTime - TimeOfDay.DAWN.getValue()) / (TimeOfDay.MORNING.getValue() - TimeOfDay.DAWN.getValue());
				break;
			case MORNING:
				texture0 = dayTextureID;
				texture1 = dayTextureID;
				blendFactor = (preciseTime - TimeOfDay.MORNING.getValue()) / (TimeOfDay.NOON.getValue() - TimeOfDay.MORNING.getValue());
				break;
			case NOON:
				texture0 = dayTextureID;
				texture1 = dayTextureID;
				blendFactor = (preciseTime - TimeOfDay.NOON.getValue()) / (TimeOfDay.AFTERNOON.getValue() - TimeOfDay.NOON.getValue());
				break;
			case AFTERNOON:
				texture0 = dayTextureID;
				texture1 = dayTextureID;
				blendFactor = (preciseTime - TimeOfDay.AFTERNOON.getValue()) / (TimeOfDay.EVENING.getValue() - TimeOfDay.AFTERNOON.getValue());
				break;
			case EVENING:
				texture0 = dayTextureID;
				texture1 = nightTextureID;
				blendFactor = (preciseTime - TimeOfDay.EVENING.getValue()) / (TimeOfDay.NIGHT.getValue() - TimeOfDay.EVENING.getValue());
				break;
			case NIGHT:
				texture0 = nightTextureID;
				texture1 = nightTextureID;
				blendFactor = preciseTime / TimeOfDay.MORNING.getValue();
				break;
			case MAX_TIME:
			default:
				break;
		}
	}
}
