package com.alexian123.texture;

import com.alexian123.util.gl.TextureSampler;

public class ParticleTexture {
	
	private TextureSampler atlas;
	private int atlasDimension = 1;
	private boolean additiveBlending = false;
	
	public ParticleTexture(TextureSampler atlas) {
		this.atlas = atlas;
	}
	
	public ParticleTexture(TextureSampler atlas, int atlasDimension) {
		this.atlas = atlas;
		this.atlasDimension = atlasDimension;
	}
	
	public ParticleTexture(TextureSampler atlas, int atlasDimension, boolean additiveBlending) {
		this.atlas = atlas;
		this.atlasDimension = atlasDimension;
		this.additiveBlending = additiveBlending;
	}

	public TextureSampler getAtlas() {
		return atlas;
	}

	public int getAtlasDimension() {
		return atlasDimension;
	}

	public void setAtlasDimension(int atlasDimension) {
		this.atlasDimension = atlasDimension;
	}

	public boolean isAdditiveBlending() {
		return additiveBlending;
	}

	public void setAdditiveBlending(boolean additiveBlending) {
		this.additiveBlending = additiveBlending;
	}
}
