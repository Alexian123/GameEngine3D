package com.alexian123.rendering;

import org.lwjgl.opengl.GL11;

import com.alexian123.util.DepthBufferType;
import com.alexian123.util.Fbo;

public class ImageRenderer {

	private final Fbo fbo;

	public ImageRenderer(int width, int height) {
		this.fbo = new Fbo(width, height, DepthBufferType.NONE, false);
	}

	public ImageRenderer() {
		this.fbo = null;
	}

	public void renderQuad() {
		if (fbo != null) {
			fbo.bindFrameBuffer();
		}
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		if (fbo != null) {
			fbo.unbindFrameBuffer();
		}
	}

	public int getOutputTexture() {
		return fbo.getColorTexture();
	}

	public void cleanup() {
		if (fbo != null) {
			fbo.cleanup();
		}
	}
}