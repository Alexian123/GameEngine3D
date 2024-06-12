package com.alexian123.rendering;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.alexian123.engine.GameManager;
import com.alexian123.entity.Camera;
import com.alexian123.entity.Light;
import com.alexian123.loader.Loader;
import com.alexian123.model.RawModel;
import com.alexian123.shader.WaterShader;
import com.alexian123.util.Constants;
import com.alexian123.util.Maths;
import com.alexian123.water.Water;
import com.alexian123.water.WaterFrameBuffers;

public class WaterRenderer {
	
	private static final String DUDV_MAP_FILE = "maps/waterDUDV";
	private static final String NORMAL_MAP_FILE = "maps/waterNormal";
	private static final float WAVE_SPEED = 0.03f;
	private static final int MAX_NUM_TEXTURES = 5;
	
	private final int numTextures;
	private final int[] textures = new int[MAX_NUM_TEXTURES];
	
	private float moveFactor = 0;
	
	private RawModel quad;
	private WaterShader shader;
	private int dudvTexture;
	private int normalTexture;
	
	public WaterRenderer(Loader loader) {
		this.shader = new WaterShader();
		this.dudvTexture = loader.loadTexture(DUDV_MAP_FILE);
		this.normalTexture = loader.loadTexture(NORMAL_MAP_FILE);
		shader.start();
		this.numTextures = shader.connectTextureUnits();
		shader.loadProjectionMatrix(Constants.PROJECTION_MATRIX);
		shader.loadShineParameters(20f, 0.5f);	// shineDamper, reflectivity
		shader.loadViewPlanes(Constants.NEAR_PLANE, Constants.FAR_PLANE);
		shader.loadTilingFactor(4.0f);
		shader.loadWaveStrength(0.04f);
		shader.stop();
		setUpVAO(loader);		
		textures[0] = dudvTexture;
		textures[1] = normalTexture;
	}

	public void render(List<Water> waters, Camera camera, List<Light> lights) {
		moveFactor += WAVE_SPEED * GameManager.getFrameTimeSeconds();
		moveFactor %= 1;
		for (Water water : waters) {
			prepareRender(camera, water.getFbos(), lights);
			Matrix4f modelMatrix = Maths.createTransformationMatrix(
					new Vector3f(water.getX(), water.getHeight(), water.getZ()), new Vector3f(0, 0, 0), Water.TILE_SIZE);
			shader.loadModelMatrix(modelMatrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
			unbind();
		}
	}

	public void cleanup() {
		shader.cleanup();
	}

	private void prepareRender(Camera camera, WaterFrameBuffers fbos, List<Light> lights) {
		shader.start();
		shader.loadViewMatrix(camera);
		shader.loadMoveFactor(moveFactor);
		shader.loadLights(lights);
		shader.loadFog(Constants.FOG_DENSITY, Constants.FOG_GRADIENT, Constants.FOG_COLOR);
		GL30.glBindVertexArray(quad.getVaoID());
		for (int i = 0; i < shader.getNumAttributes(); ++i) {
			GL20.glEnableVertexAttribArray(i);
		}
		textures[2] = fbos.getReflectionTexture();
		textures[3] = fbos.getRefractionTexture();
		textures[4] = fbos.getRefractionDepthTexture();
		for (int i = 0; i < numTextures; ++i) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0 + i);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures[i]);
		}
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	private void unbind() {
		GL11.glDisable(GL11.GL_BLEND);
		for (int i = 0; i < shader.getNumAttributes(); ++i) {
			GL20.glDisableVertexAttribArray(i);
		}
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	private void setUpVAO(Loader loader) {
		// Just x and z vertex positions here, y is set to 0 in v.shader
		float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
		quad = loader.loadToVao(vertices, 2);
	}
}
