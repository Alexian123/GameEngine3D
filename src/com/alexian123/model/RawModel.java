package com.alexian123.model;

public class RawModel {
	
	private final int vaoID;
	private final int vertexCount;
	private final float height;
	
	public RawModel(int vaoID, int vertexCount) {;
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
		this.height = 0;
	}
	
	public RawModel(int vaoID, int vertexCount, float height) {;
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
		this.height = height;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public float getHeight() {
		return height;
	}
}
