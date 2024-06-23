package com.alexian123.engine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {
	
	private static boolean isInitialized = false;
	
	public static void init(String title, int width, int height) {
		if (!isInitialized) {
			ContextAttribs attribs = new ContextAttribs(3, 3).withForwardCompatible(true).withProfileCore(true);
			try {
				Display.setDisplayMode(new DisplayMode(width, height));
				Display.create(new PixelFormat().withDepthBits(24), attribs);
				Display.setTitle(title);
				GL11.glEnable(GL13.GL_MULTISAMPLE);
			} catch (LWJGLException e) {
				e.printStackTrace();
				System.err.println("Error creating display");
				System.exit(-1);
			}	
			GL11.glViewport(0, 0, width, height);
			isInitialized = true;
		}
	}
	
	public static void updateDisplay() {
		Display.sync(GameManager.SETTINGS.fpsCap);
		Display.update();
	}
	
	public static void closeDisplay() {
		Display.destroy();
	}
	
	public static boolean displayShouldClose() {
		return Display.isCloseRequested();
	}
}