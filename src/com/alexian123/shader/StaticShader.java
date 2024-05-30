package com.alexian123.shader;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.alexian123.entity.Camera;
import com.alexian123.entity.Light;
import com.alexian123.util.Maths;

public class StaticShader extends ShaderProgram {
	
	private static final String VERTEX_SHADER_FILE = "src/com/alexian123/shader/entityShader.vert";
	private static final String FRAGMENT_SHADER_FILE = "src/com/alexian123/shader/entityShader.frag";
	
	private int transformationMatrixLocation;
	private int projectionMatrixLocation;
	private int viewMatrixLocation;
	private int lightPositionLocation;
	private int lightColorLocation;
	private int shineDamperLocation;
	private int reflectivityLocation;
	private int useFakeLightingLocation;
	private int skyColorLocation;
	private int atlasDimensionLocation;
	private int atlasOffsetLocation;

	public StaticShader() {
		super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(transformationMatrixLocation, matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(projectionMatrixLocation, matrix);
	}
	
	public void loadViewMatrix(Camera camera) {
		super.loadMatrix(viewMatrixLocation, Maths.createViewMatrix(camera));
	}
	
	public void loadLight(Light light) {
		super.loadVector(lightPositionLocation, light.getPosition());
		super.loadVector(lightColorLocation, light.getColor());
	}
	
	public void loadShineParameters(float shineDamper, float reflectivity) {
		super.loadFLoat(shineDamperLocation, shineDamper);
		super.loadFLoat(reflectivityLocation, reflectivity);
	}
	
	public void loadUseFakeLighting(boolean useFakeLighting) {
		super.loadBoolean(useFakeLightingLocation, useFakeLighting);
	}
	
	public void loadSkyColor(Vector3f skyColor) {
		super.loadVector(skyColorLocation, skyColor);
	}
	
	public void loadAtlasDimension(float atlasDimension) {
		super.loadFLoat(atlasDimensionLocation, atlasDimension);
	}
	
	public void loadAtlasOffset(float offsetX, float offsetY) {
		super.loadVector(atlasOffsetLocation, new Vector2f(offsetX, offsetY));
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
		useFakeLightingLocation = super.getUniformLocation("useFakeLighting");
		skyColorLocation = super.getUniformLocation("skyColor");
		atlasDimensionLocation = super.getUniformLocation("atlasDimension");
		atlasOffsetLocation = super.getUniformLocation("atlasOffset");
	}

}
