package com.alexian123.shader;

import org.lwjgl.util.vector.Matrix4f;

import com.alexian123.entity.Camera;
import com.alexian123.util.Maths;

public class SkyBoxShader extends ShaderProgram{
	
	private static final String VERTEX_SHADER_FILE = "src/com/alexian123/shader/glsl/skybox_shader.vert";
	private static final String FRAGMENT_SHADER_FILE = "src/com/alexian123/shader/glsl/skybox_shader.frag";
	
	private int projectionMatrixLocation;
	private int viewMatrixLocation;

	public SkyBoxShader() {
		super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix){
		super.loadMatrix(projectionMatrixLocation, matrix);
	}
	
	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		// disable translation
		viewMatrix.m30 = 0;
		viewMatrix.m31 = 0;
		viewMatrix.m32 = 0;
		super.loadMatrix(viewMatrixLocation, viewMatrix);
	}

	@Override
	protected void getAllUniformLocations() {
		projectionMatrixLocation = super.getUniformLocation("projectionMatrix");
		viewMatrixLocation = super.getUniformLocation("viewMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttrib(0, "position");
	}
}