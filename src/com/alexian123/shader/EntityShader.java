package com.alexian123.shader;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.alexian123.entity.Camera;
import com.alexian123.light.Light;
import com.alexian123.util.Maths;

public class EntityShader extends ShaderProgram {
	
	private static final String VERTEX_SHADER_FILE = "src/com/alexian123/shader/glsl/entity_shader.vert";
	private static final String FRAGMENT_SHADER_FILE = "src/com/alexian123/shader/glsl/entity_shader.frag";
	
	private int transformationMatrixLocation;
	private int projectionMatrixLocation;
	private int viewMatrixLocation;
	private int lightPositionLocations[];
	private int lightColorLocations[];
	private int attenuationLocations[];
	private int shineDamperLocation;
	private int reflectivityLocation;
	private int useFakeLightingLocation;
	private int fogColorLocation;
	private int atlasDimensionLocation;
	private int atlasOffsetLocation;
	private int clipPlaneLocation;

	public EntityShader() {
		super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
	}
	
	public void loadFogColor(Vector3f fogColor) {
		super.loadVector(fogColorLocation, fogColor);
	}
	
	public void loadLights(List<Light> lights) {
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
	
	public void loadViewMatrix(Camera camera) {
		super.loadMatrix(viewMatrixLocation, Maths.createViewMatrix(camera));
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
		transformationMatrixLocation = super.getUniformLocation("transformationMatrix");
		projectionMatrixLocation = super.getUniformLocation("projectionMatrix");
		viewMatrixLocation = super.getUniformLocation("viewMatrix");
		lightPositionLocations = new int[MAX_LIGHTS];
		lightColorLocations = new int[MAX_LIGHTS];
		attenuationLocations = new int[MAX_LIGHTS];
		shineDamperLocation = super.getUniformLocation("shineDamper");
		reflectivityLocation = super.getUniformLocation("reflectivity");
		useFakeLightingLocation = super.getUniformLocation("useFakeLighting");
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
