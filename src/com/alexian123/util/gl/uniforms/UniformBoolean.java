package com.alexian123.util.gl.uniforms;

import org.lwjgl.opengl.GL20;

import com.alexian123.util.enums.UniformName;

public class UniformBoolean extends Uniform<Boolean> {

	public UniformBoolean(UniformName name, int programID) {
		super(name, programID);
	}

	@Override
	public void load(Boolean value) {
		GL20.glUniform1f(location, value ? 1.0f : 0.0f);
	}

}
