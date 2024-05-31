package com.alexian123.shader;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import com.alexian123.entity.Camera;
import com.alexian123.light.Light;

public interface IShader3D {
	static final int MAX_LIGHTS = 4;

	void start();
	
	void loadSkyColor(Vector3f skyColor);
	
	void loadLights(List<Light> lights);
	
	void loadViewMatrix(Camera camera);
	
	void stop();
}
