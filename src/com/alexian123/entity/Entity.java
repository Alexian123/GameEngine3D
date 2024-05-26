package com.alexian123.entity;

import org.lwjgl.util.vector.Vector3f;

import com.alexian123.model.TexturedModel;

public class Entity {
	
	private TexturedModel model;
	private Vector3f position;
	private Vector3f rotation;
	private float scale;
	
	public Entity(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
		super();
		this.model = model;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
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

	public void setModel(TexturedModel model) {
		this.model = model;
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

	public void setScale(float scale) {
		this.scale = scale;
	}

}
