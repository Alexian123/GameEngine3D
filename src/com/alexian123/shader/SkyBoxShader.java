package com.alexian123.shader;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.alexian123.engine.GameManager;
import com.alexian123.entity.Camera;
import com.alexian123.util.enums.Attribute;
import com.alexian123.util.enums.Uniform;
import com.alexian123.util.mathematics.MatrixCreator;

public class SkyBoxShader extends ShaderProgram{
	
	private static final String VERTEX_SHADER_FILE = "/com/alexian123/shader/glsl/vertex/skybox.vert";
	private static final String FRAGMENT_SHADER_FILE = "/com/alexian123/shader/glsl/fragment/skybox.frag";
	
	private static final float ROTATION_INCREMENT = 0.1f;
	
	private float rotation = 0;

	public SkyBoxShader() {
		super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix) {
		loadMatrix(uniforms.get(Uniform.PROJECTION_MATRIX), matrix);
	}
	
	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = MatrixCreator.createViewMatrix(camera);
		// disable translation
		viewMatrix.m30 = 0;
		viewMatrix.m31 = 0;
		viewMatrix.m32 = 0;
		// add rotation
		rotation += ROTATION_INCREMENT * GameManager.getFrameTimeSeconds();
		Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
		loadMatrix(uniforms.get(Uniform.VIEW_MATRIX), viewMatrix);
	}
	
	public void loadFogColor(Vector3f fogColor) {
		loadVector(uniforms.get(Uniform.FOG_COLOR), fogColor);
	}
	
	public int connectTextureUnits() {
		int textureNo = 0;
		loadInt(uniforms.get(Uniform.CUBE_MAP_0), textureNo++);
		loadInt(uniforms.get(Uniform.CUBE_MAP_1), textureNo++);
		return textureNo;
	}
	
	public void loadBlendFactor(float blendFactor) {
		loadFloat(uniforms.get(Uniform.BLEND_FACTOR), blendFactor);
	}
	
	public void loadLimits(float lower, float upper) {
		loadFloat(uniforms.get(Uniform.LOWER_LIMIT), lower);
		loadFloat(uniforms.get(Uniform.UPPER_LIMIT), upper);
	}
	
	@Override
	protected int setAttributes() {
		int attribNo = 0;
		attributes.put(Attribute.POSITION, attribNo++);
		return attribNo;
	}

	@Override
	protected void setUniforms() {
		uniforms.put(Uniform.PROJECTION_MATRIX, NEW_UNIFORM);
		uniforms.put(Uniform.VIEW_MATRIX, NEW_UNIFORM);
		uniforms.put(Uniform.FOG_COLOR, NEW_UNIFORM);
		uniforms.put(Uniform.CUBE_MAP_0, NEW_UNIFORM);
		uniforms.put(Uniform.CUBE_MAP_1, NEW_UNIFORM);
		uniforms.put(Uniform.BLEND_FACTOR, NEW_UNIFORM);
		uniforms.put(Uniform.LOWER_LIMIT, NEW_UNIFORM);
		uniforms.put(Uniform.UPPER_LIMIT, NEW_UNIFORM);
	}
}