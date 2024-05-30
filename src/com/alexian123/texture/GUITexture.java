package com.alexian123.texture;

import org.lwjgl.util.vector.Vector2f;

public class GUITexture {
	
	private final int id;
	private final Vector2f position;
	private final Vector2f scale;
	
	public GUITexture(int id, Vector2f position, Vector2f scale) {
		this.id = id;
		this.position = position;
		this.scale = scale;
	}

	public int getID() {
		return id;
	}

	public Vector2f getPosition() {
		return position;
	}

	public Vector2f getScale() {
		return scale;
	}
}
