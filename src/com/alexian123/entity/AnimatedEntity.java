package com.alexian123.entity;

import org.lwjgl.util.vector.Vector3f;

import com.alexian123.model.animated.AnimatedModel;
import com.alexian123.util.enums.EntityType;

public class AnimatedEntity extends Entity {
	
	protected final AnimatedModel model;
	
	public AnimatedEntity(AnimatedModel model, Vector3f position, Vector3f rotation, float scale) {
		super(model, position, rotation, scale);
		this.model = model;
	}

	@Override
	public AnimatedModel getModel() {
		return model;
	}
	
	@Override
	public EntityType getType() {
		return EntityType.ANIMATED;
	}
}
