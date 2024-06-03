package com.alexian123.terrain;

import java.util.Arrays;
import java.util.List;

import com.alexian123.loader.Loader;
import com.alexian123.texture.TerrainTexture;
import com.alexian123.texture.TerrainTexturePack;

public class TerrainGrid {
	
	private final int size;
	private final Terrain[] terrains;
	
	public TerrainGrid(int startX, int startZ, int size, Loader loader, 
			TerrainTexturePack[] texturePacks, TerrainTexture[] blendMaps, String[] heightmapFiles) 
					throws RuntimeException {
		if (size <= 0) {
			throw new RuntimeException("Gird size must be greater than 0");
		}
		if (texturePacks.length < size*size || blendMaps.length < size*size || heightmapFiles.length < size*size) {
			throw new RuntimeException("All arrays must have a size of at least " + size*size);
		}
		this.size = size;
		this.terrains = new Terrain[size * size];
		int terrainPtr = 0;
		for (int x = startX; x < (startX + size); ++x) {
			for (int z = startZ; z < (startZ + size); ++z) {
				terrains[terrainPtr] = new Terrain(x, z, loader, texturePacks[terrainPtr], blendMaps[terrainPtr], heightmapFiles[terrainPtr]);
				++terrainPtr;
			}
		}
	}
	
	public int getSize() {
		return size;
	}
	
	public List<Terrain> getTerrains() {
		return Arrays.asList(terrains);
	}
	
	public Terrain getTerrainAt(float worldX, float worldZ) {
		for (Terrain terrain : terrains) {
			
			if (worldX >= terrain.getX() && worldX < terrain.getX() + Terrain.SIZE
					&& worldZ >= terrain.getZ() && worldZ < terrain.getZ() + Terrain.SIZE) {
				return terrain;
			}
		}
		return null;
	}
}
