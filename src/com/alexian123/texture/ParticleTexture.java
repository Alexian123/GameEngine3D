package com.alexian123.texture;

public class ParticleTexture {
	
	private int id;
	private int atlasDimension = 1;
	private boolean additiveBlending = false;
	
	public ParticleTexture(int id) {
		this.id = id;
	}
	
	public ParticleTexture(int id, int atlasDimension) {
		this.id = id;
		this.atlasDimension = atlasDimension;
	}
	
	public ParticleTexture(int id, int atlasDimension, boolean additiveBlending) {
		this.id = id;
		this.atlasDimension = atlasDimension;
		this.additiveBlending = additiveBlending;
	}

	public int getID() {
		return id;
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
