package com.alexian123.util.gl.uniforms;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector3f;

import com.alexian123.util.enums.UniformName;

public class UniformVec3 extends Uniform<Vector3f> {

	public UniformVec3(UniformName name, int programID) {
		super(name, programID);
	}

	@Override
	public void load(Vector3f value) {
		GL20.glUniform3f(location, value.x, value.y, value.z);
	}

}
