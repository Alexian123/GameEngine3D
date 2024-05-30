package com.alexian123.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.alexian123.entity.Camera;
import com.alexian123.entity.Entity;
import com.alexian123.entity.Light;
import com.alexian123.entity.Player;
import com.alexian123.loader.Loader;
import com.alexian123.loader.ModelData;
import com.alexian123.loader.OBJFileLoader;
import com.alexian123.model.RawModel;
import com.alexian123.model.TexturedModel;
import com.alexian123.renderer.DisplayManager;
import com.alexian123.renderer.GUIRenderer;
import com.alexian123.renderer.RenderingManager;
import com.alexian123.terrain.Terrain;
import com.alexian123.terrain.TerrainGrid;
import com.alexian123.texture.GUITexture;
import com.alexian123.texture.ModelTexture;
import com.alexian123.texture.TerrainTexturePack;
import com.alexian123.texture.TerrainTexture;

public class Main {

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		RenderingManager rendManager = new RenderingManager();
		
		// terrain
		TerrainTexture bgTexture = new TerrainTexture(loader.loadTexture("grass2"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("flowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		TerrainTexturePack texturePack = new TerrainTexturePack(bgTexture, rTexture, gTexture, bTexture);
		TerrainGrid terrainGrid = new TerrainGrid(-1, -1, 2, loader, 
				new TerrainTexturePack[] {texturePack, texturePack, texturePack, texturePack}, 
				new TerrainTexture[] {blendMap, blendMap, blendMap, blendMap}, 
				new String[] {"heightmap", "heightmap", "heightmap", "heightmap"});
		
		ModelData modelData;
		RawModel rawModel;
		ModelTexture texture;
		TexturedModel texturedModel;
		Random random = new Random();
		List<Entity> entities = new ArrayList<>();
		
		// trees
		modelData = OBJFileLoader.loadOBJ("tree");
		rawModel = loader.loadToVao(modelData.getVertices(), modelData.getTextureCoords(), modelData.getNormals(), modelData.getIndices());
		texture = new ModelTexture(loader.loadTexture("tree"), 1, 0);
		texturedModel = new TexturedModel(rawModel, texture);
		for (int i = 0; i < 1000; ++i) {
			float x = random.nextFloat() * 1600 - 800; 
			float z = random.nextFloat() * 1600 - 800;
			entities.add(new Entity(texturedModel, new Vector3f(x, terrainGrid.getTerrainAt(x, z).getHeightAtPosition(x, z) - 0.5f, z), new Vector3f(0, 0, 0), 10));
		}
		
		// ferns
		modelData = OBJFileLoader.loadOBJ("fern");
		rawModel = loader.loadToVao(modelData.getVertices(), modelData.getTextureCoords(), modelData.getNormals(), modelData.getIndices());
		texture = new ModelTexture(loader.loadTexture("fern_atlas"), 1, 0, true, true);
		texture.setAtlasDimension(2);
		texturedModel = new TexturedModel(rawModel, texture);
		for (int i = 0; i < 2000; ++i) {
			float x = random.nextFloat() * 1600 - 800; 
			float z = random.nextFloat() * 1600 - 800;
			entities.add(new Entity(texturedModel, random.nextInt(4), new Vector3f(x, terrainGrid.getTerrainAt(x, z).getHeightAtPosition(x, z), z), new Vector3f(0, 0, 0), 1));
		}
		
		// player
		modelData = OBJFileLoader.loadOBJ("person");
		rawModel = loader.loadToVao(modelData.getVertices(), modelData.getTextureCoords(), modelData.getNormals(), modelData.getIndices());
		texture = new ModelTexture(loader.loadTexture("playerTexture"));
		texturedModel = new TexturedModel(rawModel, texture);
		Player player = new Player(texturedModel, new Vector3f(100, 0, -50), new Vector3f(0, 180, 0), 0.6f);
		entities.add(player);
		
		Light sun = new Light(new Vector3f(0, 100, -50), new Vector3f(1, 1, 1));
		Camera camera = new Camera(player);
		
		// GUI
		GUIRenderer guiRenderer = new GUIRenderer(loader);
		List<GUITexture> guis = new ArrayList<>();
		GUITexture gui = new GUITexture(loader.loadTexture("health"), new Vector2f(-0.75f, -0.9f), new Vector2f(0.25f, 0.35f));
		guis.add(gui);
			
		while (!DisplayManager.displayShouldClose()) {
			camera.move();
			player.move(terrainGrid.getTerrainAt(player.getPosition().x, player.getPosition().z));
			
			for (Entity entity : entities) {
				rendManager.processEntity(entity);
			}
			
			for (Terrain terrain : terrainGrid.getTerrains()) {
				rendManager.processTerrain(terrain);
			}
			
			rendManager.renderScene(sun, camera);
			guiRenderer.render(guis);
			DisplayManager.updateDisplay();
		}
		
		guiRenderer.cleanup();
		rendManager.cleanup();
		loader.cleanup();
		DisplayManager.closeDisplay();
	}

}