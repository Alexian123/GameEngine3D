package com.alexian123.shader;

public class ContrastShader extends ShaderProgram {

	private static final String VERTEX_SHADER_FILE = "/com/alexian123/shader/glsl/vertex/contrast.vert";
	private static final String FRAGMENT_SHADER_FILE = "/com/alexian123/shader/glsl/fragment/contrast.frag";

	public ContrastShader() {
		super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
	}
	
	public void loadContrast(float contrast) {
		loadFloat(uniforms.get(Uniform.CONTRAST), contrast);
	}
	
	@Override
	protected int setAttributes() {
		int attribNo = 0;
		attributes.put(Attribute.POSITION, attribNo++);
		return attribNo;
	}
	
	@Override
	protected void setUniforms() {
		uniforms.put(Uniform.CONTRAST, NEW_UNIFORM);
	}
}
