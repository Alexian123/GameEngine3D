package com.alexian123.shaders;

import org.lwjgl.util.vector.Matrix4f;

import com.alexian123.entities.Camera;
import com.alexian123.utils.Maths;

public class StaticShader extends ShaderProgram {
	
	private static final String VERTEX_SHADER_FILE = "src/com/alexian123/shaders/shader.vert";
	private static final String FRAGMENT_SHADER_FILE = "src/com/alexian123/shaders/shader.frag";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;

	public StaticShader() {
		super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix4f(location_transformationMatrix, matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix4f(location_projectionMatrix, matrix);
	}
	
	public void loadViewMatrix(Camera camera) {
		super.loadMatrix4f(location_viewMatrix, Maths.createViewMatrix(camera));
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttrib(0, "position");
		super.bindAttrib(1, "textureCoords");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
	}

}
