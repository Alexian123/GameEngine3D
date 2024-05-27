package com.alexian123.texture;

public class TerrainTexturePack {

	private final Texture backgroundTexture;
	private final Texture redTexture;
	private final Texture greenTexture;
	private final Texture blueTexture;
	
	public TerrainTexturePack(Texture backgroundTexture, Texture redTexture, Texture greenTexture, Texture blueTexture) {
		this.backgroundTexture = backgroundTexture;
		this.redTexture = redTexture;
		this.greenTexture = greenTexture;
		this.blueTexture = blueTexture;
	}

	public Texture getBackgroundTexture() {
		return backgroundTexture;
	}

	public Texture getRedTexture() {
		return redTexture;
	}

	public Texture getGreenTexture() {
		return greenTexture;
	}

	public Texture getBlueTexture() {
		return blueTexture;
	}
}
