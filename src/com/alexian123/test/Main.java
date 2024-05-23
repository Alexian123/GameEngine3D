package com.alexian123.test;

import org.lwjgl.util.vector.Vector3f;

import com.alexian123.entities.Camera;
import com.alexian123.entities.Entity;
import com.alexian123.models.RawModel;
import com.alexian123.models.TexturedModel;
import com.alexian123.renderEngine.DisplayManager;
import com.alexian123.renderEngine.Loader;
import com.alexian123.renderEngine.Renderer;
import com.alexian123.shaders.StaticShader;
import com.alexian123.textures.ModelTexture;

public class Main {

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer(shader);
		
		float[] vertices = {			
				-0.5f,0.5f,-0.5f,	
				-0.5f,-0.5f,-0.5f,	
				0.5f,-0.5f,-0.5f,	
				0.5f,0.5f,-0.5f,		
				
				-0.5f,0.5f,0.5f,	
				-0.5f,-0.5f,0.5f,	
				0.5f,-0.5f,0.5f,	
				0.5f,0.5f,0.5f,
				
				0.5f,0.5f,-0.5f,	
				0.5f,-0.5f,-0.5f,	
				0.5f,-0.5f,0.5f,	
				0.5f,0.5f,0.5f,
				
				-0.5f,0.5f,-0.5f,	
				-0.5f,-0.5f,-0.5f,	
				-0.5f,-0.5f,0.5f,	
				-0.5f,0.5f,0.5f,
				
				-0.5f,0.5f,0.5f,
				-0.5f,0.5f,-0.5f,
				0.5f,0.5f,-0.5f,
				0.5f,0.5f,0.5f,
				
				-0.5f,-0.5f,0.5f,
				-0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,0.5f
				
		};
		float[] textureCoords = {
				0,0,
				0,1,
				1,1,
				1,0,			
				0,0,
				0,1,
				1,1,
				1,0,			
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0

				
		};
		int[] indices = {
				0,1,3,	
				3,1,2,	
				4,5,7,
				7,5,6,
				8,9,11,
				11,9,10,
				12,13,15,
				15,13,14,	
				16,17,19,
				19,17,18,
				20,21,23,
				23,21,22

		};
		
		RawModel model = loader.loadToVao(vertices, indices, textureCoords);
		ModelTexture texture = new ModelTexture(loader.loadTexture("grass"));
		TexturedModel texturedModel = new TexturedModel(model, texture);
		Entity entity = new Entity(texturedModel, new Vector3f(0, 0, -5), new Vector3f(0, 0, 0), 1);
		
		Camera camera = new Camera();
		
		while (!DisplayManager.displayShouldClose()) {
			entity.incrementRotation(0.5f, 0.5f, 0);
			camera.move();
			
			renderer.prepare();
			shader.start();
			shader.loadViewMatrix(camera);
			renderer.render(entity, shader);
			shader.stop();
			
			DisplayManager.updateDisplay();
		}
		
		shader.cleanup();
		loader.cleanup();
		DisplayManager.closeDisplay();
	}

}