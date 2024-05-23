package com.alexian123.test;

import com.alexian123.renderEngine.DisplayManager;

public class Main {

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		while (!DisplayManager.displayShouldClose()) {
			// logic
			// rendering
			DisplayManager.updateDisplay();
		}
		DisplayManager.closeDisplay();
	}

}