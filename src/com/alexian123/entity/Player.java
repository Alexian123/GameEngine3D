package com.alexian123.entity;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import com.alexian123.model.TexturedModel;
import com.alexian123.renderer.DisplayManager;
import com.alexian123.terrain.Terrain;
import com.alexian123.terrain.TerrainGrid;

public class Player extends Entity {
	
	private static final float RUN_SPEED = 20.0f;	// units/s
	private static final float TURN_SPEED = 160.0f;	// degrees/s
	private static final float GRAVITY = -50.0f;
	private static final float JUMP_POWER = 20.0f;
	
	private float currentRunSpeed = 0;
	private float currentTurnSpeed = 0;
	private float currrentYSpeed = 0;
	
	private boolean isAirborne = false;

	public Player(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
		super(model, position, rotation, scale);
	}
	
	public void move(Terrain terrain) {
		getKeyboardInput();
		rotation.y += currentTurnSpeed * DisplayManager.getFrameTimeSeconds();
		float distance = currentRunSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(rotation.y)));
		float dz = (float) (distance * Math.cos(Math.toRadians(rotation.y)));
		position.x += dx;
		position.z += dz;
		currrentYSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
		position.y += currrentYSpeed * DisplayManager.getFrameTimeSeconds();
		float terrainHeight = 0.0f;
		if (terrain != null) {
			terrainHeight = terrain.getHeightAtPosition(position.x, position.z);
		}
		if (position.y < terrainHeight) {
			position.y = terrainHeight;
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
