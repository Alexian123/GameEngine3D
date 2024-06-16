package com.alexian123.shader;

public class CombineFilterShader extends ShaderProgram {
	
	private static final String VERTEX_SHADER_FILE = "/com/alexian123/shader/glsl/vertex/simple_quad.vert";
	private static final String FRAGMENT_SHADER_FILE = "/com/alexian123/shader/glsl/fragment/combine_filter.frag";

	public CombineFilterShader() {
		super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
	}
	
	public int connectTextureUnits() {
		int textureNo = 0;
		loadInt(uniforms.get(Uniform.COLOR_TEXTURE), textureNo++);
		loadInt(uniforms.get(Uniform.HIGHLIGHT_TEXTURE), textureNo++);
		return textureNo;
	}
	
	public void loadBloomFactor(float bloomFactor) {
		loadFloat(uniforms.get(Uniform.BLOOM_FACTOR), bloomFactor);
	}
	
	@Override
	protected int setAttributes() {
		int attribNo = 0;
		attributes.put(Attribute.POSITION, attribNo++);
		return attribNo;
	}

	@Override
	protected void setUniforms() {
		uniforms.put(Uniform.COLOR_TEXTURE, NEW_UNIFORM);
		uniforms.put(Uniform.HIGHLIGHT_TEXTURE, NEW_UNIFORM);
		uniforms.put(Uniform.BLOOM_FACTOR, NEW_UNIFORM);
	}
}
