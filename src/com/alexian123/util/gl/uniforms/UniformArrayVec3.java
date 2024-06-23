package com.alexian123.util.gl.uniforms;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector3f;

import com.alexian123.util.enums.UniformName;

public class UniformArrayVec3 extends UniformArray<Vector3f> {

	public UniformArrayVec3(UniformName name, int size, int programID) {
		super(name, size, programID);
	}

	@Override
	public void load(Vector3f[] values) {
		for (int i = 0; i < values.length; ++i) {
			GL20.glUniform3f(locations[i], values[i].x, values[i].y, values[i].z);
		}
	}

}
