package com.alexian123.terrain;

import java.util.Arrays;
import java.util.List;

import com.alexian123.engine.GameManager;

public class TerrainGrid {
	
	private final int dimension;
	private final Terrain[] grid;
	
	private int gridIdx = 0;
	
	public TerrainGrid(int dimension) {
		this.dimension = dimension;
		this.grid = new Terrain[dimension * dimension];
	}
	
	public int getDimension() {
		return dimension;
	}
	
	public List<Terrain> asList() {
		return Arrays.asList(grid);
	}
	
	public float getHeightAt(float worldX, float worldZ) {
		Terrain t = getTerrainAt(worldX, worldZ);
		return t != null ? t.getHeightAtPosition(worldX, worldZ) : 0;
	}
	
	public Terrain getTerrainAt(float worldX, float worldZ) {
		int gridX = (int) (worldX / GameManager.SETTINGS.terrainTileSize);
		int gridZ = (int) (worldZ / GameManager.SETTINGS.terrainTileSize);
		int index = gridX * dimension + gridZ;
		Terrain t = null;
		if (index >= 0 && index < dimension * dimension) {
			t = grid[index];
		}
		return t;
	}
	
	boolean addTerrain(Terrain terrain) {
		if (gridIdx < dimension * dimension) {
			grid[gridIdx++] = terrain;
			return true;
		}
		return false;
	}
	
	int[] getNextPosition() {
		return new int[] { gridIdx / dimension, gridIdx % dimension };
	}
}
