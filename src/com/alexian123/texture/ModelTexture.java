package com.alexian123.texture;

public class ModelTexture {
	
	public static final int NO_TEXTURE = -1;
	
	private final int id;
	
	private int normalMap = NO_TEXTURE;
	private int lightingMap = NO_TEXTURE;
	
	private int atlasDimension = 1;
	
	private float shineDamper = 1;
	private float reflectivity = 0;
	
	private boolean usingTransparency = false;
	private boolean usingFakeLighting = false;
	
	public ModelTexture(int id) {
		this.id = id;
	}
	
	public ModelTexture(int id, int normalMap) {
		this.id = id;
		this.normalMap = normalMap;
	}

	public ModelTexture(int id, float shineDamper, float reflectivity) {
		this.id = id;
		this.shineDamper = shineDamper;
		this.reflectivity = reflectivity;
	}
	public ModelTexture(int id, int normalMap, float shineDamper, float reflectivity) {
		this.id = id;
		this.normalMap = normalMap;
		this.shineDamper = shineDamper;
		this.reflectivity = reflectivity;
	}
	
	public ModelTexture(int id, boolean transparency, boolean fakeLighting) {
		this.id = id;
		this.usingTransparency = transparency;
		this.usingFakeLighting = fakeLighting;
	}
	
	public ModelTexture(int id, float shineDamper, float reflectivity, boolean transparency, boolean fakeLighting) {
		this.id = id;
		this.shineDamper = shineDamper;
		this.reflectivity = reflectivity;
		this.usingTransparency = transparency;
		this.usingFakeLighting = fakeLighting;
	}
	
	public ModelTexture(int id, float shineDamper, float reflectivity, boolean transparency, boolean fakeLighting, int atlasDimension) {
		this.id = id;
		this.shineDamper = shineDamper;
		this.reflectivity = reflectivity;
		this.usingTransparency = transparency;
		this.usingFakeLighting = fakeLighting;
		this.atlasDimension = atlasDimension;
	}
	
	public int getID() {
		return id;
	}

	public int getNormalMap() {
		return normalMap;
	}

	public void setNormalMap(int normalMap) {
		this.normalMap = normalMap;
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
		return usingTransparency;
	}

	public void setTransparency(boolean transparency) {
		this.usingTransparency = transparency;
	}

	public boolean isFakeLighting() {
		return usingFakeLighting;
	}

	public void setFakeLighting(boolean fakeLighting) {
		this.usingFakeLighting = fakeLighting;
	}

	public int getAtlasDimension() {
		return atlasDimension;
	}

	public void setAtlasDimension(int atlasDimension) {
		this.atlasDimension = atlasDimension;
	}

	public int getLightingMap() {
		return lightingMap;
	}

	public void setLightingMap(int lightingMap) {
		this.lightingMap = lightingMap;
	}
	
	public boolean isUsingLightingMap() {
		return lightingMap != NO_TEXTURE;
	}
}
