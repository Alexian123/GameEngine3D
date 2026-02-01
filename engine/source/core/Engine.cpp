#include "core/Engine.h"
#include "core/Application.h"
#include "scene/GameObject.h"
#include "scene/Component.h"
#include "scene/components/CameraComponent.h"

#include <iostream>

#include <GL/glew.h>
#include <GLFW/glfw3.h>

namespace engine
{
	Engine& Engine::getInstance()
	{
		static Engine instance;
		return instance;
	}

	bool Engine::init(int windowWidth, int windowHeight)
	{
		if (!application) {
			return false;
		}
		
		// Linux platform hint
#if defined (__linux__)
		glfwInitHint(GLFW_PLATFORM, GLFW_PLATFORM_X11);
#endif
		// Initialize GLFW
		if (!glfwInit()) {
			std::cerr << "Failed to initialize GLFW" << std::endl;
			return false;
		}

		// Set OpenGL version 3.3 core
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

		// Create window
		window = glfwCreateWindow(windowWidth, windowHeight, "GameEngine3D", nullptr, nullptr);
		if (!window) {
			std::cerr << "Failed to create GLFW window" << std::endl;
			glfwTerminate();
			return false;
		}
		this->windowHeight = windowHeight;
		this->windowWidth = windowWidth;

		// Set input callbacks
		glfwSetKeyCallback(window, keyCallback);
		glfwSetMouseButtonCallback(window, mouseButtonCallback);
		glfwSetCursorPosCallback(window, cursorPosCallback);

		glfwMakeContextCurrent(window);

		// Initialize GLEW
		if (glewInit() != GLEW_OK) {
			std::cerr << "Failed to initialize GLEW" << std::endl;
			glfwDestroyWindow(window);
			glfwTerminate();
			return false;
		}

		graphicsAPI.init();
		return application->init();
	}

	void Engine::run()
	{
		if (!application) {
			return;
		}
		lastTimePoint = std::chrono::high_resolution_clock::now();
		while (!glfwWindowShouldClose(window) && !application->getShouldClose()) {
			// Proccess events
			glfwPollEvents();

			// Update application
			auto currentTimePoint = std::chrono::high_resolution_clock::now();
			std::chrono::duration<float> deltaTime = currentTimePoint - lastTimePoint;
			lastTimePoint = currentTimePoint;
			application->update(deltaTime.count());

			// Update camera data
			CameraData camData;
			int width = 0, height = 0;
			glfwGetWindowSize(window, &width, &height);
			float aspectRatio = static_cast<float>(width) / static_cast<float>(height);
			if (currentScene) {
				auto camObj = currentScene->getMainCamera();
				if (camObj) {
					auto camComp = camObj->getComponent<CameraComponent>();
					if (camComp) {
						camData.viewMatrix = camComp->getViewMatrix();
						camData.projectionMatrix = camComp->getProjectionMatrix(aspectRatio);
					}
				}
			}

			// Render frame
			graphicsAPI.setClearColor(0.3f, 0.3f, 0.3f, 1.0f);
			graphicsAPI.clearBuffers();
			renderQueue.draw(graphicsAPI, camData);
			glfwSwapBuffers(window);

			// Update last mouse position
			inputManager.setLastMousePos(inputManager.getCurrentMousePos());
		}
	}

	void Engine::cleanup()
	{
		if (application) {
			application->cleanup();
			application.reset();
			glfwDestroyWindow(window);
			glfwTerminate();
			window = nullptr;
		}
	}

	void Engine::setApplication(Application* app)
	{
		application.reset(app);
	}

	Application* Engine::getApplication() const
	{
		return application.get();
	}

	InputManager& Engine::getInputManager()
	{
		return inputManager;
	}

	GraphicsAPI& Engine::getGraphicsAPI()
	{
		return graphicsAPI;
	}

	RenderQueue& Engine::getRenderQueue()
	{
		return renderQueue;
	}

	void Engine::setCurrentScene(Scene* scene)
	{
		currentScene.reset(scene);
	}

	Scene* Engine::getCurrentScene()
	{
		return currentScene.get();
	}

	FileSystem& Engine::getFileSystem()
	{
		return fileSystem;
	}

	void Engine::keyCallback(GLFWwindow* window, int key, int scancode, int action, int mods)
	{
		auto& inputManager = Engine::getInstance().getInputManager();
		if (action == GLFW_PRESS) {
			inputManager.setKeyPressState(key, true);
		} else if (action == GLFW_RELEASE) {
			inputManager.setKeyPressState(key, false);
		}
	}

	void Engine::mouseButtonCallback(GLFWwindow* window, int button, int action, int mods)
	{
		auto& inputManager = Engine::getInstance().getInputManager();
		if (action == GLFW_PRESS) {
			inputManager.setMouseBtnPressState(button, true);
		} else if (action == GLFW_RELEASE) {
			inputManager.setMouseBtnPressState(button, false);
		}
	}

	void Engine::cursorPosCallback(GLFWwindow* window, double xpos, double ypos)
	{
		auto& inputManager = Engine::getInstance().getInputManager();
		auto lastPos = inputManager.getCurrentMousePos();
		inputManager.setLastMousePos(lastPos);
		inputManager.setCurrentMousePos(glm::vec2(static_cast<float>(xpos), static_cast<float>(ypos)));
	}
}