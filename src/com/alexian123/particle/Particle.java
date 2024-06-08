package com.alexian123.particle;

import org.lwjgl.util.vector.Vector3f;

import com.alexian123.renderer.DisplayManager;
import com.alexian123.util.Constants;

public class Particle {
	
	private final Vector3f position;
	private final Vector3f velocity;
	
	private final float gravityComplient;
	private final float lifeLength;
	private final float rotation;
	private final float scale;
	
	private float elapsedTime = 0;

	public Particle(Vector3f position, Vector3f velocity, float gravityComplient, float lifeLength, float rotation,
			float scale) {
		this.position = position;
		this.velocity = velocity;
		this.gravityComplient = gravityComplient;
		this.lifeLength = lifeLength;
		this.rotation = rotation;
		this.scale = scale;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getRotation() {
		return rotation;
	}

	public float getScale() {
		return scale;
	}

	public boolean update() {
		velocity.y += Constants.GRAVITY * gravityComplient * DisplayManager.getFrameTimeSeconds();
		Vector3f change = new Vector3f(velocity);
		change.scale(DisplayManager.getFrameTimeSeconds());
		Vector3f.add(change, position, position);
		elapsedTime += DisplayManager.getFrameTimeSeconds();
		return elapsedTime < lifeLength;
	}
}
