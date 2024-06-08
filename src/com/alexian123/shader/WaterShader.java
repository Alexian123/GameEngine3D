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

	public WaterShader() {
		super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
	}
	
	public void loadViewMatrix(Camera camera) {
		super.loadMatrix(uniforms.get(Uniform.VIEW_MATRIX.getName()), Maths.createViewMatrix(camera));
		super.loadVector(uniforms.get(Uniform.CAMERA_POSITION.getName()), camera.getPosition());
	}
	
	public void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(uniforms.get(Uniform.PROJECTION_MATRIX.getName()), projectionMatrix);
	}

	public void loadModelMatrix(Matrix4f modelMatrix){
		super.loadMatrix(uniforms.get(Uniform.MODEL_MATRIX.getName()), modelMatrix);
	}
	
	public int connectTextureUnits() {
		int textureNo = 0;
		loadInt(uniforms.get(Uniform.REFLECTION_TEXTURE.getName()), textureNo++);
		loadInt(uniforms.get(Uniform.REFRACTION_TEXTURE.getName()), textureNo++);
		loadInt(uniforms.get(Uniform.DUDV_MAP.getName()), textureNo++);
		loadInt(uniforms.get(Uniform.NORMAL_MAP.getName()), textureNo++);
		loadInt(uniforms.get(Uniform.DEPTH_MAP.getName()), textureNo++);
		return textureNo;
	}
	
	public void loadViewPlanes(float nearPlane, float farPlane) {
		loadFLoat(uniforms.get(Uniform.NEAR_PLANE.getName()), nearPlane);
		loadFLoat(uniforms.get(Uniform.FAR_PLANE.getName()), farPlane);
	}
	
	public void loadMoveFactor(float moveFactor) {
		loadFLoat(uniforms.get(Uniform.MOVE_FACTOR.getName()), moveFactor);
	}
	
	public void loadWaveStrength(float waveStrength) {
		loadFLoat(uniforms.get(Uniform.WAVE_STRENGTH.getName()), waveStrength);
	}
	
	public void loadLights(List<Light> lights) {
		for (int i = 0; i < Light.MAX_LIGHTS; ++i) {
			if (i < lights.size()) {
				Light light = lights.get(i);
				loadVector(uniformArrays.get(Uniform.LIGHT_POSITION.getName()).get(i), light.getPosition());
				loadVector(uniformArrays.get(Uniform.LIGHT_COLOR.getName()).get(i), light.getColor());
				loadVector(uniformArrays.get(Uniform.ATTENUATION.getName()).get(i), light.getAttenuation());
			} else {
				loadVector(uniformArrays.get(Uniform.LIGHT_POSITION.getName()).get(i), Light.NO_LIGHT.getPosition());
				loadVector(uniformArrays.get(Uniform.LIGHT_COLOR.getName()).get(i), Light.NO_LIGHT.getColor());
				loadVector(uniformArrays.get(Uniform.ATTENUATION.getName()).get(i), Light.NO_LIGHT.getAttenuation());
			}
		}
	}
	
	public void loadShineParameters(float shineDamper, float reflectivity) {
		loadFLoat(uniforms.get(Uniform.SHINE_DAMPER.getName()), shineDamper);
		loadFLoat(uniforms.get(Uniform.REFLECTIVITY.getName()), reflectivity);
	}
	
	public void loadTilingFactor(float tilingFactor) {
		loadFLoat(uniforms.get(Uniform.TILING_FACTOR.getName()), tilingFactor);
	}
	
	public void loadFog(float density, float gradient, Vector3f color) {
		loadFLoat(uniforms.get(Uniform.FOG_DENSITY.getName()), density);
		loadFLoat(uniforms.get(Uniform.FOG_GRADIENT.getName()), gradient);
		loadVector(uniforms.get(Uniform.FOG_COLOR.getName()), color);
	}
	
	@Override
	protected int setAttributes() {
		int attribNo = 0;
		attributes.put(Attribute.POSITION.getName(), attribNo++);
		return attribNo;
	}

	@Override
	protected void setUniforms() {		
		uniforms.put(Uniform.MODEL_MATRIX.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.VIEW_MATRIX.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.PROJECTION_MATRIX.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.REFLECTION_TEXTURE.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.REFRACTION_TEXTURE.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.DUDV_MAP.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.NORMAL_MAP.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.DEPTH_MAP.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.NEAR_PLANE.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.FAR_PLANE.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.MOVE_FACTOR.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.WAVE_STRENGTH.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.CAMERA_POSITION.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.SHINE_DAMPER.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.REFLECTIVITY.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.TILING_FACTOR.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.FOG_DENSITY.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.FOG_GRADIENT.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.FOG_COLOR.getName(), NEW_UNIFORM);
	
		uniformArrays.put(Uniform.LIGHT_POSITION.getName(), createNewUniformArray(Light.MAX_LIGHTS));
		uniformArrays.put(Uniform.LIGHT_COLOR.getName(), createNewUniformArray(Light.MAX_LIGHTS));
		uniformArrays.put(Uniform.ATTENUATION.getName(), createNewUniformArray(Light.MAX_LIGHTS));
	}

}