package com.alexian123.water;

public class Water {

	private final float height;
	private final float x;
	private final float z;
	
	private final WaterFrameBuffers fbos;
	
	public Water(float centerX, float centerZ, float height) {
		this.x = centerX;
		this.z = centerZ;
		this.height = height;
		this.fbos = new WaterFrameBuffers();
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
	
	public WaterFrameBuffers getFbos() {
		return fbos;
	}
}
