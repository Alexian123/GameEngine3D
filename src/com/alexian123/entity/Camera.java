package com.alexian123.entity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private static final float CAMERA_STEP = 0.2f;
	
	private Vector3f position = new Vector3f(0, 2, 0);
	private float pitch; // high/low
	private float yaw; // left/right
	private float roll; // tilt
	
	public void move() {
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			position.z -= CAMERA_STEP;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			position.x -= CAMERA_STEP;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			position.z += CAMERA_STEP;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			position.x += CAMERA_STEP;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			position.y += CAMERA_STEP;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			position.y -= CAMERA_STEP;
		}
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public float getPitch() {
		return pitch;
	}
	
	public float getYaw() {
		return yaw;
	}
	
	public float getRoll() {
		return roll;
	}

}
