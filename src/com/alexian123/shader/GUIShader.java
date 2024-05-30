package com.alexian123.shader;

import org.lwjgl.util.vector.Matrix4f;

public class GUIShader extends ShaderProgram{
	
	private static final String VERTEX_SHADER_FILE = "src/com/alexian123/shader/glsl/gui_shader.vert";
	private static final String FRAGMENT_SHADER_FILE = "src/com/alexian123/shader/glsl/gui_shader.frag";
	
	private int transformationMatrixLocation;

	public GUIShader() {
		super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
	}
	
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(transformationMatrixLocation, matrix);
	}

	@Override
	protected void getAllUniformLocations() {
		transformationMatrixLocation = super.getUniformLocation("transformationMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttrib(0, "position");
	}
}