package com.alexian123.shader;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.alexian123.entity.Camera;
import com.alexian123.renderer.DisplayManager;
import com.alexian123.util.Maths;

public class SkyBoxShader extends ShaderProgram{
	
	private static final String VERTEX_SHADER_FILE = "src/com/alexian123/shader/glsl/skybox_shader.vert";
	private static final String FRAGMENT_SHADER_FILE = "src/com/alexian123/shader/glsl/skybox_shader.frag";
	
	private static final float ROTATION_INCREMENT = 1.0f;
	
	private int projectionMatrixLocation;
	private int viewMatrixLocation;
	private int fogColorLocation;
	private int cubeMap0Location;
	private int cubeMap1Location;
	private int blendFactorLocation;
	
	private float rotation = 0;

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
		// add rotation
		rotation += ROTATION_INCREMENT * DisplayManager.getFrameTimeSeconds();
		Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
		super.loadMatrix(viewMatrixLocation, viewMatrix);
	}
	
	public void loadFogColor(Vector3f fogColor) {
		super.loadVector(fogColorLocation, fogColor);
	}
	
	public void connectTextureUnits() {
		super.loadInt(cubeMap0Location, 0);
		super.loadInt(cubeMap1Location, 1);
	}
	
	public void loadBlendFactor(float blendFactor) {
		super.loadFLoat(blendFactorLocation, blendFactor);
	}

	@Override
	protected void getAllUniformLocations() {
		projectionMatrixLocation = super.getUniformLocation("projectionMatrix");
		viewMatrixLocation = super.getUniformLocation("viewMatrix");
		fogColorLocation = super.getUniformLocation("fogColor");
		cubeMap0Location = super.getUniformLocation("cubeMap0");
		cubeMap1Location = super.getUniformLocation("cubeMap1");
		blendFactorLocation = super.getUniformLocation("blendFactor");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttrib(0, "position");
	}
}