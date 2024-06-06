package com.alexian123.shader;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.alexian123.entity.Camera;
import com.alexian123.light.Light;
import com.alexian123.util.Maths;

public class TerrainShader extends ShaderProgram {

	private static final String VERTEX_SHADER_FILE = "src/com/alexian123/shader/glsl/terrain_shader.vert";
	private static final String FRAGMENT_SHADER_FILE = "src/com/alexian123/shader/glsl/terrain_shader.frag";

	private int transformationMatrixLocation;
	private int projectionMatrixLocation;
	private int viewMatrixLocation;
	private int lightPositionLocations[];
	private int lightColorLocations[];
	private int attenuationLocations[];
	private int shineDamperLocation;
	private int reflectivityLocation;
	private int fogDensityLocation;
	private int fogGradientLocation;
	private int fogColorLocation;
	private int bgTextureLocation;
	private int rTextureLocation;
	private int gTextureLocation;
	private int bTextureLocation;
	private int blendMapLocation;
	private int clipPlaneLocation;
	
	public TerrainShader() {
		super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
	}
	
	public void loadFog(float density, float gradient, Vector3f color) {
		super.loadFLoat(fogDensityLocation, density);
		super.loadFLoat(fogGradientLocation, gradient);
		super.loadVector(fogColorLocation, color);
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
	
	public void connectTextureUnits() {
		super.loadInt(bgTextureLocation, 0);
		super.loadInt(rTextureLocation, 1);
		super.loadInt(gTextureLocation, 2);
		super.loadInt(bTextureLocation, 3);
		super.loadInt(blendMapLocation, 4);
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
		fogDensityLocation = super.getUniformLocation("fogDensity");
		fogGradientLocation = super.getUniformLocation("fogGradient");
		fogColorLocation = super.getUniformLocation("fogColor");
		bgTextureLocation = super.getUniformLocation("bgTexture");
		rTextureLocation = super.getUniformLocation("rTexture");
		gTextureLocation = super.getUniformLocation("gTexture");
		bTextureLocation = super.getUniformLocation("bTexture");
		blendMapLocation = super.getUniformLocation("blendMap");
		clipPlaneLocation = super.getUniformLocation("clipPlane");
		
		for (int i = 0; i < MAX_LIGHTS; ++i) {
			lightPositionLocations[i] = super.getUniformLocation("lightPosition[" + i + "]");
			lightColorLocations[i] = super.getUniformLocation("lightColor[" + i + "]");
			attenuationLocations[i] = super.getUniformLocation("attenuation[" + i + "]");
		}
	}
}
