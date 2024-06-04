package com.alexian123.renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.alexian123.entity.Camera;
import com.alexian123.entity.Entity;
import com.alexian123.light.Light;
import com.alexian123.loader.Loader;
import com.alexian123.model.TexturedModel;
import com.alexian123.shader.EntityShader;
import com.alexian123.shader.TerrainShader;
import com.alexian123.terrain.Terrain;
import com.alexian123.terrain.Water;
import com.alexian123.terrain.WaterFrameBuffers;
import com.alexian123.texture.GUITexture;
import com.alexian123.util.Clock;

public class RenderingManager {
		
	private static final float FOV = 70.0f;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000.0f;
	
	private static final Vector3f SKY_COLOR = new Vector3f(0.5444f, 0.62f, 0.69f);
	
	private static final Matrix4f PROJECTION_MATRIX = createProjectionMatrix();
	
	private final EntityRenderer entityRenderer;
	private final TerrainRenderer terrainRenderer;
	private final WaterRenderer waterRenderer;
	private final SkyBoxRenderer skyBoxRenderer;
	private final GUIRenderer guiRenderer;
	
	private final Map<TexturedModel, List<Entity>> entities;
	
	public RenderingManager(Loader loader, Clock clock) {
		enableCulling();
		this.entityRenderer = new EntityRenderer(PROJECTION_MATRIX);
		this.terrainRenderer = new TerrainRenderer(PROJECTION_MATRIX);
		this.waterRenderer = new WaterRenderer(loader, PROJECTION_MATRIX);
		this.skyBoxRenderer = new SkyBoxRenderer(loader, PROJECTION_MATRIX, clock);
		this.guiRenderer = new GUIRenderer(loader);
		this.entities = new HashMap<>();
	}
	
	public void render(Scene scene, Camera camera, List<GUITexture> guis) {
		renderWaterFX(scene, camera);
		renderScene(scene, camera, new Vector4f(0, -1, 0, 1000));
		waterRenderer.render(scene.getWaters(), camera);
		guiRenderer.render(guis);
	}
	
	public void cleanup() {
		entityRenderer.cleanup();
		terrainRenderer.cleanup();
		waterRenderer.cleanup();
		skyBoxRenderer.cleanup();
		guiRenderer.cleanup();
	}
	
	private void renderWaterFX(Scene scene, Camera camera) {
		GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
		for (Water water : scene.getWaters()) {
			WaterFrameBuffers fbos = water.getFbos();
			float distance = 2f * (camera.getPosition().y - water.getHeight());
			camera.getPosition().y -= distance;
			camera.invertPitch();
			fbos.bindReflectionFrameBuffer();
			renderScene(scene, camera, new Vector4f(0, 1, 0, -water.getHeight()));
			camera.getPosition().y += distance;
			camera.invertPitch();
			fbos.bindRefractionFrameBuffer();
			renderScene(scene, camera, new Vector4f(0, -1, 0, water.getHeight()));
			fbos.unbindCurrentFrameBuffer();
		}
		GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
	}
	
	private void renderScene(Scene scene, Camera camera, Vector4f clipPlane) {
		prepare();
		for (Entity entity : scene.getEntities()) {
			processEntity(entity);
		}
		renderEntities(scene.getLights(), camera, clipPlane);
		renderTerrains(scene.getTerrains(), scene.getLights(), camera, clipPlane);
		skyBoxRenderer.render(camera, SKY_COLOR);
		entities.clear();
	}
	
	private void processEntity(Entity entity) {
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
	
	private void renderEntities(List<Light> lights, Camera camera, Vector4f clipPlane) {
		EntityShader shader = entityRenderer.getShader();
		shader.start();
		shader.loadClipPlane(clipPlane);
		shader.loadFogColor(SKY_COLOR);
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		entityRenderer.render(entities);
		shader.stop();
	}
	
	private void renderTerrains(List<Terrain> terrains, List<Light> lights, Camera camera, Vector4f clipPlane) {
		TerrainShader shader = terrainRenderer.getShader();
		shader.start();
		shader.loadClipPlane(clipPlane);
		shader.loadFogColor(SKY_COLOR);
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		shader.stop();
	}
	
	/* Static Methods */
	public static Matrix4f getProjectionMatrix() {
		return PROJECTION_MATRIX;
	}
	
	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
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
	/* -------------- */
}
