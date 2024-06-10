package com.alexian123.game;

import java.util.List;

import com.alexian123.engine.GameManager;
import com.alexian123.entity.Camera;
import com.alexian123.loader.Loader;
import com.alexian123.texture.GUITexture;
import com.alexian123.util.Clock;
import com.alexian123.util.Scene;

public abstract class Game {
	
	protected Loader loader;
	protected Clock clock;
	
	private boolean running = true;
	
	/**
	 * Creates a new game
	 * 
	 */
	protected Game(Loader loader, Clock clock) {
		this.loader = loader;
		this.clock = clock;
		GameManager.init(this);
	}
	
	/**
	 * 	@return The loader
	 */
	public Loader getLoader() {
		return loader;
	}
	
	/**
	 * 	@return The game clock
	 */
	public Clock getClock() {
		return clock;
	}
	
	/**
	 * 	@return true if the game is running
	 */
	public boolean isRunning() {
		return running;
	}
	
	/**
	 *  Frees memory
 	 */
	public void cleanup() {
		loader.cleanup();
	}
	
	/**
	 *  Will be called before every render
 	 */
	public abstract void update();
	
	/**
	 * 	@return The current scene to be rendered
	 */
	public abstract Scene getCurrentScene();
	
	/**
	 * 	@return The camera
	 */
	public abstract Camera getCamera();
	
	/**
	 * 	@return A list of GUI's to be rendered on the screen
	 */
	public abstract List<GUITexture> getGUI();
	
	/**
	 *  Stops the game when called
 	 */
	protected void stop() {
		running = false;
	}
}
