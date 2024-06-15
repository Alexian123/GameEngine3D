package com.alexian123.engine;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.alexian123.loader.Loader;
import com.alexian123.model.RawModel;
import com.alexian123.postProcessing.ContrastChanger;
import com.alexian123.postProcessing.HorizontalBlur;
import com.alexian123.postProcessing.VerticalBlur;
import com.alexian123.util.Constants;

public class PostProcessingManager {

	private static final float[] VERTICES = { -1, 1, -1, -1, 1, 1, 1, -1 };	
	private static RawModel quad;
	
	private static ContrastChanger contrastChanger;
	private static HorizontalBlur hBlur;
	private static VerticalBlur vBlur;
	
	private static boolean isInitialized = false;

	public static void init(Loader loader) {
		if (!isInitialized) {
			quad = loader.loadToVao(VERTICES, 2);
			contrastChanger = new ContrastChanger(0.3f);
			hBlur = new HorizontalBlur(Display.getWidth() / Constants.BLUR_LEVEL, Display.getHeight() / Constants.BLUR_LEVEL);
			vBlur = new VerticalBlur(Display.getWidth() / Constants.BLUR_LEVEL, Display.getHeight() / Constants.BLUR_LEVEL);
			isInitialized = true;
		}
	}
	
	public static void doPostProcessing(int colourTexture) {
		start();
		hBlur.run(colourTexture);
		vBlur.run(hBlur.getOutputTexture());
		contrastChanger.run(vBlur.getOutputTexture());
		end();
	}
	
	public static void cleanup() {
		contrastChanger.cleanup();
		hBlur.cleanup();
		vBlur.cleanup();
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
