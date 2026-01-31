#include "Game.h"

#include <iostream>
#include <vector>

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
		void main()
		{
			fragColor = vec4(vColor, 1.0);
		}
	)";

	auto& graphicsAPI = engine::Engine::getInstance().getGraphicsAPI();
	auto shaderProgram = graphicsAPI.createShaderProgram(vertexShaderSource, fragmentShaderSource);
	material.setShaderProgram(shaderProgram);

	std::vector<float> vertices = {
		 0.5f,  0.5f, 0.0f,		1.0f, 0.0f, 0.0f,
		-0.5f,  0.5f, 0.0f,		0.0f, 1.0f, 0.0f,
		-0.5f, -0.5f, 0.0f,		0.0f, 0.0f, 1.0f,
		 0.5f, -0.5f, 0.0f,		1.0f, 1.0f, 0.0f,
	};

	std::vector<unsigned int> indices = {
		0, 1, 2,
		0, 2, 3
	};

	engine::VertexLayout vertexLayout;

	// Position attribute
	vertexLayout.elements.push_back({ 0, 3, GL_FLOAT, 0 });

	// Color attribute
	vertexLayout.elements.push_back({ 1, 3, GL_FLOAT, sizeof(float) * 3 });

	vertexLayout.stride = sizeof(float) * 6; // 3 for position + 3 for color

	mesh = std::make_unique<engine::Mesh>(vertexLayout, vertices, indices);

	return true;
}

void Game::update(float deltaTime)
{
	auto& inputManager = engine::Engine::getInstance().getInputManager();

	if (inputManager.getKeyPressState(GLFW_KEY_ESCAPE)) {
		setShouldClose(true);
	}
	else if (inputManager.getKeyPressState(GLFW_KEY_W)) {
		offsetY += 0.0001f;
	}
	else if (inputManager.getKeyPressState(GLFW_KEY_A)) {
		offsetX -= 0.0001f;
	}
	else if (inputManager.getKeyPressState(GLFW_KEY_S)) {
		offsetY -= 0.0001f;
	}
	else if (inputManager.getKeyPressState(GLFW_KEY_D)) {
		offsetX += 0.0001f;
	}

	material.setParameter("uOffset", offsetX, offsetY);

	engine::RenderCmd cmd;
	cmd.material = &material;
	cmd.mesh = mesh.get();

	auto& renderQueue = engine::Engine::getInstance().getRenderQueue();
	renderQueue.enqueue(cmd);
}

void Game::cleanup()
{
}