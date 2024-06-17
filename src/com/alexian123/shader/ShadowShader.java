package com.alexian123.shader;

import org.lwjgl.util.vector.Matrix4f;

import com.alexian123.util.Constants;
import com.alexian123.util.enums.Attribute;
import com.alexian123.util.enums.Uniform;

public class ShadowShader extends ShaderProgram {
	
	private static final String VERTEX_SHADER_FILE = "shadow";
	private static final String FRAGMENT_SHADER_FILE = "shadow";

	public ShadowShader() {
		super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
	}
	
	public void loadMvpMatrix(Matrix4f mvpMatrix){
		loadMatrix(uniforms.get(Uniform.MVP_MATRIX), mvpMatrix);
	}
	
	public void loadJointTransforms(Matrix4f[] jointTransforms) {
		for (int i = 0; i < jointTransforms.length; ++i) {
			loadMatrix(uniformArrays.get(Uniform.JOINT_TRANSFORMS).get(i), jointTransforms[i]);
		}
	}
	
	public void loadIsAnimated(boolean isAnimated) {
		loadBoolean(uniforms.get(Uniform.IS_ANIMATED), isAnimated);
	}

	@Override
	protected int setAttributes() {
		int attribNo = 0;
		attributes.put(Attribute.POSITION, attribNo++);
		attributes.put(Attribute.TEXTURE_COORD, attribNo++);
		++attribNo; // skip attribute 2
		attributes.put(Attribute.JOINT_INDICES, attribNo++);
		attributes.put(Attribute.WEIGHTS, attribNo++);
		return attribNo;
		
	}

	@Override
	protected void setUniforms() {
		uniforms.put(Uniform.MVP_MATRIX, NEW_UNIFORM);
		uniforms.put(Uniform.IS_ANIMATED, NEW_UNIFORM);
		uniformArrays.put(Uniform.JOINT_TRANSFORMS, createNewUniformArray(Constants.MAX_JOINTS));
	}

}