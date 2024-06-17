package com.alexian123.shader;

import org.lwjgl.util.vector.Matrix4f;

import com.alexian123.util.enums.Attribute;
import com.alexian123.util.enums.Uniform;

public class ParticleShader extends ShaderProgram{
	
	private static final String VERTEX_SHADER_FILE = "particle";
	private static final String FRAGMENT_SHADER_FILE = "particle";

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
