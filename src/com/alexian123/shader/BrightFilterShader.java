package com.alexian123.shader;

import com.alexian123.util.enums.Attribute;

public class BrightFilterShader extends ShaderProgram{

	private static final String VERTEX_SHADER_FILE = "/com/alexian123/shader/glsl/vertex/simple_quad.vert";
	private static final String FRAGMENT_SHADER_FILE = "/com/alexian123/shader/glsl/fragment/bright_filter.frag";

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
