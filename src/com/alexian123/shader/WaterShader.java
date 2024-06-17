package com.alexian123.shader;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.alexian123.util.Constants;
import com.alexian123.util.enums.Attribute;
import com.alexian123.util.enums.Uniform;
import com.alexian123.util.mathematics.MatrixCreator;
import com.alexian123.entity.Camera;
import com.alexian123.lighting.Light;

public class WaterShader extends ShaderProgram {

	private static final String VERTEX_SHADER_FILE = "/com/alexian123/shader/glsl/vertex/water.vert";
	private static final String FRAGMENT_SHADER_FILE = "/com/alexian123/shader/glsl/fragment/water.frag";

	public WaterShader() {
		super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
	}
	
	public void loadViewMatrix(Camera camera) {
		super.loadMatrix(uniforms.get(Uniform.VIEW_MATRIX), MatrixCreator.createViewMatrix(camera));
		super.loadVector(uniforms.get(Uniform.CAMERA_POSITION), camera.getPosition());
	}
	
	public void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(uniforms.get(Uniform.PROJECTION_MATRIX), projectionMatrix);
	}

	public void loadModelMatrix(Matrix4f modelMatrix){
		super.loadMatrix(uniforms.get(Uniform.MODEL_MATRIX), modelMatrix);
	}
	
	public int connectTextureUnits() {
		int textureNo = 0;
		loadInt(uniforms.get(Uniform.DUDV_MAP), textureNo++);
		loadInt(uniforms.get(Uniform.NORMAL_MAP), textureNo++);
		loadInt(uniforms.get(Uniform.REFLECTION_TEXTURE), textureNo++);
		loadInt(uniforms.get(Uniform.REFRACTION_TEXTURE), textureNo++);
		loadInt(uniforms.get(Uniform.DEPTH_MAP), textureNo++);
		return textureNo;
	}
	
	public void loadViewPlanes(float nearPlane, float farPlane) {
		loadFloat(uniforms.get(Uniform.NEAR_PLANE), nearPlane);
		loadFloat(uniforms.get(Uniform.FAR_PLANE), farPlane);
	}
	
	public void loadMoveFactor(float moveFactor) {
		loadFloat(uniforms.get(Uniform.MOVE_FACTOR), moveFactor);
	}
	
	public void loadWaveStrength(float waveStrength) {
		loadFloat(uniforms.get(Uniform.WAVE_STRENGTH), waveStrength);
	}
	
	public void loadLights(List<Light> lights) {
		for (int i = 0; i < Constants.MAX_LIGHTS; ++i) {
			if (i < lights.size()) {
				Light light = lights.get(i);
				loadVector(uniformArrays.get(Uniform.LIGHT_POSITION).get(i), light.getPosition());
				loadVector(uniformArrays.get(Uniform.LIGHT_COLOR).get(i), light.getColor());
				loadVector(uniformArrays.get(Uniform.ATTENUATION).get(i), light.getAttenuation());
			} else {
				loadVector(uniformArrays.get(Uniform.LIGHT_POSITION).get(i), Light.NO_LIGHT.getPosition());
				loadVector(uniformArrays.get(Uniform.LIGHT_COLOR).get(i), Light.NO_LIGHT.getColor());
				loadVector(uniformArrays.get(Uniform.ATTENUATION).get(i), Light.NO_LIGHT.getAttenuation());
			}
		}
	}
	
	public void loadShineParameters(float shineDamper, float reflectivity) {
		loadFloat(uniforms.get(Uniform.SHINE_DAMPER), shineDamper);
		loadFloat(uniforms.get(Uniform.REFLECTIVITY), reflectivity);
	}
	
	public void loadTilingFactor(float tilingFactor) {
		loadFloat(uniforms.get(Uniform.TILING_FACTOR), tilingFactor);
	}
	
	public void loadFog(float density, float gradient, Vector3f color) {
		loadFloat(uniforms.get(Uniform.FOG_DENSITY), density);
		loadFloat(uniforms.get(Uniform.FOG_GRADIENT), gradient);
		loadVector(uniforms.get(Uniform.FOG_COLOR), color);
	}
	
	@Override
	protected int setAttributes() {
		int attribNo = 0;
		attributes.put(Attribute.POSITION, attribNo++);
		return attribNo;
	}

	@Override
	protected void setUniforms() {		
		uniforms.put(Uniform.MODEL_MATRIX, NEW_UNIFORM);
		uniforms.put(Uniform.VIEW_MATRIX, NEW_UNIFORM);
		uniforms.put(Uniform.PROJECTION_MATRIX, NEW_UNIFORM);
		uniforms.put(Uniform.REFLECTION_TEXTURE, NEW_UNIFORM);
		uniforms.put(Uniform.REFRACTION_TEXTURE, NEW_UNIFORM);
		uniforms.put(Uniform.DUDV_MAP, NEW_UNIFORM);
		uniforms.put(Uniform.NORMAL_MAP, NEW_UNIFORM);
		uniforms.put(Uniform.DEPTH_MAP, NEW_UNIFORM);
		uniforms.put(Uniform.NEAR_PLANE, NEW_UNIFORM);
		uniforms.put(Uniform.FAR_PLANE, NEW_UNIFORM);
		uniforms.put(Uniform.MOVE_FACTOR, NEW_UNIFORM);
		uniforms.put(Uniform.WAVE_STRENGTH, NEW_UNIFORM);
		uniforms.put(Uniform.CAMERA_POSITION, NEW_UNIFORM);
		uniforms.put(Uniform.SHINE_DAMPER, NEW_UNIFORM);
		uniforms.put(Uniform.REFLECTIVITY, NEW_UNIFORM);
		uniforms.put(Uniform.TILING_FACTOR, NEW_UNIFORM);
		uniforms.put(Uniform.FOG_DENSITY, NEW_UNIFORM);
		uniforms.put(Uniform.FOG_GRADIENT, NEW_UNIFORM);
		uniforms.put(Uniform.FOG_COLOR, NEW_UNIFORM);
	
		uniformArrays.put(Uniform.LIGHT_POSITION, createNewUniformArray(Constants.MAX_LIGHTS));
		uniformArrays.put(Uniform.LIGHT_COLOR, createNewUniformArray(Constants.MAX_LIGHTS));
		uniformArrays.put(Uniform.ATTENUATION, createNewUniformArray(Constants.MAX_LIGHTS));
	}

}