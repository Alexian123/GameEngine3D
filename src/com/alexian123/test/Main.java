package com.alexian123.test;

import com.alexian123.models.RawModel;
import com.alexian123.renderEngine.DisplayManager;
import com.alexian123.renderEngine.Loader;
import com.alexian123.renderEngine.Renderer;
import com.alexian123.shaders.StaticShader;

public class Main {

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		Renderer renderer = new Renderer();
		StaticShader shader = new StaticShader();
		
		float[] vertices = {
			    -0.5f, 0.5f, 0f,
			    -0.5f, -0.5f, 0f,
			    0.5f, -0.5f, 0f,
			    0.5f, 0.5f, 0f,
		};
		int[] indices = {
				0, 1, 3,
				3, 1, 2
		};
		RawModel model = loader.loadToVao(vertices, indices);
		
		while (!DisplayManager.displayShouldClose()) {
			renderer.prepare();
			shader.start();
			renderer.render(model);
			shader.stop();
			
			DisplayManager.updateDisplay();
		}
		
		shader.cleanup();
		loader.cleanup();
		DisplayManager.closeDisplay();
	}

}