package com.alexian123.renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import com.alexian123.entity.Camera;
import com.alexian123.entity.Entity;
import com.alexian123.entity.Light;
import com.alexian123.model.TexturedModel;
import com.alexian123.shader.StaticShader;
import com.alexian123.shader.TerrainShader;
import com.alexian123.terrain.Terrain;

public class RenderingManager {
	
	private static final float FOV = 70.0f;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000.0f;
	
	private final Matrix4f projectionMatrix;
	
	private final StaticShader staticShader;
	private final EntityRenderer entityRenderer;
	
	private final TerrainShader terrainShader;
	private final TerrainRenderer terrainRenderer;
	
	private Map<TexturedModel, List<Entity>> entities;
	private List<Terrain> terrains;
	
	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	public RenderingManager() {
		enableCulling();
		this.projectionMatrix = createProjectionMatrix();
		this.staticShader = new StaticShader();
		this.entityRenderer = new EntityRenderer(staticShader, projectionMatrix);
		this.terrainShader = new TerrainShader();
		this.terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		this.entities = new HashMap<>();
		this.terrains = new ArrayList<>();
	}
	
	public void renderScene(Light sun, Camera camera) {
		prepare();
		renderEntities(sun, camera);
		renderTerrains(sun, camera);
		entities.clear();
		terrains.clear();
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
	
	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}
	
	public void cleanup() {
		staticShader.cleanup();
		terrainShader.cleanup();
	}
	
	private void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0, 194.0f / 255.0f, 1, 1);
	}
	
	private void renderEntities(Light sun, Camera camera) {
		staticShader.start();
		staticShader.loadLight(sun);
		staticShader.loadViewMatrix(camera);
		entityRenderer.render(entities);
		staticShader.stop();
	}
	
	private void renderTerrains(Light sun, Camera camera) {
		terrainShader.start();
		terrainShader.loadLight(sun);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
	}
	
	private static Matrix4f createProjectionMatrix() {
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float xScale = (float) (1.0f / Math.tan(Math.toRadians(FOV / 2.0f)));
		float yScale = xScale * aspectRatio;
		float frustumLength = FAR_PLANE - NEAR_PLANE;
		Matrix4f projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = xScale;
		projectionMatrix.m11 = yScale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustumLength);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustumLength);
		projectionMatrix.m33 = 0;
		return projectionMatrix;
	}

}
