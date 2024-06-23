package com.alexian123.util.immutable;

import org.lwjgl.util.vector.Vector4f;

public class ImmutableVector4f {

	private final Vector4f value;
	
	public ImmutableVector4f(Vector4f value) {
		this.value = value;
	}
	
	public ImmutableVector4f(float x, float y, float z, float w) {
		this.value = new Vector4f(x, y, z, w);
	}
	
	public Vector4f getValue() {
		return new Vector4f(value);
	}
}
