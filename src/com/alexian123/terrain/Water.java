package com.alexian123.terrain;

import java.util.ArrayList;
import java.util.List;

public class Water {
	
	public static final float TILE_SIZE = 60.0f;
	
	private static final List<Water> instances = new ArrayList<>();
	
	private final float height;
	private final float x;
	private final float z;
	
	private final WaterFrameBuffers fbos;
	
	public Water(float centerX, float centerZ, float height) {
		this.x = centerX;
		this.z = centerZ;
		this.height = height;
		this.fbos = new WaterFrameBuffers();
		instances.add(this);
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
	
	public static void cleanup() {
		if (!instances.isEmpty()) {
			for (Water water : instances) {
				water.fbos.cleanup();
			}
			instances.clear();
		}
	}
}
