package com.alexian123.test;

import org.lwjgl.util.vector.Vector3f;

import com.alexian123.entities.Camera;
import com.alexian123.entities.Entity;
import com.alexian123.models.RawModel;
import com.alexian123.models.TexturedModel;
import com.alexian123.renderEngine.DisplayManager;
import com.alexian123.renderEngine.Loader;
import com.alexian123.renderEngine.OBJLoader;
import com.alexian123.renderEngine.Renderer;
import com.alexian123.shaders.StaticShader;
import com.alexian123.textures.ModelTexture;

public class Main {

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer(shader);
		
		
		
		RawModel model = OBJLoader.loadObjModel("stall", loader);
		ModelTexture texture = new ModelTexture(loader.loadTexture("stallTexture"));
		TexturedModel texturedModel = new TexturedModel(model, texture);
		Entity entity = new Entity(texturedModel, new Vector3f(0, -2, -5), new Vector3f(0, 0, 0), 1);
		
		Camera camera = new Camera();
		
		while (!DisplayManager.displayShouldClose()) {
			entity.incrementRotation(0, 0.5f, 0);
			camera.move();
			
			renderer.prepare();
			shader.start();
			shader.loadViewMatrix(camera);
			renderer.render(entity, shader);
			shader.stop();
			
			DisplayManager.updateDisplay();
		}
		
		shader.cleanup();
		loader.cleanup();
		DisplayManager.closeDisplay();
	}

}