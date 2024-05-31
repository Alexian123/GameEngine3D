package com.alexian123.renderer;

import com.alexian123.shader.IShader3D;

public interface IRenderer3D {
	
	void render();
	
	void cleanup();

	IShader3D getShader3D();
}
