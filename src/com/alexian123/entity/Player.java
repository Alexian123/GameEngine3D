package com.alexian123.entity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import com.alexian123.model.TexturedModel;
import com.alexian123.renderEngine.DisplayManager;

public class Player extends Entity {
	
	private static final float RUN_SPEED = 20.0f;	// units/s
	private static final float TURN_SPEED = 160.0f;	// degrees/s
	private static final float GRAVITY = -50.0f;
	private static final float JUMP_POWER = 20.0f;
	private static final float TERRAIN_HEIGHT = 0.0f;
	
	private float currentRunSpeed = 0;
	private float currentTurnSpeed = 0;
	private float currrentYSpeed = 0;
	
	private boolean isAirborne = false;

	public Player(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
		super(model, position, rotation, scale);
	}
	
	public void move() {
		getKeyboardInput();
		rotation.y += currentTurnSpeed * DisplayManager.getFrameTimeSeconds();
		float distance = currentRunSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(rotation.y)));
		float dz = (float) (distance * Math.cos(Math.toRadians(rotation.y)));
		position.x += dx;
		position.z += dz;
		currrentYSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
		position.y += currrentYSpeed * DisplayManager.getFrameTimeSeconds();
		if (position.y < TERRAIN_HEIGHT) {
			position.y = TERRAIN_HEIGHT;
			currrentYSpeed = 0;
			isAirborne = false;
		}
	}
	
	
	private void getKeyboardInput() {
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			currentRunSpeed = RUN_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			currentRunSpeed = -RUN_SPEED;
		} else {
			currentRunSpeed = 0;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			currentTurnSpeed = TURN_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			currentTurnSpeed = -TURN_SPEED;
		} else {
			currentTurnSpeed = 0;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			jump();
		}
	}
	
	private void jump() {
		if (!isAirborne) {
			currrentYSpeed = JUMP_POWER;
			isAirborne = true;
		}
	}

}
