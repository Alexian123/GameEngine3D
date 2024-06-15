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
		loadInt(uniforms.get(Uniform.MODEL_TEXTURE.getName()), textureNo++);
		loadInt(uniforms.get(Uniform.SHADOW_MAP.getName()), textureNo++);
		return textureNo;
	}
	
	public void loadFog(float density, float gradient, Vector3f color) {
		loadFloat(uniforms.get(Uniform.FOG_DENSITY.getName()), density);
		loadFloat(uniforms.get(Uniform.FOG_GRADIENT.getName()), gradient);
		loadVector(uniforms.get(Uniform.FOG_COLOR.getName()), color);
	}
	
	public void loadLights(List<Light> lights, Matrix4f viewMatrix) {
		for (int i = 0; i < Constants.MAX_LIGHTS; ++i) {
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
	
	public void loadViewMatrix(Matrix4f viewMatrix) {
		loadMatrix(uniforms.get(Uniform.VIEW_MATRIX.getName()), viewMatrix);
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		loadMatrix(uniforms.get(Uniform.TRANSFORMATION_MATRIX.getName()), matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix) {
		loadMatrix(uniforms.get(Uniform.PROJECTION_MATRIX.getName()), matrix);
	}
	
	public void loadShineParameters(float shineDamper, float reflectivity) {
		loadFloat(uniforms.get(Uniform.SHINE_DAMPER.getName()), shineDamper);
		loadFloat(uniforms.get(Uniform.REFLECTIVITY.getName()), reflectivity);
	}
	
	public void loadUseFakeLighting(boolean useFakeLighting) {
		loadBoolean(uniforms.get(Uniform.USE_FAKE_LIGHTING.getName()), useFakeLighting);
	}
	
	public void loadAtlasDimension(float atlasDimension) {
		loadFloat(uniforms.get(Uniform.ATLAS_DIMENSION.getName()), atlasDimension);
	}
	
	public void loadAtlasOffset(float offsetX, float offsetY) {
		loadVector(uniforms.get(Uniform.ATLAS_OFFSET.getName()), new Vector2f(offsetX, offsetY));
	}
	
	public void loadClipPlane(Vector4f clipPlane) {
		loadVector(uniforms.get(Uniform.CLIP_PLANE.getName()), clipPlane);
	}
	
	public void loadToShadowMapSpaceMatrix(Matrix4f matrix) {
		loadMatrix(uniforms.get(Uniform.TO_SHADOW_MAP_SPACE.getName()), matrix);
	}
	
	public void loadShadowParameters(float distance, float transition) {
		loadFloat(uniforms.get(Uniform.SHADOW_DISTANCE.getName()), distance);
		loadFloat(uniforms.get(Uniform.SHADOW_TRANSITION.getName()), transition);
	}
	
	public void loadShadowMapSize(int size) {
		loadInt(uniforms.get(Uniform.SHADOW_MAP_SIZE.getName()), size);
	}
	
	public void loadPcfCount(int pcfCount) {
		loadInt(uniforms.get(Uniform.PCF_COUNT.getName()), pcfCount);
	}
	
	@Override
	protected int setAttributes() {
		int attribNo = 0;
		attributes.put(Attribute.POSITION.getName(), attribNo++);
		attributes.put(Attribute.TEXTURE_COORD.getName(), attribNo++);
		attributes.put(Attribute.NORMAL.getName(), attribNo++);
		return attribNo;
	}
	
	@Override
	protected void setUniforms() {
		uniforms.put(Uniform.MODEL_TEXTURE.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.TRANSFORMATION_MATRIX.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.PROJECTION_MATRIX.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.VIEW_MATRIX.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.SHINE_DAMPER.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.REFLECTIVITY.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.USE_FAKE_LIGHTING.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.FOG_DENSITY.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.FOG_GRADIENT.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.FOG_COLOR.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.ATLAS_DIMENSION.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.ATLAS_OFFSET.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.CLIP_PLANE.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.TO_SHADOW_MAP_SPACE.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.SHADOW_MAP.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.SHADOW_DISTANCE.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.SHADOW_TRANSITION.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.SHADOW_MAP_SIZE.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.PCF_COUNT.getName(), NEW_UNIFORM);
	
		uniformArrays.put(Uniform.LIGHT_POSITION.getName(), createNewUniformArray(Constants.MAX_LIGHTS));
		uniformArrays.put(Uniform.LIGHT_COLOR.getName(), createNewUniformArray(Constants.MAX_LIGHTS));
		uniformArrays.put(Uniform.ATTENUATION.getName(), createNewUniformArray(Constants.MAX_LIGHTS));
	}

}
