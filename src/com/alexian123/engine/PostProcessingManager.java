package com.alexian123.engine;

import org.lwjgl.opengl.Display;

import com.alexian123.loader.Loader;
import com.alexian123.model.ModelMesh;
import com.alexian123.rendering.postProcessing.CombineFilter;
import com.alexian123.rendering.postProcessing.ContrastChanger;
import com.alexian123.rendering.postProcessing.HorizontalBlur;
import com.alexian123.rendering.postProcessing.IDualInputFilter;
import com.alexian123.rendering.postProcessing.ISingleInputFilter;
import com.alexian123.rendering.postProcessing.VerticalBlur;
import com.alexian123.util.gl.GLControl;
import com.alexian123.util.gl.TextureSampler;

public class PostProcessingManager {
	
	private static final float[] VERTICES = { -1, 1, -1, -1, 1, 1, 1, -1 };	
	private static ModelMesh quad;

	private static ISingleInputFilter hBlur;
	private static ISingleInputFilter vBlur;
	private static IDualInputFilter combineFilter;
	private static ISingleInputFilter contrastChanger;
	
	private static boolean isInitialized = false;

	public static void init(Loader loader) {
		if (!isInitialized) {
			quad = loader.loadToVao(VERTICES, 2);
			int displayWidth = Display.getWidth();
			int displayHeight = Display.getHeight();
			hBlur = new HorizontalBlur(displayWidth / GameManager.SETTINGS.blurLevel, displayHeight / GameManager.SETTINGS.blurLevel);
			vBlur = new VerticalBlur(displayWidth / GameManager.SETTINGS.blurLevel, displayHeight / GameManager.SETTINGS.blurLevel);
			combineFilter = new CombineFilter(GameManager.SETTINGS.bloomFactor, displayWidth, displayHeight);
			contrastChanger = new ContrastChanger(GameManager.SETTINGS.contrast);
			isInitialized = true;
		}
	}
	
	public static void doPostProcessing(TextureSampler colorTexture, TextureSampler brightTexture) {
		start();
		contrastChanger.run(
			combineFilter.run(
				colorTexture, 
				vBlur.run(
					hBlur.run(brightTexture)
				)	
			)
		);
		end();
	}
	
	public static void cleanup() {
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
