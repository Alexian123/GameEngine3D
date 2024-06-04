package com.alexian123.terrain;

public class Water {
	
	public static final float TILE_SIZE = 60.0f;
	
	private final float height;
	private final float x;
	private final float z;
	
	public Water(float centerX, float centerZ, float height) {
		this.x = centerX;
		this.z = centerZ;
		this.height = height;
	}

	public float getHeight() {
		return height;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}
}
