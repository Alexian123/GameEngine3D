package com.alexian123.util;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

public class VertexNM extends Vertex {
	
	private List<Vector3f> tangents = new ArrayList<Vector3f>();
	private Vector3f averagedTangent = new Vector3f(0, 0, 0);
	
	public VertexNM(int index, Vector3f position) {
		super(index, position);
	}
	
	public void addTangent(Vector3f tangent) {
		tangents.add(tangent);
	}
	
	public VertexNM duplicate(int newIndex) {
		VertexNM vertex = new VertexNM(newIndex, position);
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