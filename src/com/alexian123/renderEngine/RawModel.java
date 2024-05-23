package com.alexian123.renderEngine;

public class RawModel {
	
	private int vaoID;
	private int vertexCount;
	
	RawModel(int vaoID, int vertexCount) {;
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}

}
