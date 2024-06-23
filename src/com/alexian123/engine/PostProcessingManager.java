package com.alexian123.engine;

import org.lwjgl.opengl.Display;

import com.alexian123.loader.Loader;
import com.alexian123.model.ModelMesh;
import com.alexian123.rendering.postProcessing.BrightFilter;
import com.alexian123.rendering.postProcessing.CombineFilter;
import com.alexian123.rendering.postProcessing.ContrastChanger;
import com.alexian123.rendering.postProcessing.HorizontalBlur;
import com.alexian123.rendering.postProcessing.VerticalBlur;
import com.alexian123.util.Constants;
import com.alexian123.util.gl.GLControl;
import com.alexian123.util.gl.TextureSampler;

public class PostProcessingManager {

	private static final float[] VERTICES = { -1, 1, -1, -1, 1, 1, 1, -1 };	
	private static ModelMesh quad;
	
	private static BrightFilter brightFilter;
	private static HorizontalBlur hBlur;
	private static VerticalBlur vBlur;
	private static CombineFilter combineFilter;
	private static ContrastChanger contrastChanger;
	
	private static boolean isInitialized = false;

	public static void init(Loader loader) {
		if (!isInitialized) {
			quad = loader.loadToVao(VERTICES, 2);
			brightFilter = new BrightFilter(Display.getWidth() / 2, Display.getHeight() / 2);
			hBlur = new HorizontalBlur(Display.getWidth() / Constants.BLUR_LEVEL, Display.getHeight() / Constants.BLUR_LEVEL);
			vBlur = new VerticalBlur(Display.getWidth() / Constants.BLUR_LEVEL, Display.getHeight() / Constants.BLUR_LEVEL);
			combineFilter = new CombineFilter();
			contrastChanger = new ContrastChanger(0.3f);
			isInitialized = true;
		}
	}
	
	public static void doPostProcessing(TextureSampler colourTexture, TextureSampler brightTexture) {
		start();
		//brightFilter.run(colourTexture);
		hBlur.run(brightTexture);
		vBlur.run(hBlur.getOutputTexture());
		combineFilter.run(colourTexture, vBlur.getOutputTexture());
		end();
	}
	
	public static void cleanup() {
		brightFilter.cleanup();
		hBlur.cleanup();
		vBlur.cleanup();
		combineFilter.cleanup();
		contrastChanger.cleanup();
	}
	
	private static void start() {
		quad.getVao().bind(0);
		GLControl.disableDepthTest();
	}
	
	private static void end() {
		GLControl.enableDepthTest();
		quad.getVao().unbind(0);
	}
}
