package com.alexian123.rendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import com.alexian123.entity.Camera;
import com.alexian123.entity.Entity;
import com.alexian123.font.FontType;
import com.alexian123.font.GUIText;
import com.alexian123.font.TextMeshData;
import com.alexian123.loader.Loader;
import com.alexian123.model.TexturedModel;
import com.alexian123.particle.Particle;
import com.alexian123.particle.ParticleSorter;
import com.alexian123.terrain.Water;
import com.alexian123.terrain.WaterFrameBuffers;
import com.alexian123.texture.GUITexture;
import com.alexian123.texture.ModelTexture;
import com.alexian123.texture.ParticleTexture;
import com.alexian123.util.Clock;
import com.alexian123.util.Constants;

public class RenderingManager {
	
	private static final Matrix4f PROJECTION_MATRIX = createProjectionMatrix();
	
	private static Loader loader;
	
	private static EntityRenderer entityRenderer;
	private static EntityRendererNM entityRendererNM;
	private static TerrainRenderer terrainRenderer;
	private static WaterRenderer waterRenderer;
	private static SkyBoxRenderer skyBoxRenderer;
	private static ParticleRenderer particleRenderer;
	private static GUIRenderer guiRenderer;
	private static FontRenderer fontRenderer;
	
	private static Map<TexturedModel, List<Entity>> entities;
	private static Map<TexturedModel, List<Entity>> entitiesNM;
	private static Map<ParticleTexture, List<Particle>> particles;
	private static Map<FontType, List<GUIText>> texts;
	
	private static boolean isInitialized = false;
	
	public static void init(Loader loader, Clock clock) {
		if (!isInitialized) {
			enableCulling();
			RenderingManager.loader = loader;
			entityRenderer = new EntityRenderer(PROJECTION_MATRIX);
			entityRendererNM = new EntityRendererNM(PROJECTION_MATRIX);
			terrainRenderer = new TerrainRenderer(PROJECTION_MATRIX);
			waterRenderer = new WaterRenderer(loader, PROJECTION_MATRIX);
			skyBoxRenderer = new SkyBoxRenderer(loader, PROJECTION_MATRIX, clock);
			particleRenderer = new ParticleRenderer(loader, PROJECTION_MATRIX);
			guiRenderer = new GUIRenderer(loader);
			fontRenderer = new FontRenderer();
			entities = new HashMap<>();
			entitiesNM = new HashMap<>();
			particles = new HashMap<>();
			texts = new HashMap<>();
			isInitialized = true;
		}
	}
	
	public static void render(Scene scene, Camera camera, List<GUITexture> guis) {
		updateParticles(camera);
		renderWaterFX(scene, camera);
		renderScene(scene, camera, new Vector4f(0, -1, 0, 1000));
		waterRenderer.render(scene.getWaters(), camera, scene.getLights());
		particleRenderer.render(particles, camera);
		guiRenderer.render(guis);
		fontRenderer.render(texts);
	}
	
	public static void cleanup() {
		entityRenderer.cleanup();
		entityRendererNM.cleanup();
		terrainRenderer.cleanup();
		waterRenderer.cleanup();
		skyBoxRenderer.cleanup();
		particleRenderer.cleanup();
		guiRenderer.cleanup();
		fontRenderer.cleanup();
	}
	
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
	
	public static void addParticle(Particle particle) {
		ParticleTexture texture = particle.getTexture();
		List<Particle> batch = particles.get(texture);
		if (batch == null) {
			batch = new ArrayList<>();
			particles.put(texture,  batch);
		}
		batch.add(particle);
	}
	
	public static void loadText(GUIText text) {
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		int vao = loader.loadToVao(data.getVertexPositions(), data.getTextureCoords());
		text.setMeshInfo(vao, data.getVertexCount());
		List<GUIText> batch = texts.get(font);
		if (batch == null) {
			batch = new ArrayList<>();
			texts.put(font, batch);
		}
		batch.add(text);
	}
	
	public static void removeText(GUIText text) {
		List<GUIText> batch = texts.get(text.getFont());
		if (batch != null) {
			batch.remove(text);
			if (batch.isEmpty()) {
				loader.deleteVao(text.getMesh());	// delete VAO and associated VBO's from memory 
				texts.remove(text.getFont());
			}
		}
	}
	
	private static void renderWaterFX(Scene scene, Camera camera) {
		GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
		for (Water water : scene.getWaters()) {
			WaterFrameBuffers fbos = water.getFbos();
			float distance = 2f * (camera.getPosition().y - water.getHeight());
			camera.getPosition().y -= distance;
			camera.invertPitch();
			fbos.bindReflectionFrameBuffer();
			renderScene(scene, camera, new Vector4f(0, 1, 0, -water.getHeight() + 1f));
			camera.getPosition().y += distance;
			camera.invertPitch();
			fbos.bindRefractionFrameBuffer();
			renderScene(scene, camera, new Vector4f(0, -1, 0, water.getHeight() + 0.5f));
			fbos.unbindCurrentFrameBuffer(); 
		}
		GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
	}
	
	private static void renderScene(Scene scene, Camera camera, Vector4f clipPlane) {
		prepare();
		for (Entity entity : scene.getEntities()) {
			processEntity(entity);
		}
		entityRenderer.render(entities, scene.getLights(), camera, clipPlane);
		entityRendererNM.render(entitiesNM, scene.getLights(), camera, clipPlane);
		terrainRenderer.render(scene.getTerrains(), scene.getLights(), camera, clipPlane);
		skyBoxRenderer.render(camera);
		entities.clear();
		entitiesNM.clear();
	}
	
	private static void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(Constants.FOG_COLOR.x, Constants.FOG_COLOR.y, Constants.FOG_COLOR.z, 1.0f);
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
	
	private static void updateParticles(Camera camera) {
		Iterator<Entry<ParticleTexture, List<Particle>>> mapIter = particles.entrySet().iterator();
		while (mapIter.hasNext()) {
			List<Particle> batch = mapIter.next().getValue();
			Iterator<Particle> iter = batch.iterator();
			while (iter.hasNext()) {
				Particle p = iter.next();
				boolean alive = p.update(camera);
				if (!alive) {
					iter.remove();
					if (batch.isEmpty()) {
						mapIter.remove();
					}
				}
			}
				ParticleSorter.sortHighToLow(batch);
		}
	}
	
	private static Matrix4f createProjectionMatrix() {
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float xScale = (float) (1.0f / Math.tan(Math.toRadians(Constants.FOV / 2.0f)));
		float yScale = xScale * aspectRatio;
		float frustumLength = Constants.FAR_PLANE - Constants.NEAR_PLANE;
		Matrix4f projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = xScale;
		projectionMatrix.m11 = yScale;
		projectionMatrix.m22 = -((Constants.FAR_PLANE + Constants.NEAR_PLANE) / frustumLength);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * Constants.NEAR_PLANE * Constants.FAR_PLANE) / frustumLength);
		projectionMatrix.m33 = 0;
		return projectionMatrix;
	}
}
