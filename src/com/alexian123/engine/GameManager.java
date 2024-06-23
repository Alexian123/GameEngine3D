package com.alexian123.engine;

import org.lwjgl.Sys;

import com.alexian123.game.Settings;
import com.alexian123.game.Game;

public class GameManager {
	
	public static final Settings SETTINGS = Settings.importFrom("config.ini");

	private static Game game;
	
	private static long lastFrameTime;
	private static float timeDelta;
	
	private static boolean isInitialized = false;
	
	public static void init(Game game) {
		if (!isInitialized) {
			GameManager.game = game;
			DisplayManager.init(SETTINGS.windowTitle, SETTINGS.screenWidth, SETTINGS.screenHeight);
			RenderingManager.init(game.getLoader(), game.getClock(), game.getCamera());
			lastFrameTime = getCurrentTime();
			isInitialized = false;
		}
	}
	
	public static void run() {	
		while (!DisplayManager.displayShouldClose() && game.isRunning()) {
			game.update();
			RenderingManager.renderScene(game.getCurrentScene(), game.getCamera(), game.getGUI());	
			DisplayManager.updateDisplay();
			updateFrameTime();
		}
		game.cleanup();
		RenderingManager.cleanup();
		DisplayManager.closeDisplay();
	}
	
	public static float getFrameTimeSeconds() {
		return timeDelta;
	}
	
	private static long getCurrentTime() { // milliseconds
		return Sys.getTime() * (int) SETTINGS.millisecondsPerSecond / Sys.getTimerResolution();
	}
	
	private static void updateFrameTime() {
		long currentFrameTime = getCurrentTime();
		timeDelta = (currentFrameTime - lastFrameTime) / SETTINGS.millisecondsPerSecond;  // seconds
		lastFrameTime = currentFrameTime;
	}
}
