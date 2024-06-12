package com.alexian123.entity;

import org.lwjgl.util.vector.Vector3f;

import com.alexian123.lighting.Light;
import com.alexian123.model.TexturedModel;

public class LightEntity extends Entity {
	
	private final Light light;

	/**
	 * Creates a new LightEntity object
	 * 
	 * @param model
	 * 			- textured model for entity
	 * @param position
	 * 			- entity's position
	 * @param rotation
	 * 			- entity's rotation
	 * @param scale
	 * 			- entity's scale
	 * @param lightYFactor
	 * 			- fractional number between 0.0 and 1.0; defines at what percentage of the entity's total height should the light be placed
	 * @param color
	 * 			- the color of the light
	 * @param attenuation
	 * 			- the attenuation of the light
	 */
	public LightEntity(TexturedModel model, Vector3f position, Vector3f rotation, float scale, 
			float lightYFactor, Vector3f color, Vector3f attenuation) {
		super(model, position, rotation, scale);
		lightYFactor = Math.min(lightYFactor, 1.0f);
		lightYFactor = Math.max(lightYFactor, 0.0f);
		Vector3f lightPoition = new Vector3f(position);
		lightPoition.y += lightYFactor * scale * model.getRawModel().getHeight();
		this.light = new Light(lightPoition, color, attenuation);
	}
	
	/**
	 * 
	 * @return The light object
	 */
	public Light getLight() {
		return light;
	}
}
