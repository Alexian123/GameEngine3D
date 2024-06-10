package com.alexian123.util;

import java.util.List;

import com.alexian123.entity.Entity;
import com.alexian123.entity.Light;
import com.alexian123.terrain.TerrainGrid;
import com.alexian123.terrain.Water;

public class Scene {

	private List<Entity> entities;
	private TerrainGrid terrainGrid;
	private List<Water> waters;
	private List<Light> lights;
	
	public Scene(List<Entity> entities, TerrainGrid terrainGrid, List<Water> waters, List<Light> lights) {
		this.entities = entities;
		this.terrainGrid = terrainGrid;
		this.waters = waters;
		this.lights = lights;
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}

	public TerrainGrid getTerrainGrid() {
		return terrainGrid;
	}

	public void setTerrainGrid(TerrainGrid terrainGrid) {
		this.terrainGrid = terrainGrid;
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
