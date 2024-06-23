package com.alexian123.util.gl.uniforms;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector4f;

import com.alexian123.util.enums.UniformName;

public class UniformVec4 extends Uniform<Vector4f> {

	public UniformVec4(UniformName name, int programID) {
		super(name, programID);
	}

	@Override
	public void load(Vector4f value) {
		GL20.glUniform4f(location, value.x, value.y, value.z, value.w);
	}

}
