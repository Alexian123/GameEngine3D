package com.alexian123.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import com.alexian123.entity.Entity;
import com.alexian123.game.Camera;
import com.alexian123.lighting.Light;
import com.alexian123.model.TexturedModel;
import com.alexian123.rendering.EntityRenderer;
import com.alexian123.util.gl.TextureSampler;

public class EntityManager {
	
	private static Map<TexturedModel, List<Entity>> entities = new HashMap<>();
	
	private static EntityRenderer renderer;
	
	private static boolean isInitialized = false;
	
	public static void init(TextureSampler textureSampler) {
		if (!isInitialized) {
			renderer = new EntityRenderer(textureSampler);
			isInitialized = true;
		}
	}
	
	public static void renderEntities(List<Entity> allEntities, List<Light> lights, Camera camera, Vector4f clipPlane, Matrix4f toShadowMapSpace) {
		for (Entity entity : allEntities) {
			processEntity(entity);
		}
		renderer.render(entities, lights, camera, clipPlane, toShadowMapSpace);
		entities.clear();
	}
	
	public static void cleanup() {
		renderer.cleanup();
	}

	private static void processEntity(Entity entity) {
		TexturedModel model = entity.getModel();
		List<Entity> batch = entities.get(model);
		if (batch == null) {
			batch = new ArrayList<>();
			entities.put(model,  batch);
		}
		batch.add(entity);
	}
}
