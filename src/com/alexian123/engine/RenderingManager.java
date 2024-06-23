package com.alexian123.engine;

import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector4f;

import com.alexian123.game.Camera;
import com.alexian123.game.Clock;
import com.alexian123.game.Scene;
import com.alexian123.loader.Loader;
import com.alexian123.rendering.GUIRenderer;
import com.alexian123.rendering.SkyBoxRenderer;
import com.alexian123.rendering.TerrainRenderer;
import com.alexian123.rendering.WaterRenderer;
import com.alexian123.texture.GUITexture;
import com.alexian123.util.Constants;
import com.alexian123.util.enums.DepthBufferType;
import com.alexian123.util.gl.Fbo;
import com.alexian123.util.gl.GLControl;
import com.alexian123.water.Water;
import com.alexian123.water.WaterFrameBuffers;

public class RenderingManager {
	
	private static final int NUM_FBOS = 3;
	private static final int MULTISAMPLE_FBO = 0;
	private static final int OUTPUT_FBO_1 = 1;
	private static final int OUTPUT_FBO_2 = 2;
	
	private static final Fbo[] FBO = new Fbo[NUM_FBOS];
	
	private static TerrainRenderer terrainRenderer;
	private static WaterRenderer waterRenderer;
	private static SkyBoxRenderer skyBoxRenderer;
	private static GUIRenderer guiRenderer;
	
	private static boolean isInitialized = false;
	
	public static void init(Loader loader, Clock clock, Camera camera) {
		if (!isInitialized) {
			GLControl.enableCulling();
			ParticleManager.init(loader);
			TextManager.init(loader);
			ShadowManager.init(camera);
			EntityManager.init(ShadowManager.getShadowMap());
			PostProcessingManager.init(loader);
			FBO[MULTISAMPLE_FBO] = new Fbo(Display.getWidth(), Display.getHeight(), false, true);
			FBO[OUTPUT_FBO_1] = new Fbo(Display.getWidth(), Display.getHeight(), DepthBufferType.DEPTH_TEXTURE, false);
			FBO[OUTPUT_FBO_2] = new Fbo(Display.getWidth(), Display.getHeight(), DepthBufferType.DEPTH_TEXTURE, false);
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
		
		FBO[MULTISAMPLE_FBO].bindFrameBuffer();
		renderFrame(scene, camera, new Vector4f(0, -1, 0, 1000));
		waterRenderer.render(scene.getWaters(), camera, scene.getLights());
		ParticleManager.renderParticles(camera);
		FBO[MULTISAMPLE_FBO].unbindFrameBuffer();
		FBO[MULTISAMPLE_FBO].resolveAttachmentToFbo(0, FBO[OUTPUT_FBO_1]);
		FBO[MULTISAMPLE_FBO].resolveAttachmentToFbo(1, FBO[OUTPUT_FBO_2]);
		PostProcessingManager.doPostProcessing(FBO[OUTPUT_FBO_1].getColorTexture(), FBO[OUTPUT_FBO_2].getColorTexture());
		
		guiRenderer.render(guis);
		TextManager.renderText();
	}
	
	public static void cleanup() {
		EntityManager.cleanup();
		ParticleManager.cleanup();
		TextManager.cleanup();
		ShadowManager.cleanup();
		PostProcessingManager.cleanup();
		Fbo.cleanup();
		terrainRenderer.cleanup();
		waterRenderer.cleanup();
		skyBoxRenderer.cleanup();
		guiRenderer.cleanup();
	}
	
	private static void renderWaterFX(Scene scene, Camera camera) {
		GLControl.enableClipDistance(0);
		for (Water water : scene.getWaters()) {
			WaterFrameBuffers fbos = water.getFbos();
			float distance = 2f * (camera.getPosition().y - water.getHeight());
			camera.getPosition().y -= distance;
			camera.invertPitch();
			camera.update();
			fbos.bindReflectionFrameBuffer();
			renderFrame(scene, camera, new Vector4f(0, 1, 0, -water.getHeight() + 1f));
			camera.getPosition().y += distance;
			camera.invertPitch();
			camera.update();
			fbos.bindRefractionFrameBuffer();
			renderFrame(scene, camera, new Vector4f(0, -1, 0, water.getHeight() + 0.5f));
			fbos.unbindCurrentFrameBuffer(); 
		}
		GLControl.disableClipDistace(0);
	}
	
	private static void renderFrame(Scene scene, Camera camera, Vector4f clipPlane) {
		prepare();
		EntityManager.renderEntities(scene.getEntities(), scene.getLights(), camera, clipPlane, ShadowManager.getToShadowMapSpaceMatrix());
		terrainRenderer.render(scene.getTerrains(), scene.getLights(), camera, clipPlane, ShadowManager.getToShadowMapSpaceMatrix());
		skyBoxRenderer.render(camera);
	}
	
	private static void prepare() {
		GLControl.enableDepthTest();
		GLControl.clearColorAndDepthBuffers();
		GLControl.clearColor(Constants.FOG_COLOR.x, Constants.FOG_COLOR.y, Constants.FOG_COLOR.z);
	}
}
