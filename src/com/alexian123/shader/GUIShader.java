package com.alexian123.shader;

import org.lwjgl.util.vector.Matrix4f;

import com.alexian123.util.enums.Attribute;
import com.alexian123.util.enums.Uniform;

public class GUIShader extends ShaderProgram {
	
	private static final String VERTEX_SHADER_FILE = "gui";
	private static final String FRAGMENT_SHADER_FILE = "gui";

	public GUIShader() {
		super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
	}
	
	public void loadTransformationMatrix(Matrix4f matrix){
		loadMatrix(uniforms.get(Uniform.TRANSFORMATION_MATRIX), matrix);
	}
	
	@Override
	protected int setAttributes() {
		int attribNo = 0;
		attributes.put(Attribute.POSITION, attribNo++);
		return attribNo;
	}

	@Override
	protected void setUniforms() {
		uniforms.put(Uniform.TRANSFORMATION_MATRIX, NEW_UNIFORM);
	}
}