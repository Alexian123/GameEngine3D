package com.alexian123.entity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private static final float CAMERA_STEP = 0.5f;
	
	private Vector3f position;
	private float pitch; // high/low
	private float yaw; // left/right
	private float roll; // tilt
	
	public Camera() {
		this.position = new Vector3f(0, 0, 0);
		this.pitch = 0;
		this.yaw = 0;
		this.roll = 0;
	}
	
	public Camera(Vector3f position, float pitch, float yaw, float roll) {
		this.position = position;
		this.pitch = pitch;
		this.yaw = yaw;
		this.roll = roll;
	}
	
	public void move() {
		
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
