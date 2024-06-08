package com.alexian123.shader;

import org.lwjgl.util.vector.Matrix4f;

public class ParticleShader extends ShaderProgram{
	
	private static final String VERTEX_SHADER_FILE = "src/com/alexian123/shader/glsl/particle_shader.vert";
	private static final String FRAGMENT_SHADER_FILE = "src/com/alexian123/shader/glsl/particle_shader.frag";

	public ParticleShader() {
		super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
	}
	
	public void loadModelViewMatrix(Matrix4f modelView) {
		loadMatrix(uniforms.get(Uniform.MODEL_VIEW.getName()), modelView);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix) {
		loadMatrix(uniforms.get(Uniform.PROJECTION_MATRIX.getName()), matrix);
	}

	@Override
	protected int setAttributes() {
		int attribNo = 0;
		attributes.put(Attribute.POSITION.getName(), attribNo++);
		return attribNo;
	}

	@Override
	protected void setUniforms() {
		uniforms.put(Uniform.MODEL_VIEW.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.PROJECTION_MATRIX.getName(), NEW_UNIFORM);
	}
}
