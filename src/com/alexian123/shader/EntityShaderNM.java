package com.alexian123.shader;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.alexian123.entity.Light;

public class EntityShaderNM extends EntityShader {
	
	private static final String VERTEX_SHADER_FILE = "src/com/alexian123/shader/glsl/entity_shader_nm.vert";
	private static final String FRAGMENT_SHADER_FILE = "src/com/alexian123/shader/glsl/entity_shader_nm.frag";
	
	private int normalMapLocation;
	
	public EntityShaderNM() {
		super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
	}
	
	@Override
	public void connectTextureUnits(){
		super.connectTextureUnits();
		super.loadInt(normalMapLocation, 1);
	}
	
	@Override
	public void loadLights(List<Light> lights, Matrix4f viewMatrix) {
		for (int i = 0; i < MAX_LIGHTS; ++i) {
			if (i < lights.size()) {
				Light light = lights.get(i);
				super.loadVector(lightPositionLocations[i], getEyeSpaceLightPosition(light, viewMatrix));
				super.loadVector(lightColorLocations[i], light.getColor());
				super.loadVector(attenuationLocations[i], light.getAttenuation());
			} else {
				super.loadVector(lightPositionLocations[i], new Vector3f(0, 0, 0));
				super.loadVector(lightColorLocations[i], new Vector3f(0, 0, 0));
				super.loadVector(attenuationLocations[i], new Vector3f(1, 0, 0));
			}
		}
	}

	@Override
	protected void bindAttributes() {
		super.bindAttributes();
		super.bindAttrib(3, "tangent");
	}
	
	@Override
	protected void getAllUniformLocations() {
		super.getAllUniformLocations();
		normalMapLocation = super.getUniformLocation("normalMap");
	}
	
	private Vector3f getEyeSpaceLightPosition(Light light, Matrix4f viewMatrix) {
		Vector3f position = light.getPosition();
		Vector4f eyeSpacePos = new Vector4f(position.x,position.y, position.z, 1f);
		Matrix4f.transform(viewMatrix, eyeSpacePos, eyeSpacePos);
		return new Vector3f(eyeSpacePos);
	}
}
