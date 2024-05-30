package com.alexian123.renderer;

import com.alexian123.entity.Camera;
import com.alexian123.entity.Light;

public interface IRenderer3D {
	
	public void render(Light sun, Camera camera);
	
	public void cleanup();

}
