package com.alexian123.shader;

import org.lwjgl.util.vector.Matrix4f;

import com.alexian123.entity.Camera;
import com.alexian123.entity.Light;
import com.alexian123.util.Maths;

public class TerrainShader extends ShaderProgram {

	private static final String VERTEX_SHADER_FILE = "src/com/alexian123/shader/terrainShader.vert";
	private static final String FRAGMENT_SHADER_FILE = "src/com/alexian123/shader/terrainShader.frag";

	private int transformationMatrixLocation;
	private int projectionMatrixLocation;
	private int viewMatrixLocation;
	private int lightPositionLocation;
	private int lightColorLocation;
	private int shineDamperLocation;
	private int reflectivityLocation;
	
	public TerrainShader() {
		super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix4f(transformationMatrixLocation, matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix4f(projectionMatrixLocation, matrix);
	}
	
	public void loadViewMatrix(Camera camera) {
		super.loadMatrix4f(viewMatrixLocation, Maths.createViewMatrix(camera));
	}
	
	public void loadLight(Light light) {
		super.loadVector3f(lightPositionLocation, light.getPosition());
		super.loadVector3f(lightColorLocation, light.getColor());
	}
	
	public void loadShineParameters(float shineDamper, float reflectivity) {
		super.loadFLoat(shineDamperLocation, shineDamper);
		super.loadFLoat(reflectivityLocation, reflectivity);
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttrib(0, "position");
		super.bindAttrib(1, "textureCoord");
		super.bindAttrib(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		transformationMatrixLocation = super.getUniformLocation("transformationMatrix");
		projectionMatrixLocation = super.getUniformLocation("projectionMatrix");
		viewMatrixLocation = super.getUniformLocation("viewMatrix");
		lightPositionLocation = super.getUniformLocation("lightPosition");
		lightColorLocation = super.getUniformLocation("lightColor");
		shineDamperLocation = super.getUniformLocation("shineDamper");
		reflectivityLocation = super.getUniformLocation("reflectivity");
	}
}
