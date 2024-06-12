package com.alexian123.loader;

public class ModelData {

	private final float[] vertices;
	private final float[] textureCoords;
	private final float[] normals;
	private final int[] indices;
	private final float furthestPoint;

	public ModelData(float[] vertices, float[] textureCoords, float[] normals, int[] indices,
			float furthestPoint) {
		this.vertices = vertices;
		this.textureCoords = textureCoords;
		this.normals = normals;
		this.indices = indices;
		this.furthestPoint = furthestPoint;
	}

	public float[] getVertices() {
		return vertices;
	}

	public float[] getTextureCoords() {
		return textureCoords;
	}

	public float[] getNormals() {
		return normals;
	}

	public int[] getIndices() {
		return indices;
	}

	public float getFurthestPoint() {
		return furthestPoint;
	}
	
	public float calculateHeight() {
		float min = Float.MAX_VALUE;
		float max = Float.MIN_VALUE;	
		for (int i = 1; i < vertices.length; i += 3) {
			min = Math.min(min, vertices[i]);
			max = Math.max(max, vertices[i]);
		}
		return max - min;
	}
}
