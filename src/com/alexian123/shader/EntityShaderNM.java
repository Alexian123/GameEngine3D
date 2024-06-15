package com.alexian123.shader;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.alexian123.lighting.Light;
import com.alexian123.util.Constants;

public class EntityShaderNM extends EntityShader {
	
	private static final String VERTEX_SHADER_FILE = "/com/alexian123/shader/glsl/vertex/entity_nm.vert";
	private static final String FRAGMENT_SHADER_FILE = "/com/alexian123/shader/glsl/fragment/entity_nm.frag";
	
	public EntityShaderNM() {
		super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
	}
	
	@Override
	public int connectTextureUnits() {
		int textureNo = super.connectTextureUnits();
		loadInt(uniforms.get(Uniform.NORMAL_MAP), textureNo++);
		return textureNo;
	}
	
	@Override
	public void loadLights(List<Light> lights, Matrix4f viewMatrix) {
		for (int i = 0; i < Constants.MAX_LIGHTS; ++i) {
			if (i < lights.size()) {
				Light light = lights.get(i);
				loadVector(uniformArrays.get(Uniform.LIGHT_POSITION).get(i), getEyeSpaceLightPosition(light, viewMatrix));
				loadVector(uniformArrays.get(Uniform.LIGHT_COLOR).get(i), light.getColor());
				loadVector(uniformArrays.get(Uniform.ATTENUATION).get(i), light.getAttenuation());
			} else {
				loadVector(uniformArrays.get(Uniform.LIGHT_POSITION).get(i), getEyeSpaceLightPosition(Light.NO_LIGHT, viewMatrix));
				loadVector(uniformArrays.get(Uniform.LIGHT_COLOR).get(i), Light.NO_LIGHT.getColor());
				loadVector(uniformArrays.get(Uniform.ATTENUATION).get(i), Light.NO_LIGHT.getAttenuation());
			}
		}
	}

	@Override
	protected int setAttributes() {
		int attribNo = super.setAttributes();
		attributes.put(Attribute.TANGENT, attribNo++);
		return attribNo;
	}
	
	@Override
	protected void setUniforms() {
		super.setUniforms();
		uniforms.put(Uniform.NORMAL_MAP, NEW_UNIFORM);
	}
	
	private Vector3f getEyeSpaceLightPosition(Light light, Matrix4f viewMatrix) {
		Vector3f position = light.getPosition();
		Vector4f eyeSpacePos = new Vector4f(position.x,position.y, position.z, 1f);
		Matrix4f.transform(viewMatrix, eyeSpacePos, eyeSpacePos);
		return new Vector3f(eyeSpacePos);
	}
}
