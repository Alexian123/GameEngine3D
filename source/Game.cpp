#include "Game.h"
#include "TestObject.h"

bool Game::init()
{
	scene.createObject<TestObject>("TestObject1");
	return true;
}

void Game::update(float deltaTime)
{
	scene.update(deltaTime);
}

void Game::cleanup()
{
}