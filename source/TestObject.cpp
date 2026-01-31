#include "TestObject.h"

#include <vector>

#include <GLFW/glfw3.h>

TestObject::TestObject()
{
	std::string vertexShaderSource = R"(
		#version 330 core
		layout(location = 0) in vec3 position;
		layout(location = 1) in vec3 color;
		out vec3 vColor;
		uniform mat4 uModelMatrix;
		void main()
		{
			vColor = color;
			gl_Position = uModelMatrix * vec4(position, 1.0);
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

	mesh = std::make_shared<engine::Mesh>(vertexLayout, vertices, indices);
}

void TestObject::update(float deltaTime)
{
	engine::GameObject::update(deltaTime);

	auto& inputManager = engine::Engine::getInstance().getInputManager();
	auto position = getPosition();
	if (inputManager.getKeyPressState(GLFW_KEY_W)) {
		position.y += 0.0001f;
	}
	else if (inputManager.getKeyPressState(GLFW_KEY_A)) {
		position.x -= 0.0001f;
	}
	else if (inputManager.getKeyPressState(GLFW_KEY_S)) {
		position.y -= 0.0001f;
	}
	else if (inputManager.getKeyPressState(GLFW_KEY_D)) {
		position.x += 0.0001f;
	}
	setPosition(position);

	engine::RenderCmd cmd;
	cmd.material = &material;
	cmd.mesh = mesh.get();
	cmd.modelMatrix = getWorldTransformMatrix();

	auto& renderQueue = engine::Engine::getInstance().getRenderQueue();
	renderQueue.enqueue(cmd);
}