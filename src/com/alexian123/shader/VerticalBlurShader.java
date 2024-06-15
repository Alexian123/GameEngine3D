package com.alexian123.shader;

public class VerticalBlurShader extends ShaderProgram {
	
	private static final String VERTEX_SHADER_FILE = "/com/alexian123/shader/glsl/vertex/blur_v.vert";
	private static final String FRAGMENT_SHADER_FILE = "/com/alexian123/shader/glsl/fragment/blur.frag";

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
