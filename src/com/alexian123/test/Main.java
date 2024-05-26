package com.alexian123.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import com.alexian123.entities.Camera;
import com.alexian123.entities.Entity;
import com.alexian123.entities.Light;
import com.alexian123.models.RawModel;
import com.alexian123.models.TexturedModel;
import com.alexian123.renderEngine.DisplayManager;
import com.alexian123.renderEngine.Loader;
import com.alexian123.renderEngine.OBJLoader;
import com.alexian123.renderEngine.Renderer;
import com.alexian123.renderEngine.RenderingManager;
import com.alexian123.shaders.StaticShader;
import com.alexian123.textures.ModelTexture;

public class Main {

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		RenderingManager rendManager = new RenderingManager();
		
		RawModel rawModel = OBJLoader.loadObjModel("dragon", loader);
		ModelTexture texture = new ModelTexture(loader.loadTexture("red"), 10, 1);
		TexturedModel texturedModel = new TexturedModel(rawModel, texture);
		
		//Entity entity = new Entity(texturedModel, new Vector3f(0, -3, -25), new Vector3f(0, 0, 0), 1);
		
		List<Entity> entities = new ArrayList<>();
		Random random = new Random();
		for (int i = 0; i < 100; ++i) {
			float x = random.nextFloat() * 100 - 50;
			float y = random.nextFloat() * 100 - 50;
			float z = random.nextFloat() * -300;
			entities.add(new Entity(texturedModel, new Vector3f(x, y, z), new Vector3f(0, 0, 0), 0.5f));
 		}
		
		
		
		Light light = new Light(new Vector3f(0, 0, -20), new Vector3f(1, 1, 1));
		
		Camera camera = new Camera();
		
		while (!DisplayManager.displayShouldClose()) {
			camera.move();
			for (Entity entity : entities) {
				entity.incrementRotation(0.5f, 0.5f, 0);
				rendManager.processEntity(entity);
			}
			rendManager.renderScene(light, camera);
			DisplayManager.updateDisplay();
		}
		
		rendManager.cleanup();
		loader.cleanup();
		DisplayManager.closeDisplay();
	}

}