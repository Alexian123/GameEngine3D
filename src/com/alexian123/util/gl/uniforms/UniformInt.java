package com.alexian123.util.gl.uniforms;

import org.lwjgl.opengl.GL20;

import com.alexian123.util.enums.UniformName;

public class UniformInt extends Uniform<Integer> {

	public UniformInt(UniformName name, int programID) {
		super(name, programID);
	}

	@Override
	public void load(Integer value) {
		GL20.glUniform1i(location, value);
	}

}
