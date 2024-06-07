package com.alexian123.model;

public class ModelDataNM extends ModelData {

	private final float[] tangents;

	public ModelDataNM(float[] vertices, float[] textureCoords, float[] normals, float[] tangents, 
			int[] indices, float furthestPoint) {
		super(vertices, textureCoords, normals, indices, furthestPoint);
		this.tangents = tangents;
	}

	public float[] getTangents(){
		return tangents;
	}
}