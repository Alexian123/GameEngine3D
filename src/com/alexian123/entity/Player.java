package com.alexian123.entity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import com.alexian123.animation.Animation;
import com.alexian123.engine.GameManager;
import com.alexian123.model.animated.AnimatedModel;
import com.alexian123.terrain.Terrain;

public class Player extends AnimatedEntity {
	
	private Animation animation;
	
	private float currentRunSpeed = 0;
	private float currentTurnSpeed = 0;
	private float currrentYSpeed = 0;
	
	private boolean isAirborne = false;

	public Player(AnimatedModel model, Vector3f position, Vector3f rotation, float scale, Animation animation) {
		super(model, position, rotation, scale);
		setAnimation(animation);
	}
	
	public void move(Terrain terrain) {
		getKeyboardInput();
		rotation.y += currentTurnSpeed * GameManager.getFrameTimeSeconds();
		float distance = currentRunSpeed * GameManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(rotation.y)));
		float dz = (float) (distance * Math.cos(Math.toRadians(rotation.y)));
		position.x += dx;
		position.z += dz;
		currrentYSpeed += GameManager.SETTINGS.gravity * GameManager.getFrameTimeSeconds();
		position.y += currrentYSpeed * GameManager.getFrameTimeSeconds();
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
	
	public void setAnimation(Animation animation) {
		this.animation = animation;
		model.doAnimation(animation);
	}
	
	public Animation getAnimation() {
		return animation;
	}

	private void getKeyboardInput() {
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			model.updateAnimation();
			currentRunSpeed = GameManager.SETTINGS.playerRunSpeed;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			model.updateAnimation();
			currentRunSpeed = -GameManager.SETTINGS.playerRunSpeed;
		} else {
			currentRunSpeed = 0;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			currentTurnSpeed = GameManager.SETTINGS.playerTurnSpeed;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			currentTurnSpeed = -GameManager.SETTINGS.playerTurnSpeed;
		} else {
			currentTurnSpeed = 0;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			jump();
		}
	}
	
	private void jump() {
		if (!isAirborne) {
			currrentYSpeed = GameManager.SETTINGS.playerJumpPower;
			isAirborne = true;
		}
	}

}
