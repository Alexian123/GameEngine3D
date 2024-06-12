package com.alexian123.util;

import java.util.List;

import com.alexian123.entity.Entity;
import com.alexian123.lighting.Light;
import com.alexian123.terrain.Terrain;
import com.alexian123.water.Water;

public class Scene {

	private List<Entity> entities;
	private List<Terrain> terrains;
	private List<Water> waters;
	private List<Light> lights;
	
	public Scene(List<Entity> entities, List<Terrain> terrains, List<Water> waters, List<Light> lights) {
		this.entities = entities;
		this.terrains = terrains;
		this.waters = waters;
		this.lights = lights;
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}

	public List<Terrain> getTerrains() {
		return terrains;
	}

	public void setTerrains(List<Terrain> terrains) {
		this.terrains = terrains;
	}
	
	public List<Water> getWaters() {
		return waters;
	}

	public void setWaters(List<Water> waters) {
		this.waters = waters;
	}

	public List<Light> getLights() {
		return lights;
	}

	public void setLights(List<Light> lights) {
		this.lights = lights;
	}
}
