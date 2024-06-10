package com.alexian123.rendering;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.alexian123.entity.Camera;
import com.alexian123.loader.Loader;
import com.alexian123.model.RawModel;
import com.alexian123.particle.Particle;
import com.alexian123.particle.ParticleSystem;
import com.alexian123.shader.ParticleShader;
import com.alexian123.texture.ParticleTexture;
import com.alexian123.util.Constants;
import com.alexian123.util.Maths;

public class ParticleRenderer {
	
	private static final float[] VERTICES = { -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f };
	
	private final RawModel quad;
	private final ParticleShader shader = new ParticleShader();
	
	public ParticleRenderer(Loader loader) {
		quad = loader.loadToVao(VERTICES, 2);
		shader.start();
		shader.loadProjectionMatrix(Constants.PROJECTION_MATRIX);
		shader.stop();
	}
	
	public void render(List<ParticleSystem> systemsOrder, Map<ParticleSystem, List<Particle>> particles, Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		prepare();
		for (ParticleSystem system : systemsOrder) {
			ParticleTexture texture = system.getTexture();
			if (texture.isAdditiveBlending()) {
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			} else {
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			}
			
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());
			for (Particle particle : particles.get(system)) {
				updateModelViewMatrix(particle, viewMatrix);
				shader.loadAtlasInfo(particle.getCurrentAtlasOffset(), particle.getNextAtlasOffset(), texture.getAtlasDimension(), particle.getBlendFactor());
				GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
			}
		}
		endRendering();
	}

	public void cleanup() {
		shader.cleanup();
	}
	
	private void prepare() {
		shader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDepthMask(false);
	}

	private void updateModelViewMatrix(Particle particle, Matrix4f viewMatrix) {
		Matrix4f modelMatrix = new Matrix4f();
		Matrix4f.translate(particle.getPosition(), modelMatrix, modelMatrix);
		modelMatrix.m00 = viewMatrix.m00;
		modelMatrix.m01 = viewMatrix.m10;
		modelMatrix.m02 = viewMatrix.m20;
		modelMatrix.m10 = viewMatrix.m01;
		modelMatrix.m11 = viewMatrix.m11;
		modelMatrix.m12 = viewMatrix.m21;
		modelMatrix.m20 = viewMatrix.m02;
		modelMatrix.m21 = viewMatrix.m12;
		modelMatrix.m22 = viewMatrix.m22;
		Matrix4f.rotate((float) Math.toRadians(particle.getRotation()), new Vector3f(0, 0, 1), modelMatrix, modelMatrix);
		float scale = particle.getScale();
		Matrix4f.scale(new Vector3f(scale, scale, scale), modelMatrix, modelMatrix);
		Matrix4f modelViewMatrix = Matrix4f.mul(viewMatrix, modelMatrix, null);
		shader.loadModelViewMatrix(modelViewMatrix);
	}
	
	private void endRendering() {
		shader.stop();
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthMask(true);
	}
}
