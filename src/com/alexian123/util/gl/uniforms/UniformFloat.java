package com.alexian123.util.gl.uniforms;

import org.lwjgl.opengl.GL20;

import com.alexian123.util.enums.UniformName;

public class UniformFloat extends Uniform<Float> {

	public UniformFloat(UniformName name, int programID) {
		super(name, programID);
	}

	@Override
	public void load(Float value) {
		GL20.glUniform1f(location, value);
	}

}
