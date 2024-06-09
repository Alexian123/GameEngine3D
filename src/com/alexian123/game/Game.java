package com.alexian123.game;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;

import com.alexian123.entity.Camera;
import com.alexian123.font.GUIText;
import com.alexian123.loader.Loader;
import com.alexian123.rendering.DisplayManager;
import com.alexian123.rendering.RenderingManager;
import com.alexian123.rendering.Scene;
import com.alexian123.terrain.Water;
import com.alexian123.texture.GUITexture;
import com.alexian123.util.Clock;

public abstract class Game {

	protected final Loader loader = new Loader();
	protected final Clock clock = new Clock();
	protected final Matrix4f projectionMatrix;
	
	/**
	 * Creates a new game and spawns a new window with the desired properties
	 * 
	 * @param title
	 *            - the title of the window
	 * @param screenWidth
	 *            - the width of the window
	 * @param screenHeight
	 *            - the height of the window
	 */
	protected Game(String title, int screenWidth, int screenHeight) {
		DisplayManager.createDisplay(title, screenWidth, screenHeight);
		RenderingManager.init(loader, clock);
		projectionMatrix = RenderingManager.getProjectionMatrix();
	}
	
	/**
	 * Starts the main game loop
	 */
	public void run() {
		while (!DisplayManager.displayShouldClose()) {
			update();
			RenderingManager.render(getCurrentScene(), getCamera(), getGUI());
			DisplayManager.updateDisplay();
		}
		cleanup();
		DisplayManager.closeDisplay();
	}
	
	/**
	 *  Will be called before every render
 	 */
	protected abstract void update();
	
	/**
	 * 	@return The current scene to be rendered
	 */
	protected abstract Scene getCurrentScene();
	
	/**
	 * 	@return The camera
	 */
	protected abstract Camera getCamera();
	
	/**
	 * 	@return A list of GUI's to be rendered on the screen
	 */
	protected abstract List<GUITexture> getGUI();
	
	
	/**
	 * Add text to the screen
	 * 
	 * @param text
	 *            - the text to add
	 */
	protected void addText(GUIText text) {
		RenderingManager.loadText(text);
	}
	
	/**
	 * Removes text from the screen
	 * 
	 * @param text
	 *            - the text to remove
	 */
	protected void removeText(GUIText text) {
		RenderingManager.removeText(text);
	}
	
	/**
	 * Frees used memory
	 */
	private void cleanup() {
		loader.cleanup();
		RenderingManager.cleanup();
		Water.cleanup();
	}
}
