package com.alexian123.shadow;

import com.alexian123.util.DepthBufferType;
import com.alexian123.util.Fbo;

/**
 * The frame buffer for the shadow pass. This class sets up the depth texture
 * which can be rendered to during the shadow render pass, producing a shadow
 * map.
 * 
 * @author Karl
 *
 */
public class ShadowFrameBuffer extends Fbo {

	/**
	 * Initialises the frame buffer and shadow map of a certain size.
	 * 
	 * @param width
	 *            - the width of the shadow map in pixels.
	 * @param height
	 *            - the height of the shadow map in pixels.
	 */
	public ShadowFrameBuffer(int width, int height) {
		super(width, height, DepthBufferType.DEPTH_TEXTURE, false);
	}

	/**
	 * @return The ID of the shadow map texture.
	 */
	public int getShadowMap() {
		return getDepthTexture();
	}
}