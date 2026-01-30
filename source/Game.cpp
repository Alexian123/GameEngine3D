#include "Game.h"

#include <iostream>

#include <GLFW/glfw3.h>

bool Game::init()
{
	return true;
}

void Game::update(float deltaTime)
{
	auto& inputManager = engine::Engine::getInstance().getInputManager();
	if (inputManager.getKeyPressState(GLFW_KEY_ESCAPE)) {
		setShouldClose(true);
	}
}

void Game::cleanup()
{
}