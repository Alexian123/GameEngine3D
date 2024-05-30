package com.alexian123.terrain;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.alexian123.loader.Loader;
import com.alexian123.model.RawModel;
import com.alexian123.texture.TerrainTexturePack;
import com.alexian123.util.Maths;
import com.alexian123.texture.TerrainTexture;

public class Terrain {

	private static final float MAX_HEIGHT = 40.0f;
	private static final float MAX_PIXEL_COLOR = 256 * 256 * 256;
	
	protected static final float SIZE = 800.0f;
	
	private final float x;
	private final float z;
	private final RawModel model;
	private final TerrainTexturePack texturePack;
	private final TerrainTexture blendMap;
	
	private float heights[][];
	
	public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap, String heightmapFile) {
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.model = generateTerrain(loader, heightmapFile);
	}
	
	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	public RawModel getModel() {
		return model;
	}

	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}
	
	public TerrainTexture getBlendMap() {
		return blendMap;
	}
	
	public float getHeightAtPosition(float worldX, float worldZ) {
		float terrainX = worldX - this.x;
		float terrainZ = worldZ - this.z;
		float gridTileSize = SIZE / ((float) heights.length - 1);
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
	
	private RawModel generateTerrain(Loader loader, String heightmapFile){
		BufferedImage heightmap = null;
		try {
			heightmap = ImageIO.read(new File("res/textures/" + heightmapFile + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error reading heightmap: " + heightmapFile);
		}
		int vertexCount = heightmap.getHeight();
		heights = new float[vertexCount][vertexCount];
		int count = vertexCount * vertexCount;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (vertexCount - 1) * (vertexCount - 1)];
		int vertexPointer = 0;
		for (int i = 0; i < vertexCount; ++i) {
			for (int j = 0; j < vertexCount; ++j){
				vertices[vertexPointer * 3] = (float) j / ((float) vertexCount - 1) * SIZE;
				heights[j][i] = getHeight(j, i, heightmap);
				vertices[vertexPointer * 3 + 1] = heights[j][i];
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) vertexCount - 1) * SIZE;
				
				Vector3f normal = getNormal(j, i, heightmap);
				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
				
				textureCoords[vertexPointer * 2] = (float) j / ((float) vertexCount - 1);
				textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) vertexCount - 1);
				++vertexPointer;
			}
		}
		int pointer = 0;
		for (int gz = 0; gz < vertexCount - 1; ++gz) {
			for (int gx = 0; gx < vertexCount - 1; ++gx) {
				int topLeft = (gz * vertexCount) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * vertexCount) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadToVao(vertices, textureCoords, normals, indices);
	}
	
	private float getHeight(int x, int y, BufferedImage heightmap) {
		if (x < 0 || x >= heightmap.getHeight() || y < 0 || y >= heightmap.getHeight()) {
			return 0.0f;
		}
		float height = heightmap.getRGB(x, y);
		height += (MAX_PIXEL_COLOR / 2.0f);
		height /= (MAX_PIXEL_COLOR / 2.0f);
		height *= MAX_HEIGHT;
		return height;
	}
	
	private Vector3f getNormal(int x, int y, BufferedImage heightmap) {
		float heightL = getHeight(x - 1, y, heightmap);
		float heightR = getHeight(x + 1, y, heightmap);
		float heightU = getHeight(x, y + 1, heightmap);
		float heightD = getHeight(x, y - 1, heightmap);
		Vector3f normal = new Vector3f(heightL - heightR, 2.0f, heightD - heightU);
		normal.normalise();
		return normal;
	}
}
