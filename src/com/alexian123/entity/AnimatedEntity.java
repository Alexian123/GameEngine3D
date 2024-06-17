package com.alexian123.entity;

import org.lwjgl.util.vector.Vector3f;

import com.alexian123.model.animated.AnimatedModel;

public class AnimatedEntity extends Entity {
	
	protected AnimatedModel animatedModel;

	public AnimatedEntity(AnimatedModel animatedModel, Vector3f position, Vector3f rotation, float scale) {
		super(animatedModel, position, rotation, scale);
		this.animatedModel = animatedModel;
	}
	
	public AnimatedModel getAnimatedModel() {
		return animatedModel;
	}
}
