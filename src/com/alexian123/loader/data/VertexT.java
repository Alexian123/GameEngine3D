package com.alexian123.loader.data;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

public class VertexT extends Vertex {
	
	private List<Vector3f> tangents = new ArrayList<Vector3f>();
	private Vector3f averagedTangent = new Vector3f(0, 0, 0);
	
	private final VertexSkinData weightsData;
	
	public VertexT(int index, Vector3f position) {
		super(index, position);
		this.weightsData = null;
	}
	
	public VertexT(int index, Vector3f position, VertexSkinData weightsData) {
		super(index, position);
		this.weightsData = weightsData;
	}
	
	public VertexSkinData getWeightsData() {
		return weightsData;
	}
	
	public void addTangent(Vector3f tangent) {
		tangents.add(tangent);
	}
	
	public VertexT duplicate(int newIndex) {
		VertexT vertex = new VertexT(newIndex, position);
		vertex.tangents = this.tangents;
		return vertex;
	}
	
	public void averageTangents() {
		if (tangents.isEmpty()){
			return;
		}
		for (Vector3f tangent : tangents) {
			Vector3f.add(averagedTangent, tangent, averagedTangent);
		}
		averagedTangent.normalise();
	}
	
	public Vector3f getAverageTangent() {
		return averagedTangent;
	}
}