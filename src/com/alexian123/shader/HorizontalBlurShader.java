package com.alexian123.shader;

import com.alexian123.util.enums.Attribute;
import com.alexian123.util.enums.Uniform;

public class HorizontalBlurShader extends ShaderProgram {
	
	private static final String VERTEX_SHADER_FILE = "/com/alexian123/shader/glsl/vertex/blur_h.vert";
	private static final String FRAGMENT_SHADER_FILE = "/com/alexian123/shader/glsl/fragment/blur.frag";

	public HorizontalBlurShader() {
		super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
	}
	
	public void loadTargetWidth(float width) {
		loadFloat(uniforms.get(Uniform.TARGET_WIDTH), width);
	}
	
	@Override
	protected int setAttributes() {
		int attribNo = 0;
		attributes.put(Attribute.POSITION, attribNo++);
		return attribNo;
	}
	
	@Override
	protected void setUniforms() {
		uniforms.put(Uniform.TARGET_WIDTH, NEW_UNIFORM);
	}
}
