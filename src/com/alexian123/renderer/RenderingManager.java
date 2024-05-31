package com.alexian123.renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.alexian123.entity.Camera;
import com.alexian123.entity.Entity;
import com.alexian123.light.Light;
import com.alexian123.loader.Loader;
import com.alexian123.model.TexturedModel;
import com.alexian123.shader.IShader3D;
import com.alexian123.shader.StaticShader;
import com.alexian123.shader.TerrainShader;
import com.alexian123.terrain.Terrain;
import com.alexian123.texture.GUITexture;

public class RenderingManager {
		
	private static final float FOV = 70.0f;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000.0f;
	
	private static final Vector3f SKY_COLOR = new Vector3f(0.5f, 0.5f, 0.5f);
	
	private static final Matrix4f projectionMatrix = createProjectionMatrix();
	
	private static final List<IRenderer3D> renderers3D = new ArrayList<>();
	
	private static final Map<TexturedModel, List<Entity>> entities = new HashMap<>();
	private static final List<Terrain> terrains = new ArrayList<>();
	
	private static GUIRenderer guiRenderer;
	
	public static void init(Loader loader) {
		if (!renderers3D.isEmpty()) {
			renderers3D.clear();
		}
		renderers3D.add(new EntityRenderer(projectionMatrix));
		renderers3D.add(new TerrainRenderer(projectionMatrix));
		if (guiRenderer != null) {
			guiRenderer.cleanup();
		}
		guiRenderer = new GUIRenderer(loader);
		enableCulling();
	}
	
	public static void renderScene(List<Light> lights, Camera camera, List<GUITexture> guis) {
		prepare();
		for (IRenderer3D renderer : renderers3D) {
			IShader3D shader = renderer.getShader3D();
			shader.start();
			shader.loadSkyColor(RenderingManager.SKY_COLOR);
			shader.loadLights(lights);
			shader.loadViewMatrix(camera);
			renderer.render();
			shader.stop();
		}
		guiRenderer.render(guis);
		entities.clear();
		terrains.clear();
	}
	
	public static void processEntity(Entity entity) {
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
	
	public static void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}
	
	public static void cleanup() {
		for (IRenderer3D renderer3D : renderers3D) {
			renderer3D.cleanup();
		}
		guiRenderer.cleanup();
	}
	
	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	static Map<TexturedModel, List<Entity>> getEntities() {
		return entities;
	}
	
	static List<Terrain> getTerrains() {
		return terrains;
	}
	
	private static void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(SKY_COLOR.x, SKY_COLOR.y, SKY_COLOR.z, 1.0f);
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
