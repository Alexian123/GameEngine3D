package com.alexian123.renderer;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.alexian123.entity.Camera;
import com.alexian123.loader.Loader;
import com.alexian123.model.RawModel;
import com.alexian123.shader.WaterShader;
import com.alexian123.terrain.Water;
import com.alexian123.terrain.WaterFrameBuffers;
import com.alexian123.util.Maths;

public class WaterRenderer {
	
	private static final String DUDV_MAP_FILE = "waterDUDV";
	private static final float WAVE_SPEED = 0.03f;
	
	private float moveFactor = 0;
	
	private RawModel quad;
	private WaterShader shader;
	private int dudvTexture;
	
	public WaterRenderer(Loader loader, Matrix4f projectionMatrix) {
		this.shader = new WaterShader();
		this.dudvTexture = loader.loadTexture(DUDV_MAP_FILE);
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		setUpVAO(loader);
	}

	public void render(List<Water> waters, Camera camera) {
		moveFactor += WAVE_SPEED * DisplayManager.getFrameTimeSeconds();
		moveFactor %= 1;
		for (Water water : waters) {
			prepareRender(camera, water.getFbos());
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
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	private void prepareRender(Camera camera, WaterFrameBuffers fbos) {
		shader.start();
		shader.loadViewMatrix(camera);
		shader.loadMoveFactor(moveFactor);
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getReflectionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, dudvTexture);
	}
	
	private void setUpVAO(Loader loader) {
		// Just x and z vertex positions here, y is set to 0 in v.shader
		float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
		quad = loader.loadToVao(vertices, 2);
	}
}
