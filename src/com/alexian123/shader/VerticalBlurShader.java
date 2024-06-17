package com.alexian123.shader;

import com.alexian123.util.enums.Attribute;
import com.alexian123.util.enums.Uniform;

public class VerticalBlurShader extends ShaderProgram {
	
	private static final String VERTEX_SHADER_FILE = "blur_v";
	private static final String FRAGMENT_SHADER_FILE = "blur";

	public VerticalBlurShader() {
		super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
	}
	
	public void loadTargetHeight(float height) {
		loadFloat(uniforms.get(Uniform.TARGET_HEIGHT), height);
	}
	
	@Override
	protected int setAttributes() {
		int attribNo = 0;
		attributes.put(Attribute.POSITION, attribNo++);
		return attribNo;
	}
	
	@Override
	protected void setUniforms() {
		uniforms.put(Uniform.TARGET_HEIGHT, NEW_UNIFORM);
	}
}
