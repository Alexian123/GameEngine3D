package com.alexian123.terrain;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.alexian123.engine.GameManager;
import com.alexian123.loader.Loader;
import com.alexian123.model.ModelMesh;
import com.alexian123.texture.TerrainTexturePack;
import com.alexian123.util.gl.TextureSampler;
import com.alexian123.util.mathematics.Maths;

public class Terrain {
	
	private static final int VERTEX_COUNT = 256;
	
	private final float x;
	private final float z;
	private final HeightGenerator generator;
	private final ModelMesh model;
	private final TerrainTexturePack texturePack;
	private final TextureSampler blendMap;
	
	private float heights[][];
	
	public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, TextureSampler blendMap) {
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX * GameManager.SETTINGS.terrainTileSize;
		this.z = gridZ * GameManager.SETTINGS.terrainTileSize;
		this.generator = new HeightGenerator(gridX, gridZ, VERTEX_COUNT, GameManager.SETTINGS.terrainSeed);
		this.model = generateTerrain(loader);
	}
	
	public Terrain(TerrainGrid grid, Loader loader, TerrainTexturePack texturePack, TextureSampler blendMap) {
		int[] gridPos = grid.getNextPosition();
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridPos[0] * GameManager.SETTINGS.terrainTileSize;
		this.z = gridPos[1] * GameManager.SETTINGS.terrainTileSize;
		this.generator = new HeightGenerator(gridPos[0], gridPos[1], VERTEX_COUNT, GameManager.SETTINGS.terrainSeed);
		this.model = generateTerrain(loader);
		grid.addTerrain(this);
	}
	
	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	public ModelMesh getModel() {
		return model;
	}

	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}
	
	public TextureSampler getBlendMap() {
		return blendMap;
	}

	public float getHeightAtPosition(float worldX, float worldZ) {
		float terrainX = worldX - this.x;
		float terrainZ = worldZ - this.z;
		float gridTileSize = GameManager.SETTINGS.terrainTileSize / ((float) heights.length - 1);
		int gridX = (int) Math.floor(terrainX / gridTileSize);
		int gridZ = (int) Math.floor(terrainZ / gridTileSize);
		if (gridX < 0 || gridX >= heights.length - 1 || gridZ < 0 || gridZ >= heights.length - 1) { // check if tile is invalid
			return 0.0f;
		}
		float xCoord = (terrainX % gridTileSize) / gridTileSize;
		float zCoord = (terrainZ % gridTileSize) / gridTileSize;
		float height = 0.0f;
		if (xCoord <= (1-zCoord)) { // check which triangle the position is in
			height = Maths.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), 
										new Vector3f(1,heights[gridX + 1][gridZ], 0), 
										new Vector3f(0, heights[gridX][gridZ + 1], 1), 
										new Vector2f(xCoord, zCoord));
		} else {
			height = Maths.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0),
										new Vector3f(1,heights[gridX + 1][gridZ + 1], 1), 
										new Vector3f(0,heights[gridX][gridZ + 1], 1), 
										new Vector2f(xCoord, zCoord));
		}
		return height;
	}
	
	private ModelMesh generateTerrain(Loader loader) {
		heights = new float[VERTEX_COUNT][VERTEX_COUNT];
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
		int vertexPointer = 0;
		for (int i = 0; i < VERTEX_COUNT; ++i) {
			for (int j = 0; j < VERTEX_COUNT; ++j){
				vertices[vertexPointer * 3] = (float) j / ((float) VERTEX_COUNT - 1) * GameManager.SETTINGS.terrainTileSize;
				heights[j][i] = getHeight(j, i, generator);
				vertices[vertexPointer * 3 + 1] = heights[j][i];
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * GameManager.SETTINGS.terrainTileSize;
				
				Vector3f normal = getNormal(j, i, generator);
				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
				
				textureCoords[vertexPointer * 2] = (float) j / ((float) VERTEX_COUNT - 1);
				textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) VERTEX_COUNT - 1);
				++vertexPointer;
			}
		}
		int pointer = 0;
		for (int gz = 0; gz < VERTEX_COUNT - 1; ++gz) {
			for (int gx = 0; gx < VERTEX_COUNT - 1; ++gx) {
				int topLeft = (gz * VERTEX_COUNT) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadToVao(vertices, textureCoords, normals, indices, 0);
	}
	
	private Vector3f getNormal(int x, int z, HeightGenerator generator) {
		float heightL = getHeight(x - 1, z, generator);
		float heightR = getHeight(x + 1, z, generator);
		float heightU = getHeight(x, z + 1, generator);
		float heightD = getHeight(x, z - 1, generator);
		Vector3f normal = new Vector3f(heightL - heightR, 2.0f, heightD - heightU);
		normal.normalise();
		return normal;
	}
	
	private float getHeight(int x, int z, HeightGenerator generator) {
		return generator.generateHeight(x, z);
	}
}
