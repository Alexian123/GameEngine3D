package com.alexian123.postProcessing;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import com.alexian123.rendering.ImageRenderer;
import com.alexian123.shader.ContrastShader;

public class ContrastChanger {

	private final ContrastShader shader = new ContrastShader();
	private final ImageRenderer renderer = new ImageRenderer();
	
	public ContrastChanger(float contrastValue) {
		shader.start();
		shader.loadContrast(contrastValue);
		shader.stop();
	}

	public void run(int texture) {
		shader.start();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		renderer.renderQuad();
		shader.stop();
	}
	
	public void cleanup() {
		renderer.cleanup();
		shader.cleanup();
	}
}