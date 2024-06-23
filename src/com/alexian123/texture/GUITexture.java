package com.alexian123.texture;

import org.lwjgl.util.vector.Vector2f;

import com.alexian123.util.gl.TextureSampler;

public class GUITexture {
	
	private final TextureSampler sampler;
	private final Vector2f position;
	private final Vector2f scale;
	
	public GUITexture(TextureSampler sampler, Vector2f position, Vector2f scale) {
		this.sampler = sampler;
		this.position = position;
		this.scale = scale;
	}

	public TextureSampler getSampler() {
		return sampler;
	}

	public Vector2f getPosition() {
		return position;
	}

	public Vector2f getScale() {
		return scale;
	}
}
