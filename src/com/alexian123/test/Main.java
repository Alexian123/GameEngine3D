package com.alexian123.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import com.alexian123.entity.Camera;
import com.alexian123.entity.Entity;
import com.alexian123.entity.Light;
import com.alexian123.model.RawModel;
import com.alexian123.model.TexturedModel;
import com.alexian123.renderEngine.DisplayManager;
import com.alexian123.renderEngine.Loader;
import com.alexian123.renderEngine.OBJLoader;
import com.alexian123.renderEngine.RenderingManager;
import com.alexian123.terrain.Terrain;
import com.alexian123.texture.ModelTexture;

public class Main {

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		RenderingManager rendManager = new RenderingManager();
		
		RawModel rawModel;
		ModelTexture texture;
		TexturedModel texturedModel;
		Random random = new Random();
		List<Entity> entities = new ArrayList<>();
		
		// trees
		rawModel = OBJLoader.loadObjModel("tree", loader);
		texture = new ModelTexture(loader.loadTexture("tree"), 1, 0);
		texturedModel = new TexturedModel(rawModel, texture);
		for (int i = 0; i < 1000; ++i) {
			float x = random.nextFloat() * 1600 - 800; 
			float z = random.nextFloat() * 1600 - 800;
			entities.add(new Entity(texturedModel, new Vector3f(x, 0, z), new Vector3f(0, 0, 0), 10));
		}
		
		// grass
		rawModel = OBJLoader.loadObjModel("grassModel", loader);
		texture = new ModelTexture(loader.loadTexture("grassEntity"), 1, 0, true, true);
		texturedModel = new TexturedModel(rawModel, texture);
		for (int i = 0; i < 1000; ++i) {
			float x = random.nextFloat() * 1600 - 800; 
			float z = random.nextFloat() * 1600 - 800;
			entities.add(new Entity(texturedModel, new Vector3f(x, 0, z), new Vector3f(0, 0, 0), 5));
		}
		
		
		// ferns
		rawModel = OBJLoader.loadObjModel("fern", loader);
		texture = new ModelTexture(loader.loadTexture("fern"), 1, 0, true, true);
		texturedModel = new TexturedModel(rawModel, texture);
		for (int i = 0; i < 500; ++i) {
			float x = random.nextFloat() * 1600 - 800; 
			float z = random.nextFloat() * 1600 - 800;
			entities.add(new Entity(texturedModel, new Vector3f(x, 0, z), new Vector3f(0, 0, 0), 1));
		}
		
		
		// flowers
		rawModel = OBJLoader.loadObjModel("grassModel", loader);
		texture = new ModelTexture(loader.loadTexture("flower"), 1, 0, true, true);
		texturedModel = new TexturedModel(rawModel, texture);
		for (int i = 0; i < 250; ++i) {
			float x = random.nextFloat() * 1600 - 800; 
			float z = random.nextFloat() * 1600 - 800;
			entities.add(new Entity(texturedModel, new Vector3f(x, 0, z), new Vector3f(0, 0, 0), 5));
		}
		
		
		// terrain
		List<Terrain> terrains = new ArrayList<>();
		terrains.add(new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("grass2"))));
		terrains.add(new Terrain(-1, 0, loader, new ModelTexture(loader.loadTexture("grass2"))));
		terrains.add(new Terrain(0, -1, loader, new ModelTexture(loader.loadTexture("grass2"))));
		terrains.add(new Terrain(-1, -1, loader, new ModelTexture(loader.loadTexture("grass2"))));
		
		Light sun = new Light(new Vector3f(0, 100, -50), new Vector3f(1, 1, 1));
		
		Camera camera = new Camera();
		
		while (!DisplayManager.displayShouldClose()) {
			camera.move();
			
			for (Entity entity : entities) {
				rendManager.processEntity(entity);
			}
			
			for (Terrain terrain : terrains) {
				rendManager.processTerrain(terrain);
			}
			
			rendManager.renderScene(sun, camera);
			DisplayManager.updateDisplay();
		}
		
		rendManager.cleanup();
		loader.cleanup();
		DisplayManager.closeDisplay();
	}

}