package com.alexian123.texture;

public class ModelTexture {
	
	private int id;
	
	private float shineDamper = 1;
	private float reflectivity = 0;
	
	public ModelTexture(int textureID) {
		this.id = textureID;
	}

	public ModelTexture(int id, float shineDamper, float reflectivity) {
		this.id = id;
		this.shineDamper = shineDamper;
		this.reflectivity = reflectivity;
	}

	public int getID() {
		return id;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

}
