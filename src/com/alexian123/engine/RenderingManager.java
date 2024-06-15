package com.alexian123.engine;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector4f;

import com.alexian123.entity.Camera;
import com.alexian123.loader.Loader;
import com.alexian123.rendering.GUIRenderer;
import com.alexian123.rendering.SkyBoxRenderer;
import com.alexian123.rendering.TerrainRenderer;
import com.alexian123.rendering.WaterRenderer;
import com.alexian123.texture.GUITexture;
import com.alexian123.util.Clock;
import com.alexian123.util.Constants;
import com.alexian123.util.Scene;
import com.alexian123.water.Water;
import com.alexian123.water.WaterFrameBuffers;

public class RenderingManager {
	
	private static TerrainRenderer terrainRenderer;
	private static WaterRenderer waterRenderer;
	private static SkyBoxRenderer skyBoxRenderer;
	private static GUIRenderer guiRenderer;
	
	private static boolean isInitialized = false;
	
	public static void init(Loader loader, Clock clock, Camera camera) {
		if (!isInitialized) {
			enableCulling();
			ParticleManager.init(loader);
			TextManager.init(loader);
			ShadowManager.init(camera);
			EntityManager.init(ShadowManager.getShadowMap());
			terrainRenderer = new TerrainRenderer(ShadowManager.getShadowMap());
			waterRenderer = new WaterRenderer(loader);
			skyBoxRenderer = new SkyBoxRenderer(loader, clock);
			guiRenderer = new GUIRenderer(loader);
			isInitialized = true;
		}
	}
	
	public static void renderScene(Scene scene, Camera camera, List<GUITexture> guis) {
		ShadowManager.render(scene.getEntities(), scene.getLights().get(0));
		renderWaterFX(scene, camera);
		renderFrame(scene, camera, new Vector4f(0, -1, 0, 1000));
		waterRenderer.render(scene.getWaters(), camera, scene.getLights());
		ParticleManager.renderParticles(camera);
		guiRenderer.render(guis);
		TextManager.renderText();
	}
	
	public static void cleanup() {
		EntityManager.cleanup();
		ParticleManager.cleanup();
		TextManager.cleanup();
		Water.cleanup();
		ShadowManager.cleanup();
		terrainRenderer.cleanup();
		waterRenderer.cleanup();
		skyBoxRenderer.cleanup();
		guiRenderer.cleanup();
	}
	
	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	private static void renderWaterFX(Scene scene, Camera camera) {
		GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
		for (Water water : scene.getWaters()) {
			WaterFrameBuffers fbos = water.getFbos();
			float distance = 2f * (camera.getPosition().y - water.getHeight());
			camera.getPosition().y -= distance;
			camera.invertPitch();
			fbos.bindReflectionFrameBuffer();
			renderFrame(scene, camera, new Vector4f(0, 1, 0, -water.getHeight() + 1f));
			camera.getPosition().y += distance;
			camera.invertPitch();
			fbos.bindRefractionFrameBuffer();
			renderFrame(scene, camera, new Vector4f(0, -1, 0, water.getHeight() + 0.5f));
			fbos.unbindCurrentFrameBuffer(); 
		}
		GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
	}
	
	private static void renderFrame(Scene scene, Camera camera, Vector4f clipPlane) {
		prepare();
		EntityManager.renderEntities(scene.getEntities(), scene.getLights(), camera, clipPlane, ShadowManager.getToShadowMapSpaceMatrix());
		terrainRenderer.render(scene.getTerrains(), scene.getLights(), camera, clipPlane, ShadowManager.getToShadowMapSpaceMatrix());
		skyBoxRenderer.render(camera);
	}
	
	private static void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(Constants.FOG_COLOR.x, Constants.FOG_COLOR.y, Constants.FOG_COLOR.z, 1.0f);
	}
}
