package com.alexian123.texture;

public class TerrainTexturePack {

	private final TerrainTexture backgroundTexture;
	private final TerrainTexture redTexture;
	private final TerrainTexture greenTexture;
	private final TerrainTexture blueTexture;
	
	private float shineDamper = 1;
	private float reflectivity = 0;
	
	public TerrainTexturePack(TerrainTexture backgroundTexture, TerrainTexture redTexture, TerrainTexture greenTexture, TerrainTexture blueTexture) {
		this.backgroundTexture = backgroundTexture;
		this.redTexture = redTexture;
		this.greenTexture = greenTexture;
		this.blueTexture = blueTexture;
	}

	public TerrainTexture getBackgroundTexture() {
		return backgroundTexture;
	}

	public TerrainTexture getRedTexture() {
		return redTexture;
	}

	public TerrainTexture getGreenTexture() {
		return greenTexture;
	}

	public TerrainTexture getBlueTexture() {
		return blueTexture;
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
