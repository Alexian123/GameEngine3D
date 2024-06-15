package com.alexian123.shader;

import org.lwjgl.util.vector.Matrix4f;

public class ParticleShader extends ShaderProgram{
	
	private static final String VERTEX_SHADER_FILE = "/com/alexian123/shader/glsl/vertex/particle.vert";
	private static final String FRAGMENT_SHADER_FILE = "/com/alexian123/shader/glsl/fragment/particle.frag";

	public ParticleShader() {
		super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix) {
		loadMatrix(uniforms.get(Uniform.PROJECTION_MATRIX), matrix);
	}
	
	public void loadAtlasDimension(float dimension) {
		loadFloat(uniforms.get(Uniform.ATLAS_DIMENSION), dimension);
	}

	@Override
	protected int setAttributes() {
		int attribNo = 0;
		attributes.put(Attribute.POSITION, attribNo++);
		attributes.put(Attribute.MODEL_VIEW_MATRIX, attribNo);
		attribNo += 4;
		attributes.put(Attribute.ATLAS_OFFSETS, attribNo++);
		attributes.put(Attribute.BLEND_FACTOR, attribNo++);
		return attribNo;
	}

	@Override
	protected void setUniforms() {
		uniforms.put(Uniform.PROJECTION_MATRIX, NEW_UNIFORM);
		uniforms.put(Uniform.ATLAS_DIMENSION, NEW_UNIFORM);
	}
}
