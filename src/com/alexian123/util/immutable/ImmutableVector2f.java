package com.alexian123.util.immutable;

import org.lwjgl.util.vector.Vector2f;

public class ImmutableVector2f {

	private final Vector2f value;
	
	public ImmutableVector2f(Vector2f value) {
		this.value = value;
	}
	
	public ImmutableVector2f(float x, float y) {
		this.value = new Vector2f(x, y);
	}
	
	public Vector2f getValue() {
		return new Vector2f(value);
	}
}
