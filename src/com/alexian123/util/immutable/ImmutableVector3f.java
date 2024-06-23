package com.alexian123.util.immutable;

import org.lwjgl.util.vector.Vector3f;

public class ImmutableVector3f {

	private final Vector3f value;
	
	public ImmutableVector3f(Vector3f value) {
		this.value = value;
	}
	
	public ImmutableVector3f(float x, float y, float z) {
		this.value = new Vector3f(x, y, z);
	}
	
	public Vector3f getValue() {
		return new Vector3f(value);
	}
}
