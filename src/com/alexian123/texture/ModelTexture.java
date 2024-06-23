package com.alexian123.texture;

import com.alexian123.util.gl.TextureSampler;

public class ModelTexture {
	
	private final TextureSampler colorTexture;
	private TextureSampler normalMap;
	private TextureSampler lightingMap;
	
	private int atlasDimension = 1;
	
	private float shineDamper = 1;
	private float reflectivity = 0;
	
	private boolean usingTransparency = false;
	private boolean usingFakeLighting = false;
	
	public ModelTexture(TextureSampler colorTexture) {
		this.colorTexture = colorTexture;
	}
	
	public ModelTexture(TextureSampler colorTexture, TextureSampler normalMap) {
		this.colorTexture = colorTexture;
		this.normalMap = normalMap;
	}

	public ModelTexture(TextureSampler colorTexture, float shineDamper, float reflectivity) {
		this.colorTexture = colorTexture;
		this.shineDamper = shineDamper;
		this.reflectivity = reflectivity;
	}
	public ModelTexture(TextureSampler colorTexture, TextureSampler normalMap, float shineDamper, float reflectivity) {
		this.colorTexture = colorTexture;
		this.normalMap = normalMap;
		this.shineDamper = shineDamper;
		this.reflectivity = reflectivity;
	}
	
	public ModelTexture(TextureSampler colorTexture, boolean transparency, boolean fakeLighting) {
		this.colorTexture = colorTexture;
		this.usingTransparency = transparency;
		this.usingFakeLighting = fakeLighting;
	}
	
	public ModelTexture(TextureSampler colorTexture, float shineDamper, float reflectivity, boolean transparency, boolean fakeLighting) {
		this.colorTexture = colorTexture;
		this.shineDamper = shineDamper;
		this.reflectivity = reflectivity;
		this.usingTransparency = transparency;
		this.usingFakeLighting = fakeLighting;
	}
	
	public ModelTexture(TextureSampler colorTexture, float shineDamper, float reflectivity, boolean transparency, boolean fakeLighting, int atlasDimension) {
		this.colorTexture = colorTexture;
		this.shineDamper = shineDamper;
		this.reflectivity = reflectivity;
		this.usingTransparency = transparency;
		this.usingFakeLighting = fakeLighting;
		this.atlasDimension = atlasDimension;
	}
	
	public TextureSampler getColorTexture() {
		return colorTexture;
	}

	public TextureSampler getNormalMap() {
		return normalMap;
	}
	
	public boolean hasNormalMap() {
		return normalMap != null;
	}

	public void setNormalMap(TextureSampler normalMap) {
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

	public boolean isUsingTransparency() {
		return usingTransparency;
	}

	public void setIsUsingTransparency(boolean usingTransparency) {
		this.usingTransparency = usingTransparency;
	}

	public boolean isUsingFakeLighting() {
		return usingFakeLighting;
	}

	public void setIsUsingFakeLighting(boolean usingFakeLighting) {
		this.usingFakeLighting = usingFakeLighting;
	}

	public int getAtlasDimension() {
		return atlasDimension;
	}

	public void setAtlasDimension(int atlasDimension) {
		this.atlasDimension = atlasDimension;
	}

	public TextureSampler getLightingMap() {
		return lightingMap;
	}

	public void setLightingMap(TextureSampler lightingMap) {
		this.lightingMap = lightingMap;
	}
	
	public boolean hasLightingMap() {
		return lightingMap != null;
	}
}
