package com.alexian123.renderer;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.alexian123.entity.Camera;
import com.alexian123.light.Light;
import com.alexian123.loader.Loader;
import com.alexian123.model.RawModel;
import com.alexian123.shader.WaterShader;
import com.alexian123.terrain.Water;
import com.alexian123.terrain.WaterFrameBuffers;
import com.alexian123.util.Maths;

public class WaterRenderer {
	
	private static final String DUDV_MAP_FILE = "waterDUDV";
	private static final String NORMAL_MAP_FILE = "waterNormal";
	private static final float WAVE_SPEED = 0.03f;
	
	private float moveFactor = 0;
	
	private RawModel quad;
	private WaterShader shader;
	private int dudvTexture;
	private int normalTexture;
	
	public WaterRenderer(Loader loader, Matrix4f projectionMatrix) {
		this.shader = new WaterShader();
		this.dudvTexture = loader.loadTexture(DUDV_MAP_FILE);
		this.normalTexture = loader.loadTexture(NORMAL_MAP_FILE);
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.loadShineParameters(20f, 0.5f);	// shineDamper, reflectivity
		shader.loadViewPlanes(RenderingManager.NEAR_PLANE, RenderingManager.FAR_PLANE);
		shader.stop();
		setUpVAO(loader);
	}

	public void render(List<Water> waters, Camera camera, List<Light> lights) {
		moveFactor += WAVE_SPEED * DisplayManager.getFrameTimeSeconds();
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
	
	private void unbind() {
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	private void prepareRender(Camera camera, WaterFrameBuffers fbos, List<Light> lights) {
		shader.start();
		shader.loadViewMatrix(camera);
		shader.loadMoveFactor(moveFactor);
		shader.loadLights(lights);
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getReflectionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, dudvTexture);
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, normalTexture);
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionDepthTexture());
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	private void setUpVAO(Loader loader) {
		// Just x and z vertex positions here, y is set to 0 in v.shader
		float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
		quad = loader.loadToVao(vertices, 2);
	}
}