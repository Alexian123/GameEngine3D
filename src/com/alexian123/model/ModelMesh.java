package com.alexian123.model;

import com.alexian123.util.gl.Vao;

public class ModelMesh {
	
	private final Vao vao;
	private final float height;
	
	public ModelMesh(Vao vao) {;
		this.vao = vao;
		this.height = 0;
	}
	
	public ModelMesh(Vao vao, float height) {
		this.vao = vao;
		this.height = height;
	}
	
	public int getVertexCount() {
		return vao.getIndexCount();
	}

	public float getHeight() {
		return height;
	}
	
	public Vao getVao() {
		return vao;
	}
}
