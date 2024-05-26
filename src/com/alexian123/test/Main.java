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
		
		RawModel rawModel = OBJLoader.loadObjModel("dragon", loader);
		ModelTexture texture = new ModelTexture(loader.loadTexture("dirt"), 10, 1);
		TexturedModel texturedModel = new TexturedModel(rawModel, texture);
		
		List<Entity> entities = new ArrayList<>();
		Entity e = new Entity(texturedModel, new Vector3f(0, 0, -25), new Vector3f(0, 0, 0), 1);
		entities.add(e);
		
		List<Terrain> terrains = new ArrayList<>();
		terrains.add(new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("grass"))));
		terrains.add(new Terrain(-1, 0, loader, new ModelTexture(loader.loadTexture("grass"))));
		terrains.add(new Terrain(0, -1, loader, new ModelTexture(loader.loadTexture("grass"))));
		terrains.add(new Terrain(-1, -1, loader, new ModelTexture(loader.loadTexture("grass"))));
		
		Light light = new Light(new Vector3f(0, 0, -20), new Vector3f(1, 1, 1));
		
		Camera camera = new Camera();
		
		while (!DisplayManager.displayShouldClose()) {
			camera.move();
			
			for (Entity entity : entities) {
				entity.incrementRotation(0, 0.5f, 0);
				rendManager.processEntity(entity);
			}
			
			for (Terrain terrain : terrains) {
				rendManager.processTerrain(terrain);
			}
			
			rendManager.renderScene(light, camera);
			DisplayManager.updateDisplay();
		}
		
		rendManager.cleanup();
		loader.cleanup();
		DisplayManager.closeDisplay();
	}

}