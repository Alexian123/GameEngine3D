package com.alexian123.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import com.alexian123.entity.Camera;
import com.alexian123.entity.Entity;
import com.alexian123.lighting.Light;
import com.alexian123.model.TexturedModel;
import com.alexian123.rendering.EntityRenderer;
import com.alexian123.shader.EntityShaderNM;
import com.alexian123.texture.ModelTexture;

public class EntityManager {
	
	private static Map<TexturedModel, List<Entity>> entities = new HashMap<>();
	private static Map<TexturedModel, List<Entity>> entitiesNM = new HashMap<>();
	
	private static EntityRenderer renderer;
	private static EntityRenderer rendererNM;
	
	private static boolean isInitialized = false;
	
	public static void init(int shadowMapID) {
		if (!isInitialized) {
			renderer = new EntityRenderer(shadowMapID);
			rendererNM = new EntityRenderer(new EntityShaderNM(), shadowMapID);
			isInitialized = true;
		}
	}
	
	public static void renderEntities(List<Entity> allEntities, List<Light> lights, Camera camera, Vector4f clipPlane, Matrix4f toShadowMapSpace) {
		for (Entity entity : allEntities) {
			processEntity(entity);
		}
		renderer.render(entities, lights, camera, clipPlane, toShadowMapSpace);
		rendererNM.render(entitiesNM, lights, camera, clipPlane, toShadowMapSpace);
		entities.clear();
		entitiesNM.clear();
	}
	
	public static void cleanup() {
		renderer.cleanup();
		rendererNM.cleanup();
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
