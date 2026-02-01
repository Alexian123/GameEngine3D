#include "Game.h"
#include "TestObject.h"

#include <iostream>

#define STB_IMAGE_IMPLEMENTATION
#include <stb_image.h>

bool Game::init()
{
	auto& fs = engine::Engine::getInstance().getFileSystem();
	auto path = fs.getAssetsDir() / "brick.png";

	int width, height, channels;
	unsigned char* data = stbi_load(path.string().c_str(), &width, &height, &channels, 0);

	if (data) {
		std::cout << "Image loaded" << std::endl;
	}

	scene = new engine::Scene();

	auto camera = scene->createObject("Camera");
	camera->addComponent(new engine::CameraComponent());
	camera->addComponent(new engine::PlayerControllerComponent());
	camera->setPosition(glm::vec3(0.0f, 0.0f, 2.0f));
	scene->setMainCamera(camera);

	scene->createObject<TestObject>("TestObject");

	std::string vertexShaderSource = R"(
		#version 330 core
		layout(location = 0) in vec3 position;
		layout(location = 1) in vec3 color;
		out vec3 vColor;
		uniform mat4 uModelMatrix;
		uniform mat4 uViewMatrix;
		uniform mat4 uProjectionMatrix;
		void main()
		{
			vColor = color;
			mat4 mvpMatrix = uProjectionMatrix * uViewMatrix * uModelMatrix;
			gl_Position = mvpMatrix * vec4(position, 1.0);
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

	auto material = std::make_shared<engine::Material>();
	material->setShaderProgram(shaderProgram);

	std::vector<float> vertices = {
		 0.5f,  0.5f,  0.5f,		1.0f, 0.0f, 0.0f,
		-0.5f,  0.5f,  0.5f,		0.0f, 1.0f, 0.0f,
		-0.5f, -0.5f,  0.5f,		0.0f, 0.0f, 1.0f,
		 0.5f, -0.5f,  0.5f,		1.0f, 1.0f, 0.0f,

		 0.5f,  0.5f, -0.5f,		1.0f, 0.0f, 0.0f,
		-0.5f,  0.5f, -0.5f,		0.0f, 1.0f, 0.0f,
		-0.5f, -0.5f, -0.5f,		0.0f, 0.0f, 1.0f,
		 0.5f, -0.5f, -0.5f,		1.0f, 1.0f, 0.0f,
	};

	std::vector<unsigned int> indices = {
		// Front face
		0, 1, 2,
		0, 2, 3,

		// Top face
		4, 5, 1,
		4, 1, 0,

		// Right face
		4, 0, 3,
		4, 3, 7,

		// Left face
		1, 5, 6,
		1, 6, 2,

		// Bottom face
		3, 2, 6,
		3, 6, 7,

		// Back face
		4, 7, 6,
		4, 6, 5,
	};

	engine::VertexLayout vertexLayout;

	// Position attribute
	vertexLayout.elements.push_back({ 0, 3, GL_FLOAT, 0 });

	// Color attribute
	vertexLayout.elements.push_back({ 1, 3, GL_FLOAT, sizeof(float) * 3 });

	vertexLayout.stride = sizeof(float) * 6; // 3 for position + 3 for color

	auto mesh = std::make_shared<engine::Mesh>(vertexLayout, vertices, indices);

	auto object1 = scene->createObject("Cube1");
	object1->addComponent(new engine::MeshComponent(material, mesh));
	object1->setPosition(glm::vec3(0.0f, 2.0f, 0.0f));

	auto object2 = scene->createObject("Cube2");
	object2->addComponent(new engine::MeshComponent(material, mesh));
	object2->setPosition(glm::vec3(0.0f, 2.0f, 2.0f));
	object2->setRotation(glm::vec3(0.0f, 2.0f, 0.0f));

	auto object3 = scene->createObject("Cube3");
	object3->addComponent(new engine::MeshComponent(material, mesh));
	object3->setPosition(glm::vec3(-2.0f, 0.0f, 0.0f));
	object3->setRotation(glm::vec3(1.0f, 0.0f, 1.0f));
	object3->setScale(glm::vec3(1.5f, 1.5f, 1.5f));

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