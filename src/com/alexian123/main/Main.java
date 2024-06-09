package com.alexian123.main;

import com.alexian123.game.Game;
import com.alexian123.game.TestGame;

public class Main {

	public static void main(String[] args) {
		Game game = new TestGame("GameEngine3D", 1600, 900);
		game.run();
	}

}