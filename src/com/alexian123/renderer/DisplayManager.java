package com.alexian123.renderer;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {
	
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static final int FPS_CAP = 144;
	
	private static long lastFrameTime;
	private static float timeDelta;
	
	public static void createDisplay() {
		ContextAttribs attribs = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), attribs);
			Display.setTitle("GameEngine3D");
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		lastFrameTime = getCurrentTime();
	}
	
	public static void updateDisplay() {
		Display.sync(FPS_CAP);
		Display.update();
		long currentFrameTime = getCurrentTime();
		timeDelta = (currentFrameTime - lastFrameTime) / 1000.0f; // seconds
		lastFrameTime = currentFrameTime;
	}
	
	public static void closeDisplay() {
		Display.destroy();
	}
	
	public static boolean displayShouldClose() {
		return Display.isCloseRequested();
	}
	
	public static float getFrameTimeSeconds() {
		return timeDelta;
	}
	
	private static long getCurrentTime() { // milliseconds
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}

}