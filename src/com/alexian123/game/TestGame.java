package com.alexian123.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.alexian123.entity.Camera;
import com.alexian123.entity.Entity;
import com.alexian123.entity.Light;
import com.alexian123.entity.Player;
import com.alexian123.font.FontType;
import com.alexian123.font.GUIText;
import com.alexian123.loader.OBJFileLoader;
import com.alexian123.loader.OBJFileLoaderNM;
import com.alexian123.model.RawModel;
import com.alexian123.model.TexturedModel;
import com.alexian123.particle.ParticleSystem;
import com.alexian123.rendering.Scene;
import com.alexian123.terrain.TerrainGrid;
import com.alexian123.terrain.Water;
import com.alexian123.texture.GUITexture;
import com.alexian123.texture.ModelTexture;
import com.alexian123.texture.ParticleTexture;
import com.alexian123.texture.TerrainTexture;
import com.alexian123.texture.TerrainTexturePack;
import com.alexian123.util.MousePicker;

public class TestGame extends Game {
	
	private Player player;
	private Camera camera;
	private TerrainGrid terrain;
	private MousePicker mousePicker;
	private Scene currentScene;
	private ParticleSystem fireSystem;
	private ParticleSystem cosmicSystem;
	
	private List<Entity> entities = new ArrayList<>();
	private List<Light> lights = new ArrayList<>();
	private List<Water> waters = new ArrayList<>();
	private List<GUITexture> guis = new ArrayList<>();
	
	private Entity lastTree;
	private Entity barrel;
	private Entity crate;
	private Entity boulder;
	
	public TestGame(String title, int screenWidth, int screenHeight) {
		super(title, screenWidth, screenHeight);
		initTerrain();
		initEntities();
		initWater();
		initLights();
		initParticleSystem();
		initGUI();
		initText();
		currentScene = new Scene(entities, terrain.getTerrains(), waters, lights);
		camera = new Camera(player);
		mousePicker = new MousePicker(projectionMatrix, camera, terrain);
	}
	

	@Override
	protected void update() {
		clock.tick();
		mousePicker.update();
		Vector3f terrainPoint = mousePicker.getCurrentTerrainPoint();
		if (terrainPoint != null && Mouse.isButtonDown(0)) {
			lastTree.setPosition(terrainPoint);
		}
		
		fireSystem.generateParticles(player.getPosition());
		cosmicSystem.generateParticles(new Vector3f(251, -12, -273));
		
		barrel.incrementRotation(0, 0.5f, 0);
		crate.incrementRotation(0, 0.5f, 0);
		boulder.incrementRotation(0, 0.5f, 0);
		
		player.move(terrain.getTerrainAt(player.getPosition().x, player.getPosition().z));
		camera.move();
		
		System.out.println(player.getPosition());
	}

	@Override
	protected Scene getCurrentScene() {
		return currentScene;
	}

	@Override
	protected Camera getCamera() {
		return camera;
	}

	@Override
	protected List<GUITexture> getGUI() {
		return guis;
	}
	
	private void initTerrain() {
		TerrainTexture bgTexture = new TerrainTexture(loader.loadTexture("terrain/grass2"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("terrain/dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("terrain/flowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("terrain/path"));
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("maps/blendMap"));
		TerrainTexturePack texturePack = new TerrainTexturePack(bgTexture, rTexture, gTexture, bTexture);
		terrain = new TerrainGrid(-1, -1, 2, loader, 
				new TerrainTexturePack[] {texturePack, texturePack, texturePack, texturePack}, 
				new TerrainTexture[] {blendMap, blendMap, blendMap, blendMap}, 
				new String[] {"maps/heightmap", "maps/heightmap", "maps/heightmap", "maps/heightmap"});
	}
	
	private void initEntities() {
		RawModel rawModel;
		ModelTexture texture;
		TexturedModel texturedModel;
		Random random = new Random();
		
		// player
		rawModel = loader.loadToVao(OBJFileLoader.loadOBJ("person"));
		texture = new ModelTexture(loader.loadTexture("entities/playerTexture"));
		texturedModel = new TexturedModel(rawModel, texture);
		player = new Player(texturedModel, new Vector3f(286, 0, -268), new Vector3f(0, 100, 0), 0.6f);
		entities.add(player);
		
		// trees
		rawModel = loader.loadToVao(OBJFileLoader.loadOBJ("tree"));
		texture = new ModelTexture(loader.loadTexture("entities/tree"), 1, 0);
		texturedModel = new TexturedModel(rawModel, texture);
		for (int i = 0; i < 100; ++i) {
			float x = random.nextFloat() * 1600 - 800; 
			float z = random.nextFloat() * 1600 - 800;
			lastTree = new Entity(texturedModel, new Vector3f(x, terrain.getTerrainAt(x, z).getHeightAtPosition(x, z) - 0.5f, z), new Vector3f(0, 0, 0), 10);
			entities.add(lastTree);
		}
		
		// ferns
		rawModel = loader.loadToVao(OBJFileLoader.loadOBJ("fern"));
		texture = new ModelTexture(loader.loadTexture("entities/fern_atlas"), 1, 0, true, true, 2);
		texturedModel = new TexturedModel(rawModel, texture);
		for (int i = 0; i < 500; ++i) {
			float x = random.nextFloat() * 1600 - 800; 
			float z = random.nextFloat() * 1600 - 800;
			entities.add(new Entity(texturedModel, random.nextInt(4), new Vector3f(x, terrain.getTerrainAt(x, z).getHeightAtPosition(x, z), z), new Vector3f(0, 0, 0), 1));
		}
		
		// lamps
		rawModel = loader.loadToVao(OBJFileLoader.loadOBJ("lamp"));
		texture = new ModelTexture(loader.loadTexture("entities/lamp"), false, true);
		texturedModel = new TexturedModel(rawModel, texture);
		entities.add(new Entity(texturedModel, new Vector3f(185, -4.7f, -293), new Vector3f(0, 0, 0), 1));
		entities.add(new Entity(texturedModel, new Vector3f(370, 4.2f, -300), new Vector3f(0, 0, 0), 1));
		entities.add(new Entity(texturedModel, new Vector3f(293, -6.8f, -305), new Vector3f(0, 0, 0), 1));
		
		// NM barrel
		rawModel = loader.loadToVao(OBJFileLoaderNM.loadOBJ("barrel"));	
		texture = new ModelTexture(loader.loadTexture("entities/barrel"), loader.loadTexture("maps/barrelNormal"), 10f, 0.5f);;
		texturedModel = new TexturedModel(rawModel, texture);
		barrel = new Entity(texturedModel, new Vector3f(334, 10f, -290), new Vector3f(0, 0, 0), 1);
		entities.add(barrel);
		
		// NM crate
		rawModel = loader.loadToVao(OBJFileLoaderNM.loadOBJ("crate"));	
		texture = new ModelTexture(loader.loadTexture("entities/crate"), loader.loadTexture("maps/crateNormal"), 10f, 0.5f);
		texturedModel = new TexturedModel(rawModel, texture);
		crate = new Entity(texturedModel, new Vector3f(315, 10f, -313), new Vector3f(0, 0, 0), 0.05f);
		entities.add(crate);
		
		// NM boulder
		rawModel = loader.loadToVao(OBJFileLoaderNM.loadOBJ("boulder"));	
		texture = new ModelTexture(loader.loadTexture("entities/boulder"), loader.loadTexture("maps/boulderNormal"), 10f, 0.5f);
		texturedModel = new TexturedModel(rawModel, texture);
		boulder = new Entity(texturedModel, new Vector3f(350, 15f, -308), new Vector3f(0, 0, 0), 1);
		entities.add(boulder);
	}

	
	private void initWater() {
		waters.add(new Water(165, -175, terrain.getTerrainAt(165, -175).getHeightAtPosition(165, -175) - 1f));
		waters.add(new Water(304, -360, terrain.getTerrainAt(304, -360).getHeightAtPosition(304, -360) + 3f));
	}
	
	private void initLights() {
		Light sun = new Light(new Vector3f(0, 1000, -1000), new Vector3f(0.5f, 0.5f, 0.5f));
		Light light1 = new Light(new Vector3f(185, 10, -293), new Vector3f(2, 1, 0), new Vector3f(1, 0.01f, 0.002f));
		Light light2 = new Light(new Vector3f(370, 17, -300), new Vector3f(2, 1, 0), new Vector3f(1, 0.01f, 0.002f));
		Light light3 = new Light(new Vector3f(293, 7, -305), new Vector3f(2, 1, 0), new Vector3f(1, 0.01f, 0.002f));
		lights.add(sun);
		lights.add(light1);
		lights.add(light2);
		lights.add(light3);
	}
	
	private void initGUI() {
		GUITexture gui = new GUITexture(loader.loadTexture("gui/health"), new Vector2f(-0.75f, -0.9f), new Vector2f(0.25f, 0.35f));
		guis.add(gui);
	}
	
	private void initText() {
		FontType font = loader.loadFont("candara");
		GUIText text = new GUIText("Hello, World!", 5f, font, new Vector2f(0.0f, 0.0f), 1f, false);
		text.setColor(0, 0, 0);
		text.setOutlineColor(1, 1, 1);
		text.setBorderWidth(0.5f);
		super.addText(text);
	}
	
	private void initParticleSystem() {
		ParticleTexture texture = new ParticleTexture(loader.loadTexture("particles/fire"), 8, true);
		fireSystem = new ParticleSystem(texture, 1000, 10, 0.1f, 2, 1.6f);
		fireSystem.randomizeRotation();
		fireSystem.setDirection(new Vector3f(0.5f, 0.5f, 0), 0.05f);
		fireSystem.setLifeError(0.1f);
		fireSystem.setSpeedError(0.25f);
		fireSystem.setScaleError(0.5f);
		texture = new ParticleTexture(loader.loadTexture("particles/cosmic"), 4, false);
		cosmicSystem = new ParticleSystem(texture, 100, 10, 0.1f, 2, 1.6f);
		cosmicSystem.randomizeRotation();
		cosmicSystem.setDirection(new Vector3f(0, 1, 0), 0.1f);
		cosmicSystem.setLifeError(0.1f);
		cosmicSystem.setSpeedError(0.25f);
		cosmicSystem.setScaleError(0.5f);
	}
}
