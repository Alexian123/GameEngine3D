package com.alexian123.water;

import com.alexian123.engine.GameManager;
import com.alexian123.util.enums.DepthBufferType;
import com.alexian123.util.gl.Fbo;
import com.alexian123.util.gl.TextureSampler;

public class WaterFrameBuffers {

	private Fbo reflection;
	private Fbo refraction;
 
	public WaterFrameBuffers() { // call when loading the game
		this.reflection = new Fbo(GameManager.SETTINGS.waterReflectionWidth, GameManager.SETTINGS.waterReflectionHeight, 
				DepthBufferType.DEPTH_RENDER_BUFFER, true);
		this.refraction = new Fbo(GameManager.SETTINGS.waterRefractionWidth, GameManager.SETTINGS.waterRefractionHeight, 
				DepthBufferType.DEPTH_TEXTURE, true);
	}

	public void cleanup() { // call when closing the game
		reflection.delete();
		refraction.delete();
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

	public TextureSampler getReflectionTexture() { // get the resulting texture
		return reflection.getColorTexture();
	}
	
	public TextureSampler getRefractionTexture() { // get the resulting texture
		return refraction.getColorTexture();
	}
	
	public TextureSampler getRefractionDepthTexture(){ //get the resulting depth texture
		return refraction.getDepthTexture();
	}
}