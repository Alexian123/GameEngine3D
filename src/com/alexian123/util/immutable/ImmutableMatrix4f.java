package com.alexian123.util.immutable;

import org.lwjgl.util.vector.Matrix4f;

public class ImmutableMatrix4f {

	private final Matrix4f value;
	
	public ImmutableMatrix4f(Matrix4f value) {
		this.value = value;
	}
	
	public Matrix4f getValue() {
		return new Matrix4f(value);
	}
}
