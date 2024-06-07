package com.alexian123.shader;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.alexian123.entity.Light;

public class EntityShader extends ShaderProgram {
	
	private static final String VERTEX_SHADER_FILE = "src/com/alexian123/shader/glsl/entity_shader.vert";
	private static final String FRAGMENT_SHADER_FILE = "src/com/alexian123/shader/glsl/entity_shader.frag";
	
	protected int modelTextureLocation;
	protected int transformationMatrixLocation;
	protected int projectionMatrixLocation;
	protected int viewMatrixLocation;
	protected int lightPositionLocations[];
	protected int lightColorLocations[];
	protected int attenuationLocations[];
	protected int shineDamperLocation;
	protected int reflectivityLocation;
	protected int useFakeLightingLocation;
	protected int fogDensityLocation;
	protected int fogGradientLocation;
	protected int fogColorLocation;
	protected int atlasDimensionLocation;
	protected int atlasOffsetLocation;
	protected int clipPlaneLocation;

	public EntityShader() {
		super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
	}
	
	protected EntityShader(String vertexShader, String fragmentShader) {
		super(vertexShader, fragmentShader);
	}
	
	public void connectTextureUnits(){
		super.loadInt(modelTextureLocation, 0);
	}
	
	public void loadFog(float density, float gradient, Vector3f color) {
		super.loadFLoat(fogDensityLocation, density);
		super.loadFLoat(fogGradientLocation, gradient);
		super.loadVector(fogColorLocation, color);
	}
	
	public void loadLights(List<Light> lights, Matrix4f viewMatrix) {
		for (int i = 0; i < MAX_LIGHTS; ++i) {
			if (i < lights.size()) {
				Light light = lights.get(i);
				super.loadVector(lightPositionLocations[i], light.getPosition());
				super.loadVector(lightColorLocations[i], light.getColor());
				super.loadVector(attenuationLocations[i], light.getAttenuation());
			} else {
				super.loadVector(lightPositionLocations[i], new Vector3f(0, 0, 0));
				super.loadVector(lightColorLocations[i], new Vector3f(0, 0, 0));
				super.loadVector(attenuationLocations[i], new Vector3f(1, 0, 0));
			}
		}
	}
	
	public void loadViewMatrix(Matrix4f viewMatrix) {
		super.loadMatrix(viewMatrixLocation, viewMatrix);
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(transformationMatrixLocation, matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(projectionMatrixLocation, matrix);
	}
	
	public void loadShineParameters(float shineDamper, float reflectivity) {
		super.loadFLoat(shineDamperLocation, shineDamper);
		super.loadFLoat(reflectivityLocation, reflectivity);
	}
	
	public void loadUseFakeLighting(boolean useFakeLighting) {
		super.loadBoolean(useFakeLightingLocation, useFakeLighting);
	}
	
	public void loadAtlasDimension(float atlasDimension) {
		super.loadFLoat(atlasDimensionLocation, atlasDimension);
	}
	
	public void loadAtlasOffset(float offsetX, float offsetY) {
		super.loadVector(atlasOffsetLocation, new Vector2f(offsetX, offsetY));
	}
	
	public void loadClipPlane(Vector4f clipPlane) {
		super.loadVector(clipPlaneLocation, clipPlane);
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttrib(0, "position");
		super.bindAttrib(1, "textureCoord");
		super.bindAttrib(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		modelTextureLocation = super.getUniformLocation("modelTexture");
		transformationMatrixLocation = super.getUniformLocation("transformationMatrix");
		projectionMatrixLocation = super.getUniformLocation("projectionMatrix");
		viewMatrixLocation = super.getUniformLocation("viewMatrix");
		lightPositionLocations = new int[MAX_LIGHTS];
		lightColorLocations = new int[MAX_LIGHTS];
		attenuationLocations = new int[MAX_LIGHTS];
		shineDamperLocation = super.getUniformLocation("shineDamper");
		reflectivityLocation = super.getUniformLocation("reflectivity");
		useFakeLightingLocation = super.getUniformLocation("useFakeLighting");
		fogDensityLocation = super.getUniformLocation("fogDensity");
		fogGradientLocation = super.getUniformLocation("fogGradient");
		fogColorLocation = super.getUniformLocation("fogColor");
		atlasDimensionLocation = super.getUniformLocation("atlasDimension");
		atlasOffsetLocation = super.getUniformLocation("atlasOffset");
		clipPlaneLocation = super.getUniformLocation("clipPlane");
		
		for (int i = 0; i < MAX_LIGHTS; ++i) {
			lightPositionLocations[i] = super.getUniformLocation("lightPosition[" + i + "]");
			lightColorLocations[i] = super.getUniformLocation("lightColor[" + i + "]");
			attenuationLocations[i] = super.getUniformLocation("attenuation[" + i + "]");
		}
	}

}
