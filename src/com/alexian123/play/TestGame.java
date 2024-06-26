package com.alexian123.play;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.alexian123.animation.Animation;
import com.alexian123.entity.Entity;
import com.alexian123.entity.LightEntity;
import com.alexian123.entity.Player;
import com.alexian123.font.FontType;
import com.alexian123.font.GUIText;
import com.alexian123.game.Camera;
import com.alexian123.game.Clock;
import com.alexian123.game.Game;
import com.alexian123.game.MousePicker;
import com.alexian123.game.Scene;
import com.alexian123.lighting.Light;
import com.alexian123.loader.Loader;
import com.alexian123.loader.collada.AnimatedModelFileLoader;
import com.alexian123.loader.collada.AnimationFileLoader;
import com.alexian123.loader.obj.OBJFileLoader;
import com.alexian123.loader.obj.OBJFileLoaderNM;
import com.alexian123.model.ModelMesh;
import com.alexian123.model.TexturedModel;
import com.alexian123.model.animated.AnimatedModel;
import com.alexian123.particle.ParticleSystem;
import com.alexian123.terrain.Terrain;
import com.alexian123.terrain.TerrainGrid;
import com.alexian123.texture.GUITexture;
import com.alexian123.texture.ModelTexture;
import com.alexian123.texture.ParticleTexture;
import com.alexian123.texture.TerrainTexturePack;
import com.alexian123.util.gl.TextureSampler;
import com.alexian123.water.Water;

public class TestGame extends Game {
	
	private Player player;
	private TerrainGrid terrainGrid;
	private MousePicker mousePicker;
	private Scene currentScene;
	private ParticleSystem fireSystem, fireSystem2;
	private ParticleSystem[] cosmicSystems = new ParticleSystem[10];
	
	private List<Entity> entities = new ArrayList<>();
	private List<Light> lights = new ArrayList<>();
	private List<Water> waters = new ArrayList<>();
	private List<GUITexture> guis = new ArrayList<>();
	
	private Entity barrel;
	private Entity crate;
	private Entity boulder;
	
	private LightEntity[] lamps;
	
	private FontType font;
	private GUIText text;
	
	public TestGame() {
		super(new Loader(), new Clock(), new Camera());
		initTerrain();
		initEntities();
		initWater();
		initLights();
		initParticles();
		initGUI();
		initText();
		currentScene = new Scene(entities, terrainGrid.asList(), waters, lights);
		mousePicker = new MousePicker(terrainGrid);
		clock.setTimeSpeed(1000);
	}
	

	@Override
	public void update() {
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			stop();
		}
		
		camera.update();
		clock.tick();
		mousePicker.update(camera);
		
		Vector3f terrainPoint = mousePicker.getCurrentTerrainPoint();
		if (terrainPoint != null && Mouse.isButtonDown(0)) {
			lamps[0].setPosition(terrainPoint);
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
			fireSystem.generateParticles();
		}
			
		for (ParticleSystem cosmicSystem : cosmicSystems) {
			cosmicSystem.generateParticles();
		}
	
		fireSystem2.generateParticles();
		
		barrel.incrementRotation(0, 0.5f, 0);
		crate.incrementRotation(0, 0.5f, 0);
		boulder.incrementRotation(0, 0.5f, 0);
		
		player.move(terrainGrid.getTerrainAt(player.getPosition().x, player.getPosition().z));
		camera.move();
		
		Vector3f playerPos = player.getPosition();
		text.hide();
		text = new GUIText(String.format("X:%.1f Y:%.1f Z:%.1f", playerPos.x, playerPos.y, playerPos.z), 2f, font, new Vector2f(0.0f, 0.0f), 1f, false);
		text.setColor(0, 0, 0);
		text.setOutlineColor(1, 1, 1);
		text.setBorderWidth(0.5f);
		text.show();
	}

	@Override
	public Scene getCurrentScene() {
		return currentScene;
	}

	@Override
	public List<GUITexture> getGUI() {
		return guis;
	}
	
	private void initTerrain() {
		TextureSampler bgTexture = loader.loadTexture("terrain/grass2");
		TextureSampler rTexture = loader.loadTexture("terrain/dirt");
		TextureSampler gTexture = loader.loadTexture("terrain/flowers");
		TextureSampler bTexture = loader.loadTexture("terrain/path");
		TextureSampler blendMap = loader.loadTexture("maps/blendMap");
		TerrainTexturePack texturePack = new TerrainTexturePack(bgTexture, rTexture, gTexture, bTexture);
		terrainGrid = new TerrainGrid(2);
		new Terrain(terrainGrid, loader, texturePack, blendMap);
		new Terrain(terrainGrid, loader, texturePack, blendMap);
		new Terrain(terrainGrid, loader, texturePack, blendMap);
		new Terrain(terrainGrid, loader, texturePack, blendMap);
	}
	
	private void initEntities() {
		ModelMesh rawModel;
		ModelTexture texture;
		TexturedModel texturedModel;
		AnimatedModel animatedModel;
		Animation animation;
		Random random = new Random();
		
		// player
		animatedModel = AnimatedModelFileLoader.loadEntity(loader, "cowboy", "entities/cowboy");
		animation = AnimationFileLoader.loadAnimation("cowboy");
		player = new Player(animatedModel, new Vector3f(376, 0, 423), new Vector3f(0, 0, 0), 0.6f, animation);
		camera.setPlayer(player);
		entities.add(player);
		
		// trees
		rawModel = loader.loadToVao(OBJFileLoader.loadOBJ("cherry"));
		texture = new ModelTexture(loader.loadTexture("entities/cherry"), 1, 0);
		texturedModel = new TexturedModel(rawModel, texture);
		for (int i = 0; i < 250; ++i) {
			float x = random.nextFloat() * 1600 - 800; 
			float z = random.nextFloat() * 1600 - 800;
			entities.add(new Entity(texturedModel, new Vector3f(x, terrainGrid.getTerrainAt(x, z).getHeightAtPosition(x, z), z), new Vector3f(0, 0, 0), 5));
		}
		
		// ferns
		rawModel = loader.loadToVao(OBJFileLoader.loadOBJ("fern"));
		texture = new ModelTexture(loader.loadTexture("entities/fern_atlas"), 1, 0, true, true, 2);
		texturedModel = new TexturedModel(rawModel, texture);
		for (int i = 0; i < 500; ++i) {
			float x = random.nextFloat() * 1600 - 800; 
			float z = random.nextFloat() * 1600 - 800;
			entities.add(new Entity(texturedModel, random.nextInt(4), new Vector3f(x, terrainGrid.getTerrainAt(x, z).getHeightAtPosition(x, z), z), new Vector3f(0, 0, 0), 1));
		}
		
		// lamps
		rawModel = loader.loadToVao(OBJFileLoader.loadOBJ("lantern"));
		texture = new ModelTexture(loader.loadTexture("entities/lantern"));
		texture.setLightingMap(loader.loadTexture("maps/lanternDiffuse"));
		texturedModel = new TexturedModel(rawModel, texture);
		lamps = new LightEntity[3];
		lamps[0] = new LightEntity(texturedModel, new Vector3f(329, terrainGrid.getHeightAt(329, 476), 476), new Vector3f(0, 0, 0), 1, 0.7f, new Vector3f(0.5f, 0.25f, 0), new Vector3f(1, 0.01f, 0.002f));
		lamps[1] = new LightEntity(texturedModel, new Vector3f(370, terrainGrid.getHeightAt(370, 300), 300), new Vector3f(0, 0, 0), 1, 0.7f, new Vector3f(0.5f, 0.25f, 0), new Vector3f(1, 0.01f, 0.002f));
		lamps[2] = new LightEntity(texturedModel, new Vector3f(293, terrainGrid.getHeightAt(293, 305), 305), new Vector3f(0, 0, 0), 1, 0.7f, new Vector3f(0.5f, 0.25f, 0), new Vector3f(1, 0.01f, 0.002f));
		entities.add(lamps[0]);
		entities.add(lamps[1]);
		entities.add(lamps[2]);
		
		// NM barrel
		rawModel = loader.loadToVao(OBJFileLoaderNM.loadOBJ("barrel"));	
		texture = new ModelTexture(loader.loadTexture("entities/barrel"), loader.loadTexture("maps/barrelNormal"), 10f, 0.5f);
		texture.setLightingMap(loader.loadTexture("maps/barrelSpecular"));
		texturedModel = new TexturedModel(rawModel, texture);
		barrel = new Entity(texturedModel, new Vector3f(334, terrainGrid.getHeightAt(334, 290) + 10f, 290), new Vector3f(0, 0, 0), 1);
		entities.add(barrel);
		
		// NM crate
		rawModel = loader.loadToVao(OBJFileLoaderNM.loadOBJ("crate"));	
		texture = new ModelTexture(loader.loadTexture("entities/crate"), loader.loadTexture("maps/crateNormal"), 10f, 0.5f);
		texturedModel = new TexturedModel(rawModel, texture);
		crate = new Entity(texturedModel, new Vector3f(315, terrainGrid.getHeightAt(315, 313) + 10f, 313), new Vector3f(0, 0, 0), 0.05f);
		entities.add(crate);
		
		// NM boulder
		rawModel = loader.loadToVao(OBJFileLoaderNM.loadOBJ("boulder"));	
		texture = new ModelTexture(loader.loadTexture("entities/boulder"), loader.loadTexture("maps/boulderNormal"), 10f, 0.5f);
		texturedModel = new TexturedModel(rawModel, texture);
		boulder = new Entity(texturedModel, new Vector3f(350, terrainGrid.getHeightAt(350, 308) + 10f, 308), new Vector3f(0, 0, 0), 1);
		entities.add(boulder);
	}

	
	private void initWater() {
		waters.add(new Water(378, 475, -12));
	}
	
	private void initLights() {
		Light sun = new Light(new Vector3f(1_000_000, 1_500_000, -1_000_000), new Vector3f(1.3f, 1.3f, 1.3f));
		lights.add(sun);
		lights.add(lamps[0].getLight());
		lights.add(lamps[1].getLight());
		lights.add(lamps[2].getLight());
	}
	
	private void initGUI() {
		GUITexture gui = new GUITexture(loader.loadTexture("gui/health"), new Vector2f(-0.75f, -0.9f), new Vector2f(0.25f, 0.35f));
		guis.add(gui);
	}
	
	private void initText() {
		font = loader.loadFont("candara");
		text = new GUIText("Hello, World!", 5f, font, new Vector2f(0.0f, 0.0f), 1f, false);
		text.setColor(0, 0, 0);
		text.setOutlineColor(1, 1, 1);
		text.setBorderWidth(0.5f);
		text.show();
	}
	
	private void initParticles() {
		ParticleTexture texture = new ParticleTexture(loader.loadTexture("particles/fire"), 8, true);
		fireSystem = new ParticleSystem(texture, 1000, 10, 0.1f, 2, 1.6f);
		fireSystem.randomizeRotation();
		fireSystem.setDirection(new Vector3f(0.5f, 0.5f, 0), 0.05f);
		fireSystem.setLifeError(0.1f);
		fireSystem.setSpeedError(0.25f);
		fireSystem.setScaleError(0.5f);
		fireSystem.setCenter(player.getPosition());
		
		fireSystem2 = new ParticleSystem(texture, 1000, 10, 0.1f, 2, 1.6f);
		fireSystem2.randomizeRotation();
		fireSystem2.setDirection(new Vector3f(0.5f, 0.5f, 0), 0.05f);
		fireSystem2.setLifeError(0.1f);
		fireSystem2.setSpeedError(0.25f);
		fireSystem2.setScaleError(0.5f);
		fireSystem2.setCenter(new Vector3f(283, 0, -233));
		
		texture = new ParticleTexture(loader.loadTexture("particles/cosmic"), 4, false);
		for (int i = 0; i < cosmicSystems.length; ++i) {
			cosmicSystems[i] = new ParticleSystem(texture, 100, 10, 0.1f, 2, 1.6f);
			cosmicSystems[i].randomizeRotation();
			cosmicSystems[i].setDirection(new Vector3f(0, 1, 0), 0.1f);
			cosmicSystems[i].setLifeError(0.1f);
			cosmicSystems[i].setSpeedError(0.25f);
			cosmicSystems[i].setScaleError(0.5f);
			cosmicSystems[i].setCenter(new Vector3f(251 + 5 * i, terrainGrid.getTerrainAt(251 + 5 * i, -273).getHeightAtPosition(251 + 5 * i, -273), -273));
		}
	}
}
