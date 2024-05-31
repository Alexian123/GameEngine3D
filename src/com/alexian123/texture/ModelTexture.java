package com.alexian123.texture;

public class ModelTexture {
	
	private final int id;
	
	private float shineDamper = 1;
	private float reflectivity = 0;
	private boolean transparency = false;
	private boolean fakeLighting = false;
	private int atlasDimension = 1;
	
	public ModelTexture(int id) {
		this.id = id;
	}

	public ModelTexture(int id, float shineDamper, float reflectivity) {
		this.id = id;
		this.shineDamper = shineDamper;
		this.reflectivity = reflectivity;
	}
	
	public ModelTexture(int id, boolean transparency, boolean fakeLighting) {
		this.id = id;
		this.transparency = transparency;
		this.fakeLighting = fakeLighting;
	}
	
	public ModelTexture(int id, float shineDamper, float reflectivity, boolean transparency, boolean fakeLighting) {
		this.id = id;
		this.shineDamper = shineDamper;
		this.reflectivity = reflectivity;
		this.transparency = transparency;
		this.fakeLighting = fakeLighting;
	}
	
	public ModelTexture(int id, float shineDamper, float reflectivity, boolean transparency, boolean fakeLighting, int atlasDimension) {
		this.id = id;
		this.shineDamper = shineDamper;
		this.reflectivity = reflectivity;
		this.transparency = transparency;
		this.fakeLighting = fakeLighting;
		this.atlasDimension = atlasDimension;
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

	public boolean isTransparency() {
		return transparency;
	}

	public void setTransparency(boolean transparency) {
		this.transparency = transparency;
	}

	public boolean isFakeLighting() {
		return fakeLighting;
	}

	public void setFakeLighting(boolean fakeLighting) {
		this.fakeLighting = fakeLighting;
	}

	public int getAtlasDimension() {
		return atlasDimension;
	}

	public void setAtlasDimension(int atlasDimension) {
		this.atlasDimension = atlasDimension;
	}
}
