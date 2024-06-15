package com.alexian123.water;

import com.alexian123.util.DepthBufferType;
import com.alexian123.util.Fbo;

public class WaterFrameBuffers {

	private static final int REFLECTION_WIDTH = 800;
	private static final int REFLECTION_HEIGHT = 600;
	
	private static final int REFRACTION_WIDTH = 1280;
	private static final int REFRACTION_HEIGHT = 720;

	private Fbo reflection;
	private Fbo refraction;
 
	public WaterFrameBuffers() { // call when loading the game
		this.reflection = new Fbo(REFLECTION_WIDTH, REFLECTION_HEIGHT, DepthBufferType.DEPTH_RENDER_BUFFER, true);
		this.refraction = new Fbo(REFRACTION_WIDTH, REFRACTION_HEIGHT, DepthBufferType.DEPTH_TEXTURE, true);
	}

	public void cleanup() { // call when closing the game
		reflection.cleanup();
		refraction.cleanup();
	}

	public void bindReflectionFrameBuffer() { // call before rendering to this FBO
		reflection.bindFrameBuffer();
	}
	
	public void bindRefractionFrameBuffer() { // call before rendering to this FBO
		refraction.bindFrameBuffer();
	}
	
	public void unbindCurrentFrameBuffer() {
		reflection.unbindFrameBuffer();
		refraction.unbindFrameBuffer();
	}

	public int getReflectionTexture() { // get the resulting texture
		return reflection.getColorTexture();
	}
	
	public int getRefractionTexture() { // get the resulting texture
		return refraction.getColorTexture();
	}
	
	public int getRefractionDepthTexture(){ //get the resulting depth texture
		return refraction.getDepthTexture();
	}
}