package com.alexian123.shader;

import org.lwjgl.util.vector.Matrix4f;

import com.alexian123.util.Constants;
import com.alexian123.util.enums.Attribute;
import com.alexian123.util.enums.Uniform;

public class AnimatedEntityShader extends EntityShader {
	
	private static final String VERTEX_SHADER_FILE = "animated_entity";
	private static final String FRAGMENT_SHADER_FILE = "entity";

	public AnimatedEntityShader() {
		super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
	}

	public void loadJointTransforms(Matrix4f[] jointTransforms) {
		for (int i = 0; i < jointTransforms.length; ++i) {
			loadMatrix(uniformArrays.get(Uniform.JOINT_TRANSFORMS).get(i), jointTransforms[i]);
		}
	}

	@Override
	protected int setAttributes() {
		int attribNo = super.setAttributes();
		attributes.put(Attribute.JOINT_INDICES, attribNo++);
		attributes.put(Attribute.WEIGHTS, attribNo++);
		return attribNo;
	}

	@Override
	protected void setUniforms() {
		super.setUniforms();
		uniformArrays.put(Uniform.JOINT_TRANSFORMS, createNewUniformArray(Constants.MAX_JOINTS));
	}
}
