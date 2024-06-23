package com.alexian123.util.gl.uniforms;

import org.lwjgl.opengl.GL20;

import com.alexian123.util.enums.UniformName;

public abstract class Uniform<T> {

	protected final int location;
	
	protected Uniform(UniformName name, int programID) {
		this.location = GL20.glGetUniformLocation(programID, name.getValue());
	}

	public abstract void load(T value);
}
