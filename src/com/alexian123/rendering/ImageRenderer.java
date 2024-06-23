package com.alexian123.rendering;

import com.alexian123.util.enums.DepthBufferType;
import com.alexian123.util.gl.Fbo;
import com.alexian123.util.gl.GLControl;
import com.alexian123.util.gl.TextureSampler;

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
		GLControl.clearColorBuffer();
		GLControl.drawArraysTS(4);
		if (fbo != null) {
			fbo.unbindFrameBuffer();
		}
	}

	public TextureSampler getOutputTexture() {
		return fbo.getColorTexture();
	}
}