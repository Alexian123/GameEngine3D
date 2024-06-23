package com.alexian123.entity;

import org.lwjgl.util.vector.Vector3f;

import com.alexian123.model.TexturedModel;
import com.alexian123.texture.ModelTexture;
import com.alexian123.util.enums.EntityType;

public class Entity {
	
	protected final TexturedModel model;
	
	protected Vector3f position;
	protected Vector3f rotation;
	
	private float scale;
	private int textureIndex = 0;
	private boolean noShading = false;
	
	public Entity(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
		this.model = model;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}
	
	public Entity(TexturedModel model, int textureIndex, Vector3f position, Vector3f rotation, float scale) {
		this.model = model;
		this.textureIndex = textureIndex;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}
	
	public float getTextureXOffset() {
		ModelTexture texture = model.getTexture();
		int col = textureIndex % texture.getAtlasDimension();
		return (float) col / (float) texture.getAtlasDimension();
	}
	
	public float getTextureYOffset() {
		ModelTexture texture = model.getTexture();
		int row = textureIndex / texture.getAtlasDimension();
		return (float) row / (float) texture.getAtlasDimension();
	}
	
	public void incrementPosition(float x, float y, float z) {
		position.x += x;
		position.y += y;
		position.z += z;
	}
	
	public void incrementRotation(float x, float y, float z) {
		rotation.x += x;
		rotation.y += y;
		rotation.z += z;
	}

	public TexturedModel getModel() {
		return model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public float getScale() {
		return scale;
	}

	protected void setScale(float scale) {
		this.scale = scale;
	}

	public int getTextureIndex() {
		return textureIndex;
	}

	public void setTextureIndex(int textureIndex) {
		this.textureIndex = textureIndex;
	}

	public boolean isNoShading() {
		return noShading;
	}

	public void setNoShading(boolean noShading) {
		this.noShading = noShading;
	}
	
	public EntityType getType() {
		return EntityType.STATIC;
	}
}
