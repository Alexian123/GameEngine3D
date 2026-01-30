#include "Game.h"

#include <iostream>

bool Game::init()
{
	return true;
}

void Game::update(float deltaTime)
{
	std::cout << "dt: " << deltaTime << " seconds" << std::endl;
}

void Game::cleanup()
{
}