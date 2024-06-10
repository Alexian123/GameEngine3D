package com.alexian123.engine;

import org.lwjgl.Sys;

import com.alexian123.game.Game;
import com.alexian123.util.Constants;

public class GameManager {

	private static Game game;
	
	private static long lastFrameTime;
	private static float timeDelta;
	
	private static boolean isInitialized = false;
	
	public static void init(Game game) {
		if (!isInitialized) {
			GameManager.game = game;
			DisplayManager.init(Constants.DEFAULT_WINDOW_TITLE, Constants.DEFAULT_SCREEN_WIDTH, Constants.DEFAULT_SCREEN_HEIGHT);
			RenderingManager.init(game.getLoader(), game.getClock());
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
		return Sys.getTime() * (int) Constants.MILLISECONDS_PER_SECOND / Sys.getTimerResolution();
	}
	
	private static void updateFrameTime() {
		long currentFrameTime = getCurrentTime();
		timeDelta = (currentFrameTime - lastFrameTime) / Constants.MILLISECONDS_PER_SECOND;  // seconds
		lastFrameTime = currentFrameTime;
	}
}
