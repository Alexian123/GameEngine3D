package com.alexian123.engine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.alexian123.loader.Loader;
import com.alexian123.model.RawModel;
import com.alexian123.postProcessing.ContrastChanger;

public class PostProcessingManager {

	private static final float[] VERTICES = { -1, 1, -1, -1, 1, 1, 1, -1 };	
	private static RawModel quad;
	
	private static ContrastChanger contrastChanger;
	
	private static boolean isInitialized = false;

	public static void init(Loader loader) {
		if (!isInitialized) {
			quad = loader.loadToVao(VERTICES, 2);
			contrastChanger = new ContrastChanger(0.3f);
			isInitialized = true;
		}
	}
	
	public static void doPostProcessing(int colourTexture) {
		start();
		contrastChanger.run(colourTexture);
		end();
	}
	
	public static void cleanup() {
		contrastChanger.cleanup();
	}
	
	private static void start() {
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	private static void end() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
}
