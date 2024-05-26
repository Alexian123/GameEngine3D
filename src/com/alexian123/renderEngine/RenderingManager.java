package com.alexian123.renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alexian123.entities.Camera;
import com.alexian123.entities.Entity;
import com.alexian123.entities.Light;
import com.alexian123.models.TexturedModel;
import com.alexian123.shaders.StaticShader;

public class RenderingManager {
	
	private StaticShader shader = new StaticShader();
	private Renderer renderer = new Renderer(shader);
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<>();
	
	public void renderScene(Light sun, Camera camera) {
		renderer.prepare();
		shader.start();
		shader.loadLight(sun);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		entities.clear();
	}
	
	public void processEntity(Entity entity) {
		TexturedModel model = entity.getModel();
		List<Entity> batch = entities.get(model);
		if (batch == null) {
			List<Entity> newBatch = new ArrayList<>();
			newBatch.add(entity);
			entities.put(model,  newBatch);
		} else {
			batch.add(entity);
		}
	}
	
	public void cleanup() {
		shader.cleanup();
	}

}
