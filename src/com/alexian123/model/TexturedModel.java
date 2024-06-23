package com.alexian123.model;

import com.alexian123.texture.ModelTexture;

public class TexturedModel {
	
	private ModelMesh mesh;
	private ModelTexture texture;

	public TexturedModel(ModelMesh mesh, ModelTexture texture) {
		this.mesh = mesh;
		this.texture = texture;
	}

	public ModelMesh getMesh() {
		return mesh;
	}

	public ModelTexture getTexture() {
		return texture;
	}
}
