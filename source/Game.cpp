#include "Game.h"
#include "TestObject.h"

bool Game::init()
{
	scene = new engine::Scene();

	auto camera = scene->createObject("Camera");
	camera->addComponent(new engine::CameraComponent());
	camera->addComponent(new engine::PlayerControllerComponent());
	camera->setPosition(glm::vec3(0.0f, 0.0f, 2.0f));
	scene->setMainCamera(camera);

	scene->createObject<TestObject>("TestObject1");

	engine::Engine::getInstance().setCurrentScene(scene);

	return true;
}

void Game::update(float deltaTime)
{
	scene->update(deltaTime);
}

void Game::cleanup()
{
}