package com.alexian123.shaders;

public class StaticShader extends ShaderProgram {
	
	private static final String VERTEX_SHADER_FILE = "src/com/alexian123/shaders/shader.vert";
	private static final String FRAGMENT_SHADER_FILE = "src/com/alexian123/shaders/shader.frag";

	public StaticShader() {
		super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttrib(0, "position");
		super.bindAttrib(1, "textureCoords");
	}

}
