package com.alexian123.postProcessing;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import com.alexian123.rendering.ImageRenderer;
import com.alexian123.shader.VerticalBlurShader;

public class VerticalBlur {
	
	private final VerticalBlurShader shader = new VerticalBlurShader();
	private final ImageRenderer renderer;
	
	public VerticalBlur(int targetFboWidth, int targetFboHeight) {
		this.renderer = new ImageRenderer(targetFboWidth, targetFboHeight);
		shader.start();
		shader.loadTargetHeight(targetFboHeight);
		shader.stop();
	}

	
	public void run(int texture) {
		shader.start();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		renderer.renderQuad();
		shader.stop();
	}
	
	public int getOutputTexture() {
		return renderer.getOutputTexture();
	}
	
	public void cleanup() {
		renderer.cleanup();
		shader.cleanup();
	}
}
