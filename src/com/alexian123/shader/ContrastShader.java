package com.alexian123.shader;

import com.alexian123.util.enums.Attribute;
import com.alexian123.util.enums.Uniform;

public class ContrastShader extends ShaderProgram {

	private static final String VERTEX_SHADER_FILE = "/com/alexian123/shader/glsl/vertex/simple_quad.vert";
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
