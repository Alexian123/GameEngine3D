package com.alexian123.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.alexian123.entity.Camera;
import com.alexian123.entity.Entity;
import com.alexian123.entity.Light;
import com.alexian123.entity.Player;
import com.alexian123.font.FontType;
import com.alexian123.font.GUIText;
import com.alexian123.loader.Loader;
import com.alexian123.loader.OBJFileLoader;
import com.alexian123.loader.OBJFileLoaderNM;
import com.alexian123.model.RawModel;
import com.alexian123.model.TexturedModel;
import com.alexian123.renderer.DisplayManager;
import com.alexian123.renderer.RenderingManager;
import com.alexian123.renderer.Scene;
import com.alexian123.terrain.TerrainGrid;
import com.alexian123.terrain.Water;
import com.alexian123.texture.GUITexture;
import com.alexian123.texture.ModelTexture;
import com.alexian123.texture.TerrainTexturePack;
import com.alexian123.util.Clock;
import com.alexian123.util.MousePicker;
import com.alexian123.texture.TerrainTexture;

public class Main {

	public static void main(String[] args) {
		DisplayManager.createDisplay("GameEngine3D", 1600, 900);
		Loader loader = new Loader();
		Clock clock = new Clock(0.0f);
		RenderingManager.init(loader, clock);
		
		// text
		FontType font = loader.loadFont("candara");
		GUIText text = new GUIText("Hello, World!", 5f, font, new Vector2f(0.0f, 0.0f), 1f, false);
		text.setColor(0, 0, 0);
		text.setOutlineColor(1, 1, 1);
		text.setBorderWidth(0.5f);
		RenderingManager.loadText(text);
		
		// terrain
		TerrainTexture bgTexture = new TerrainTexture(loader.loadTexture("grass2"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("flowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		TerrainTexturePack texturePack = new TerrainTexturePack(bgTexture, rTexture, gTexture, bTexture);
		TerrainGrid terrainGrid = new TerrainGrid(-1, -1, 2, loader, 
				new TerrainTexturePack[] {texturePack, texturePack, texturePack, texturePack}, 
				new TerrainTexture[] {blendMap, blendMap, blendMap, blendMap}, 
				new String[] {"heightmap", "heightmap", "heightmap", "heightmap"});
		
	
		RawModel rawModel;
		ModelTexture texture;
		TexturedModel texturedModel;
		Random random = new Random();
		
		/* Entities */
		List<Entity> entities = new ArrayList<>();
		
		// trees
		rawModel = loader.loadToVao(OBJFileLoader.loadOBJ("tree"));
		texture = new ModelTexture(loader.loadTexture("tree"), 1, 0);
		texturedModel = new TexturedModel(rawModel, texture);
		Entity tree = null;
		for (int i = 0; i < 100; ++i) {
			float x = random.nextFloat() * 1600 - 800; 
			float z = random.nextFloat() * 1600 - 800;
			tree = new Entity(texturedModel, new Vector3f(x, terrainGrid.getTerrainAt(x, z).getHeightAtPosition(x, z) - 0.5f, z), new Vector3f(0, 0, 0), 10);
			entities.add(tree);
		}
		
		// ferns
		rawModel = loader.loadToVao(OBJFileLoader.loadOBJ("fern"));
		texture = new ModelTexture(loader.loadTexture("fern_atlas"), 1, 0, true, true, 2);
		texturedModel = new TexturedModel(rawModel, texture);
		for (int i = 0; i < 500; ++i) {
			float x = random.nextFloat() * 1600 - 800; 
			float z = random.nextFloat() * 1600 - 800;
			entities.add(new Entity(texturedModel, random.nextInt(4), new Vector3f(x, terrainGrid.getTerrainAt(x, z).getHeightAtPosition(x, z), z), new Vector3f(0, 0, 0), 1));
		}
		
		// player
		rawModel = loader.loadToVao(OBJFileLoader.loadOBJ("person"));
		texture = new ModelTexture(loader.loadTexture("playerTexture"));
		texturedModel = new TexturedModel(rawModel, texture);
		Player player = new Player(texturedModel, new Vector3f(286, 0, -268), new Vector3f(0, 100, 0), 0.6f);
		entities.add(player);
		
		// lamps
		rawModel = loader.loadToVao(OBJFileLoader.loadOBJ("lamp"));
		texture = new ModelTexture(loader.loadTexture("lamp"), false, true);
		texturedModel = new TexturedModel(rawModel, texture);
		entities.add(new Entity(texturedModel, new Vector3f(185, -4.7f, -293), new Vector3f(0, 0, 0), 1));
		entities.add(new Entity(texturedModel, new Vector3f(370, 4.2f, -300), new Vector3f(0, 0, 0), 1));
		entities.add(new Entity(texturedModel, new Vector3f(293, -6.8f, -305), new Vector3f(0, 0, 0), 1));
		
		// NM barrel
		rawModel = loader.loadToVao(OBJFileLoaderNM.loadOBJ("barrel"));	
		texture = new ModelTexture(loader.loadTexture("barrel"), loader.loadTexture("barrelNormal"), 10f, 0.5f);
		texturedModel = new TexturedModel(rawModel, texture);
		Entity barrel = new Entity(texturedModel, new Vector3f(334, 10f, -290), new Vector3f(0, 0, 0), 1);
		entities.add(barrel);
		
		// NM crate
		rawModel = loader.loadToVao(OBJFileLoaderNM.loadOBJ("crate"));	
		texture = new ModelTexture(loader.loadTexture("crate"), loader.loadTexture("crateNormal"), 10f, 0.5f);
		texturedModel = new TexturedModel(rawModel, texture);
		Entity crate = new Entity(texturedModel, new Vector3f(315, 10f, -313), new Vector3f(0, 0, 0), 0.05f);
		entities.add(crate);
		
		// NM boulder
		rawModel = loader.loadToVao(OBJFileLoaderNM.loadOBJ("boulder"));	
		texture = new ModelTexture(loader.loadTexture("boulder"), loader.loadTexture("boulderNormal"), 10f, 0.5f);
		texturedModel = new TexturedModel(rawModel, texture);
		Entity boulder = new Entity(texturedModel, new Vector3f(350, 15f, -308), new Vector3f(0, 0, 0), 1);
		entities.add(boulder);
		/* -------- */
		
		// camera
		Camera camera = new Camera(player);
		
		// lights
		List<Light> lights = new ArrayList<>();
		Light sun = new Light(new Vector3f(0, 1000, -1000), new Vector3f(0.5f, 0.5f, 0.5f));
		Light light1 = new Light(new Vector3f(185, 10, -293), new Vector3f(2, 1, 0), new Vector3f(1, 0.01f, 0.002f));
		Light light2 = new Light(new Vector3f(370, 17, -300), new Vector3f(2, 1, 0), new Vector3f(1, 0.01f, 0.002f));
		Light light3 = new Light(new Vector3f(293, 7, -305), new Vector3f(2, 1, 0), new Vector3f(1, 0.01f, 0.002f));
		lights.add(sun);
		lights.add(light1);
		lights.add(light2);
		lights.add(light3);
		
		// water
		List<Water> waters = new ArrayList<>();
		waters.add(new Water(165, -175, terrainGrid.getTerrainAt(165, -175).getHeightAtPosition(165, -175) - 1f));
		waters.add(new Water(304, -360, terrainGrid.getTerrainAt(304, -360).getHeightAtPosition(304, -360) + 3f));
		
		// scene
		Scene scene = new Scene(entities, terrainGrid.getTerrains(), waters, lights);
		
		// GUI
		GUITexture gui = new GUITexture(loader.loadTexture("GUI/health"), new Vector2f(-0.75f, -0.9f), new Vector2f(0.25f, 0.35f));
		List<GUITexture> guis = new ArrayList<>();
		guis.add(gui);
		
		// Mouse picker
		MousePicker mousePicker = new MousePicker(RenderingManager.getProjectionMatrix(), camera, terrainGrid);
		
		while (!DisplayManager.displayShouldClose()) {
			clock.tick();
			
			mousePicker.update();
			Vector3f terrainPoint = mousePicker.getCurrentTerrainPoint();
			if (terrainPoint != null && Mouse.isButtonDown(0)) {
				tree.setPosition(terrainPoint);
			}		
			
			barrel.incrementRotation(0, 0.5f, 0);
			crate.incrementRotation(0, 0.5f, 0);
			boulder.incrementRotation(0, 0.5f, 0);
			
			player.move(terrainGrid.getTerrainAt(player.getPosition().x, player.getPosition().z));
			camera.move();
			
			System.out.println(player.getPosition());
			
			RenderingManager.render(scene, camera, guis);
			DisplayManager.updateDisplay();
		}
		
		loader.cleanup();
		RenderingManager.cleanup();
		for (Water water : waters) {
			water.getFbos().cleanup();
		}
		DisplayManager.closeDisplay();
	}

}