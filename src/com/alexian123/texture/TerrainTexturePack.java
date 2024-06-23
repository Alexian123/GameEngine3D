package com.alexian123.texture;

import com.alexian123.util.gl.TextureSampler;

public class TerrainTexturePack {

	private final TextureSampler backgroundTexture;
	private final TextureSampler redTexture;
	private final TextureSampler greenTexture;
	private final TextureSampler blueTexture;
	
	private float shineDamper = 1;
	private float reflectivity = 0;
	
	public TerrainTexturePack(TextureSampler backgroundTexture, TextureSampler redTexture, TextureSampler greenTexture, TextureSampler blueTexture) {
		this.backgroundTexture = backgroundTexture;
		this.redTexture = redTexture;
		this.greenTexture = greenTexture;
		this.blueTexture = blueTexture;
	}

	public TextureSampler getBackgroundTexture() {
		return backgroundTexture;
	}

	public TextureSampler getRedTexture() {
		return redTexture;
	}

	public TextureSampler getGreenTexture() {
		return greenTexture;
	}

	public TextureSampler getBlueTexture() {
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
