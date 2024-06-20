package com.alexian123.main;

import com.alexian123.engine.GameManager;
import com.alexian123.test.TestGame;

public class Main {

	public static void main(String[] args) {
		GameManager.init(new TestGame());
		GameManager.run();
	}

}