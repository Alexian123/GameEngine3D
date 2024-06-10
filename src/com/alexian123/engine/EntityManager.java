package com.alexian123.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Vector4f;

import com.alexian123.entity.Camera;
import com.alexian123.entity.Entity;
import com.alexian123.entity.Light;
import com.alexian123.model.TexturedModel;
import com.alexian123.rendering.EntityRenderer;
import com.alexian123.rendering.EntityRendererNM;
import com.alexian123.texture.ModelTexture;

public class EntityManager {
	
	private static Map<TexturedModel, List<Entity>> entities = new HashMap<>();
	private static Map<TexturedModel, List<Entity>> entitiesNM = new HashMap<>();
	
	private static EntityRenderer renderer = new EntityRenderer();
	private static EntityRendererNM rendererNM = new EntityRendererNM();
	
	public static void renderEntities(List<Entity> allEntities, List<Light> lights, Camera camera, Vector4f clipPlane) {
		for (Entity entity : allEntities) {
			processEntity(entity);
		}
		renderer.render(entities, lights, camera, clipPlane);
		rendererNM.render(entitiesNM, lights, camera, clipPlane);
		entities.clear();
		entitiesNM.clear();
	}
	
	public static void cleanup() {
		renderer.cleanup();
		rendererNM.cleanup();
		entities.clear();
		entitiesNM.clear();
	}

	private static void processEntity(Entity entity) {
		TexturedModel model = entity.getModel();
		ModelTexture texture = model.getTexture();
		Map<TexturedModel, List<Entity>> entitiesMap = texture.getNormalMap() != ModelTexture.NO_TEXTURE ? entitiesNM : entities;
		List<Entity> batch = entitiesMap.get(model);
		if (batch == null) {
			batch = new ArrayList<>();
			entitiesMap.put(model,  batch);
		}
		batch.add(entity);
	}
}
