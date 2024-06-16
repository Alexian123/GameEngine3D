package com.alexian123.postProcessing;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import com.alexian123.rendering.ImageRenderer;
import com.alexian123.shader.CombineFilterShader;
import com.alexian123.util.Constants;

public class CombineFilter {
	
	private static final int MAX_NUM_TEXTURES = 2;
	
	private final CombineFilterShader shader = new CombineFilterShader();
	private final ImageRenderer renderer = new ImageRenderer();
	
	private final int numTextures;
	private final int[] textures = new int[MAX_NUM_TEXTURES];
	
	public CombineFilter() {
		shader.start();
		this.numTextures = shader.connectTextureUnits();
		shader.loadBloomFactor(Constants.BLOOM_FACTOR);
		shader.stop();
	}
	
	public void run(int colourTexture, int highlightTexture) {
		textures[0] = colourTexture;
		textures[1] = highlightTexture;
		shader.start();
		for (int i = 0; i < numTextures; ++i) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0 + i);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures[i]);
		}
		renderer.renderQuad();
		shader.stop();
	}
	
	public void cleanup() {
		renderer.cleanup();
		shader.cleanup();
	}

}