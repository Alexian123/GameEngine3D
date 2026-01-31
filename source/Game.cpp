#include "Game.h"

#include <iostream>

#include <GLFW/glfw3.h>

bool Game::init()
{
	std::string vertexShaderSource = R"(
		#version 330 core
		layout(location = 0) in vec3 position;
		layout(location = 1) in vec3 color;
		out vec3 vColor;
		uniform vec2 uOffset;
		void main()
		{
			vColor = color;
			gl_Position = vec4(position.xy + uOffset.xy, position.z, 1.0);
		}
	)";

	std::string fragmentShaderSource = R"(
		#version 330 core
		in vec3 vColor;
		out vec4 fragColor;
		uniform vec4 uColor;
		void main()
		{
			fragColor = vec4(vColor, 1.0) * uColor;
		}
	)";

	auto& graphicsAPI = engine::Engine::getInstance().getGraphicsAPI();
	auto shaderProgram = graphicsAPI.createShaderProgram(vertexShaderSource, fragmentShaderSource);
	material.setShaderProgram(shaderProgram);

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