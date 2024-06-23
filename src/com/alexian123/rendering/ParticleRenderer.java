package com.alexian123.rendering;

import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.alexian123.engine.GameManager;
import com.alexian123.game.Camera;
import com.alexian123.loader.Loader;
import com.alexian123.model.ModelMesh;
import com.alexian123.particle.Particle;
import com.alexian123.particle.ParticleSystem;
import com.alexian123.shader.ShaderProgram;
import com.alexian123.texture.ParticleTexture;
import com.alexian123.util.enums.UniformName;
import com.alexian123.util.gl.GLControl;
import com.alexian123.util.gl.uniforms.UniformFloat;
import com.alexian123.util.gl.uniforms.UniformMat4;

public class ParticleRenderer {
	
	private static final float[] VERTICES = { -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f };
	private static final int INSTANCE_DATA_LENGTH = 21;
	
	private static final String VERTEX_SHADER_FILE = "particle";
	private static final String FRAGMENT_SHADER_FILE = "particle";
	
	private final ModelMesh quad;
	
	private final ShaderProgram shader;
	private final UniformMat4 projectionMatrix;
	private final UniformFloat atlasDimension;
	
	private int pointer = 0;
	
	public ParticleRenderer(Loader loader) {
		quad = loader.loadToVao(VERTICES, 2);
		quad.getVao().createInstancedBuffer(INSTANCE_DATA_LENGTH * GameManager.SETTINGS.maxParticles);
		for (int i = 1; i <= 6; ++i) {
			quad.getVao().addInstancedAttribute(i, i != 6 ? 4 : 1, INSTANCE_DATA_LENGTH, (i - 1) * 4);
		}
		
		shader = new ShaderProgram(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
		int id = shader.getProgramID();
		projectionMatrix = new UniformMat4(UniformName.PROJECTION_MATRIX, id);
		atlasDimension = new UniformFloat(UniformName.ATLAS_DIMENSION, id);
		
		shader.start();
		projectionMatrix.load(GameManager.SETTINGS.projectionMatrix.getValue());
		shader.stop();
	}
	
	public void render(List<ParticleSystem> systemsOrder, Map<ParticleSystem, List<Particle>> particles, Camera camera) {
		Matrix4f viewMatrix = camera.getViewMatrix();
		prepare();
		for (ParticleSystem system : systemsOrder) {
			bindTexture(system.getTexture());
			List<Particle> particleList = particles.get(system);
			pointer = 0;
			int size = Math.min(GameManager.SETTINGS.maxParticles, particleList.size());
			float[] vboData = new float[size * INSTANCE_DATA_LENGTH];
			for (int i = 0; i < size; ++i) {
				Particle particle = particleList.get(i);
				updateModelViewMatrix(particle, viewMatrix, vboData);
				updateAtlasInfo(particle, vboData);
			}
			quad.getVao().updateInstancedBuffer(vboData);
			GLControl.drawArraysInstancedTS(quad.getVertexCount(), size);
		}
		endRendering();
	}

	public void cleanup() {
		shader.cleanup();
	}
	
	private void prepare() {
		shader.start();
		quad.getVao().bind(0, 1, 2, 3, 4, 5, 6);
		GLControl.disableDepthMask();
	}
	
	private void bindTexture(ParticleTexture texture) {
		atlasDimension.load((float) texture.getAtlasDimension());
		if (texture.isAdditiveBlending()) {
			GLControl.enableAdditiveBlending();
		} else {
			GLControl.enableBlending();
		}
		texture.getAtlas().bindToUnit(0);
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
		quad.getVao().unbind(0, 1, 2, 3, 4, 5, 6);
		GLControl.disableBlending();
		GLControl.enableDepthMask();
	}
}
