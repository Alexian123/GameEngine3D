package com.alexian123.terrain;

import java.util.ArrayList;
import java.util.List;

import com.alexian123.renderer.Loader;
import com.alexian123.texture.TerrainTexture;
import com.alexian123.texture.TerrainTexturePack;

public class TerrainGrid {
	
	private final int size;
	private final List<Terrain> terrains;
	
	public TerrainGrid(int startX, int startZ, int size, Loader loader, 
			TerrainTexturePack[] texturePacks, TerrainTexture[] blendMaps, String[] heightmapFiles) 
					throws ArrayIndexOutOfBoundsException {
		if (texturePacks.length < size*size || blendMaps.length < size*size || heightmapFiles.length < size*size) {
			throw new ArrayIndexOutOfBoundsException("All arrays must have a size of at least " + size*size);
		}
		this.size = size;
		this.terrains = new ArrayList<>();
		int arraysPtr = 0;
		for (int x = startX; x < (startX + size); ++x) {
			for (int z = startZ; z < (startZ + size); ++z) {
				terrains.add(new Terrain(x, z, loader, texturePacks[arraysPtr], blendMaps[arraysPtr], heightmapFiles[arraysPtr]));
				++arraysPtr;
			}
		}
	}
	
	public int getSize() {
		return size;
	}
	
	public List<Terrain> getTerrains() {
		return terrains;
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
