package com.alexian123.rendering;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
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
import com.alexian123.util.mathematics.MatrixCreator;

public class ParticleRenderer {
	
	private static final float[] VERTICES = { -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f };
	private static final int INSTANCE_DATA_LENGTH = 21;
	
	private static final FloatBuffer buffer = BufferUtils.createFloatBuffer(Constants.MAX_PARTICLES * INSTANCE_DATA_LENGTH);
	
	private final Loader loader;
	private final int vbo;
	
	private final RawModel quad;
	private final ParticleShader shader = new ParticleShader();
	
	private int pointer = 0;
	
	public ParticleRenderer(Loader loader) {
		this.loader = loader;
		this.vbo = loader.createEmptyVbo(INSTANCE_DATA_LENGTH * Constants.MAX_PARTICLES);
		quad = loader.loadToVao(VERTICES, 2);
		for (int i = 1; i <= 6; ++i) {
			loader.addInstancedAttribute(quad.getVaoID(), vbo, i, i != 6 ? 4 : 1, INSTANCE_DATA_LENGTH, (i - 1) * 4);
		}
		shader.start();
		shader.loadProjectionMatrix(Constants.PROJECTION_MATRIX);
		shader.stop();
	}
	
	public void render(List<ParticleSystem> systemsOrder, Map<ParticleSystem, List<Particle>> particles, Camera camera) {
		Matrix4f viewMatrix = MatrixCreator.createViewMatrix(camera);
		prepare();
		for (ParticleSystem system : systemsOrder) {
			bindTexture(system.getTexture());
			List<Particle> particleList = particles.get(system);
			pointer = 0;
			int size = Math.min(Constants.MAX_PARTICLES, particleList.size());
			float[] vboData = new float[size * INSTANCE_DATA_LENGTH];
			for (int i = 0; i < size; ++i) {
				Particle particle = particleList.get(i);
				updateModelViewMatrix(particle, viewMatrix, vboData);
				updateAtlasInfo(particle, vboData);
			}
			loader.updateVbo(vbo, vboData, buffer);
			GL31.glDrawArraysInstanced(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount(), size);
		}
		endRendering();
	}

	public void cleanup() {
		shader.cleanup();
	}
	
	private void prepare() {
		shader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		for (int i = 0; i < shader.getNumAttributes(); ++i) {
			GL20.glEnableVertexAttribArray(i);
		}
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDepthMask(false);
	}
	
	private void bindTexture(ParticleTexture texture) {
		shader.loadAtlasDimension(texture.getAtlasDimension());
		if (texture.isAdditiveBlending()) {
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		} else {
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		}
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());
	}

	private void updateModelViewMatrix(Particle particle, Matrix4f viewMatrix, float[] vboData) {
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
		storeMatrixDataInArray(modelViewMatrix, vboData);
	}
	
	private void storeMatrixDataInArray(Matrix4f matrix, float[] data) {
		data[pointer++] = matrix.m00;
		data[pointer++] = matrix.m01;
		data[pointer++] = matrix.m02;
		data[pointer++] = matrix.m03;
		
		data[pointer++] = matrix.m10;
		data[pointer++] = matrix.m11;
		data[pointer++] = matrix.m12;
		data[pointer++] = matrix.m13;
		
		data[pointer++] = matrix.m20;
		data[pointer++] = matrix.m21;
		data[pointer++] = matrix.m22;
		data[pointer++] = matrix.m23;
		
		data[pointer++] = matrix.m30;
		data[pointer++] = matrix.m31;
		data[pointer++] = matrix.m32;
		data[pointer++] = matrix.m33;
	}
	
	private void updateAtlasInfo(Particle particle, float[] data) {
		data[pointer++] = particle.getCurrentAtlasOffset().x;
		data[pointer++] = particle.getCurrentAtlasOffset().y;
		data[pointer++] = particle.getNextAtlasOffset().x;
		data[pointer++] = particle.getNextAtlasOffset().y;
		data[pointer++] = particle.getBlendFactor();
	}
	
	private void endRendering() {
		shader.stop();
		for (int i = 0; i < shader.getNumAttributes(); ++i) {
			GL20.glDisableVertexAttribArray(i);
		}
		GL30.glBindVertexArray(0);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthMask(true);
	}
}
