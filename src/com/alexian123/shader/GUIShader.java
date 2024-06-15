package com.alexian123.shader;

import org.lwjgl.util.vector.Matrix4f;

public class GUIShader extends ShaderProgram {
	
	private static final String VERTEX_SHADER_FILE = "/com/alexian123/shader/glsl/vertex/gui.vert";
	private static final String FRAGMENT_SHADER_FILE = "/com/alexian123/shader/glsl/fragment/gui.frag";

	public GUIShader() {
		super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
	}
	
	public void loadTransformationMatrix(Matrix4f matrix){
		loadMatrix(uniforms.get(Uniform.TRANSFORMATION_MATRIX.getName()), matrix);
	}
	
	@Override
	protected int setAttributes() {
		int attribNo = 0;
		attributes.put(Attribute.POSITION.getName(), attribNo++);
		return attribNo;
	}

	@Override
	protected void setUniforms() {
		uniforms.put(Uniform.TRANSFORMATION_MATRIX.getName(), NEW_UNIFORM);
	}
}