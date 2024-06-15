package com.alexian123.shader;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.alexian123.lighting.Light;
import com.alexian123.util.Constants;

public class EntityShader extends ShaderProgram {
	
	private static final String VERTEX_SHADER_FILE = "/com/alexian123/shader/glsl/vertex/entity.vert";
	private static final String FRAGMENT_SHADER_FILE = "/com/alexian123/shader/glsl/fragment/entity.frag";

	public EntityShader() {
		super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
	}
	
	protected EntityShader(String vertexShader, String fragmentShader) {
		super(vertexShader, fragmentShader);
	}
	
	public int connectTextureUnits() {
		int textureNo = 0;
		loadInt(uniforms.get(Uniform.MODEL_TEXTURE), textureNo++);
		loadInt(uniforms.get(Uniform.SHADOW_MAP), textureNo++);
		return textureNo;
	}
	
	public void loadFog(float density, float gradient, Vector3f color) {
		loadFloat(uniforms.get(Uniform.FOG_DENSITY), density);
		loadFloat(uniforms.get(Uniform.FOG_GRADIENT), gradient);
		loadVector(uniforms.get(Uniform.FOG_COLOR), color);
	}
	
	public void loadLights(List<Light> lights, Matrix4f viewMatrix) {
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
	
	public void loadViewMatrix(Matrix4f viewMatrix) {
		loadMatrix(uniforms.get(Uniform.VIEW_MATRIX), viewMatrix);
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		loadMatrix(uniforms.get(Uniform.TRANSFORMATION_MATRIX), matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix) {
		loadMatrix(uniforms.get(Uniform.PROJECTION_MATRIX), matrix);
	}
	
	public void loadShineParameters(float shineDamper, float reflectivity) {
		loadFloat(uniforms.get(Uniform.SHINE_DAMPER), shineDamper);
		loadFloat(uniforms.get(Uniform.REFLECTIVITY), reflectivity);
	}
	
	public void loadUseFakeLighting(boolean useFakeLighting) {
		loadBoolean(uniforms.get(Uniform.USE_FAKE_LIGHTING), useFakeLighting);
	}
	
	public void loadAtlasDimension(float atlasDimension) {
		loadFloat(uniforms.get(Uniform.ATLAS_DIMENSION), atlasDimension);
	}
	
	public void loadAtlasOffset(float offsetX, float offsetY) {
		loadVector(uniforms.get(Uniform.ATLAS_OFFSET), new Vector2f(offsetX, offsetY));
	}
	
	public void loadClipPlane(Vector4f clipPlane) {
		loadVector(uniforms.get(Uniform.CLIP_PLANE), clipPlane);
	}
	
	public void loadToShadowMapSpaceMatrix(Matrix4f matrix) {
		loadMatrix(uniforms.get(Uniform.TO_SHADOW_MAP_SPACE), matrix);
	}
	
	public void loadShadowParameters(float distance, float transition) {
		loadFloat(uniforms.get(Uniform.SHADOW_DISTANCE), distance);
		loadFloat(uniforms.get(Uniform.SHADOW_TRANSITION), transition);
	}
	
	public void loadShadowMapSize(int size) {
		loadInt(uniforms.get(Uniform.SHADOW_MAP_SIZE), size);
	}
	
	public void loadPcfCount(int pcfCount) {
		loadInt(uniforms.get(Uniform.PCF_COUNT), pcfCount);
	}
	
	@Override
	protected int setAttributes() {
		int attribNo = 0;
		attributes.put(Attribute.POSITION, attribNo++);
		attributes.put(Attribute.TEXTURE_COORD, attribNo++);
		attributes.put(Attribute.NORMAL, attribNo++);
		return attribNo;
	}
	
	@Override
	protected void setUniforms() {
		uniforms.put(Uniform.MODEL_TEXTURE, NEW_UNIFORM);
		uniforms.put(Uniform.TRANSFORMATION_MATRIX, NEW_UNIFORM);
		uniforms.put(Uniform.PROJECTION_MATRIX, NEW_UNIFORM);
		uniforms.put(Uniform.VIEW_MATRIX, NEW_UNIFORM);
		uniforms.put(Uniform.SHINE_DAMPER, NEW_UNIFORM);
		uniforms.put(Uniform.REFLECTIVITY, NEW_UNIFORM);
		uniforms.put(Uniform.USE_FAKE_LIGHTING, NEW_UNIFORM);
		uniforms.put(Uniform.FOG_DENSITY, NEW_UNIFORM);
		uniforms.put(Uniform.FOG_GRADIENT, NEW_UNIFORM);
		uniforms.put(Uniform.FOG_COLOR, NEW_UNIFORM);
		uniforms.put(Uniform.ATLAS_DIMENSION, NEW_UNIFORM);
		uniforms.put(Uniform.ATLAS_OFFSET, NEW_UNIFORM);
		uniforms.put(Uniform.CLIP_PLANE, NEW_UNIFORM);
		uniforms.put(Uniform.TO_SHADOW_MAP_SPACE, NEW_UNIFORM);
		uniforms.put(Uniform.SHADOW_MAP, NEW_UNIFORM);
		uniforms.put(Uniform.SHADOW_DISTANCE, NEW_UNIFORM);
		uniforms.put(Uniform.SHADOW_TRANSITION, NEW_UNIFORM);
		uniforms.put(Uniform.SHADOW_MAP_SIZE, NEW_UNIFORM);
		uniforms.put(Uniform.PCF_COUNT, NEW_UNIFORM);
	
		uniformArrays.put(Uniform.LIGHT_POSITION, createNewUniformArray(Constants.MAX_LIGHTS));
		uniformArrays.put(Uniform.LIGHT_COLOR, createNewUniformArray(Constants.MAX_LIGHTS));
		uniformArrays.put(Uniform.ATTENUATION, createNewUniformArray(Constants.MAX_LIGHTS));
	}

}
