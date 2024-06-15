package com.alexian123.postProcessing;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import com.alexian123.rendering.ImageRenderer;
import com.alexian123.shader.ContrastShader;

public class ContrastChanger {

	private final ImageRenderer renderer = new ImageRenderer();
	private final ContrastShader shader = new ContrastShader();
	
	private float contrast;
	
	public ContrastChanger(float contrast) {
		this.contrast = contrast;
	}
	
	public float getContrast() {
		return contrast;
	}

	public void setContrast(float contrast) {
		this.contrast = contrast;
	}

	public void run(int texture) {
		shader.start();
		shader.loadContrast(contrast);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		renderer.render();
		shader.stop();
	}
	
	public void cleanup() {
		renderer.cleanup();
		shader.cleanup();
	}
}
