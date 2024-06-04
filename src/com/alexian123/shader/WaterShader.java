package com.alexian123.shader;

import org.lwjgl.util.vector.Matrix4f;

import com.alexian123.util.Maths;
import com.alexian123.entity.Camera;

public class WaterShader extends ShaderProgram {

	private static final String VERTEX_SHADER_FILE = "src/com/alexian123/shader/glsl/water_shader.vert";
	private static final String FRAGMENT_SHADER_FILE = "src/com/alexian123/shader/glsl/water_shader.frag";

	private int modelMatrixLocation;
	private int viewMatrixLocation;
	private int projectionMatrixLocation;
	private int reflectionTextureLocation;
	private int refractionTextureLocation;
	private int dudvMapLocation;
	private int moveFactorLocation;

	public WaterShader() {
		super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
	}
	
	public void loadViewMatrix(Camera camera) {
		super.loadMatrix(viewMatrixLocation, Maths.createViewMatrix(camera));
	}
	
	public void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(modelMatrixLocation, projectionMatrix);
	}

	public void loadModelMatrix(Matrix4f modelMatrix){
		super.loadMatrix(projectionMatrixLocation, modelMatrix);
	}
	
	public void connectTextureUnits() {
		super.loadInt(reflectionTextureLocation, 0);
		super.loadInt(refractionTextureLocation, 1);
		super.loadInt(dudvMapLocation, 2);
	}
	
	public void loadMoveFactor(float moveFactor) {
		super.loadFLoat(moveFactorLocation, moveFactor);
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttrib(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		modelMatrixLocation = super.getUniformLocation("projectionMatrix");
		viewMatrixLocation = super.getUniformLocation("viewMatrix");
		projectionMatrixLocation = super.getUniformLocation("modelMatrix");
		reflectionTextureLocation = super.getUniformLocation("reflectionTexture");
		refractionTextureLocation = super.getUniformLocation("refractionTexture");
		dudvMapLocation = super.getUniformLocation("dudvMap");
		moveFactorLocation = super.getUniformLocation("moveFactor");
	}

}