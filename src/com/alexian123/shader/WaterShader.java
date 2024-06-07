package com.alexian123.shader;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.alexian123.util.Maths;
import com.alexian123.entity.Camera;
import com.alexian123.entity.Light;

public class WaterShader extends ShaderProgram {

	private static final String VERTEX_SHADER_FILE = "src/com/alexian123/shader/glsl/water_shader.vert";
	private static final String FRAGMENT_SHADER_FILE = "src/com/alexian123/shader/glsl/water_shader.frag";

	private int modelMatrixLocation;
	private int viewMatrixLocation;
	private int projectionMatrixLocation;
	private int reflectionTextureLocation;
	private int refractionTextureLocation;
	private int dudvMapLocation;
	private int normalMapLocation;
	private int depthMapLocation;
	private int nearPlaneLocation;
	private int farPlaneLocation;
	private int moveFactorLocation;
	private int waveStrengthLocation;
	private int cameraPositionLocation;
	private int lightPositionLocations[];
	private int lightColorLocations[];
	private int attenuationLocations[];
	private int shineDamperLocation;
	private int reflectivityLocation;
	private int tilingFactorLocation;
	private int fogDensityLocation;
	private int fogGradientLocation;
	private int fogColorLocation;

	public WaterShader() {
		super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
	}
	
	public void loadViewMatrix(Camera camera) {
		super.loadMatrix(viewMatrixLocation, Maths.createViewMatrix(camera));
		super.loadVector(cameraPositionLocation, camera.getPosition());
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
		super.loadInt(normalMapLocation, 3);
		super.loadInt(depthMapLocation, 4);
	}
	
	public void loadViewPlanes(float nearPlane, float farPlane) {
		super.loadFLoat(nearPlaneLocation, nearPlane);
		super.loadFLoat(farPlaneLocation, farPlane);
	}
	
	public void loadMoveFactor(float moveFactor) {
		super.loadFLoat(moveFactorLocation, moveFactor);
	}
	
	public void loadWaveStrength(float waveStrength) {
		super.loadFLoat(waveStrengthLocation, waveStrength);
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
	
	public void loadShineParameters(float shineDamper, float reflectivity) {
		super.loadFLoat(shineDamperLocation, shineDamper);
		super.loadFLoat(reflectivityLocation, reflectivity);
	}
	
	public void loadTilingFactor(float tilingFactor) {
		super.loadFLoat(tilingFactorLocation, tilingFactor);
	}
	
	public void loadFog(float density, float gradient, Vector3f color) {
		super.loadFLoat(fogDensityLocation, density);
		super.loadFLoat(fogGradientLocation, gradient);
		super.loadVector(fogColorLocation, color);
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
		normalMapLocation = super.getUniformLocation("normalMap");
		depthMapLocation = super.getUniformLocation("depthMap");
		nearPlaneLocation = super.getUniformLocation("nearPlane");
		farPlaneLocation = super.getUniformLocation("farPlane");
		moveFactorLocation = super.getUniformLocation("moveFactor");
		waveStrengthLocation = super.getUniformLocation("waveStrength");
		cameraPositionLocation = super.getUniformLocation("cameraPosition");
		lightPositionLocations = new int[MAX_LIGHTS];
		lightColorLocations = new int[MAX_LIGHTS];
		attenuationLocations = new int[MAX_LIGHTS];
		shineDamperLocation = super.getUniformLocation("shineDamper");
		reflectivityLocation = super.getUniformLocation("reflectivity");
		tilingFactorLocation = super.getUniformLocation("tilingFactor");
		fogDensityLocation = super.getUniformLocation("fogDensity");
		fogGradientLocation = super.getUniformLocation("fogGradient");
		fogColorLocation = super.getUniformLocation("fogColor");
		
		for (int i = 0; i < MAX_LIGHTS; ++i) {
			lightPositionLocations[i] = super.getUniformLocation("lightPosition[" + i + "]");
			lightColorLocations[i] = super.getUniformLocation("lightColor[" + i + "]");
			attenuationLocations[i] = super.getUniformLocation("attenuation[" + i + "]");
		}
	}

}