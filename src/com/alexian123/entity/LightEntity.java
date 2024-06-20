package com.alexian123.entity;

import org.lwjgl.util.vector.Vector3f;

import com.alexian123.lighting.Light;
import com.alexian123.model.RawModel;
import com.alexian123.model.TexturedModel;
import com.alexian123.util.mathematics.Maths;

public class LightEntity extends Entity {
	
	private final Light light;
	
	private float lightYFactor;

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
		this.lightYFactor = Maths.clamp(lightYFactor, 0.0f, 1.0f);
		this.light = new Light(getUpdatedLightPosition(position), color, attenuation);
	}
	
	/**
	 * 
	 * @return The light object
	 */
	public Light getLight() {
		return light;
	}
	
	/**
	 * 
	 * @return The light Y factor
	 */
	public float getLightYFactor() {
		return lightYFactor;
	}

	/**
	 * Sets the Y factor for the light
	 * 
	 * @param lightYFactor
	 */
	public void setLightYFactor(float lightYFactor) {
		this.lightYFactor = Maths.clamp(lightYFactor, 0.0f, 1.0f);
	}

	@Override
	public void incrementPosition(float x, float y, float z) {
		super.incrementPosition(x, y, z);
		light.setPosition(getUpdatedLightPosition(position));
	}
	
	@Override
	public void setPosition(Vector3f position) {
		super.setPosition(position);
		light.setPosition(getUpdatedLightPosition(position));
	}
	
	private Vector3f getUpdatedLightPosition(Vector3f entityPosition) {
		Vector3f lightPoition = new Vector3f(entityPosition);
		RawModel rawModel = texturedModel.getRawModel();
		lightPoition.y += this.lightYFactor * getScale() * rawModel.getHeight();
		return lightPoition;
	}
}
