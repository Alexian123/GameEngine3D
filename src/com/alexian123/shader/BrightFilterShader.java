package com.alexian123.shader;

import com.alexian123.util.enums.Attribute;

public class BrightFilterShader extends ShaderProgram{

	private static final String VERTEX_SHADER_FILE = "simple_quad";
	private static final String FRAGMENT_SHADER_FILE = "bright_filter";

	public BrightFilterShader() {
		super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
	}
	
	@Override
	protected int setAttributes() {
		int attribNo = 0;
		attributes.put(Attribute.POSITION, attribNo++);
		return attribNo;
	}

	@Override
	protected void setUniforms() {

	}
	
}
