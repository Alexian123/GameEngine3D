package com.alexian123.util.gl.uniforms;

import org.lwjgl.opengl.GL20;

import com.alexian123.util.enums.UniformName;

public abstract class UniformArray<T> {
	
	protected final int[] locations;

	protected UniformArray(UniformName name, int size, int programID) {
		this.locations = new int[size];
		for (int i = 0; i < size; ++i) {
			locations[i] = GL20.glGetUniformLocation(programID, name.getValue() + "[" + i + "]");
		}
	}
	
	public abstract void load(T[] values);
}
