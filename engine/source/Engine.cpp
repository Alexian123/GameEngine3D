#include "Engine.h"
#include "Application.h"

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
#if defined (__LINUX__)
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

		glfwSetKeyCallback(window, keyCallback);
		glfwMakeContextCurrent(window);

		// Initialize GLEW
		if (glewInit() != GLEW_OK) {
			std::cerr << "Failed to initialize GLEW" << std::endl;
			glfwDestroyWindow(window);
			glfwTerminate();
			return false;
		}

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

			// Render frame
			glfwSwapBuffers(window);
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

	void Engine::keyCallback(GLFWwindow* window, int key, int scancode, int action, int mods)
	{
		auto& inputManager = Engine::getInstance().getInputManager();
		if (action == GLFW_PRESS) {
			inputManager.setKeyPressState(key, true);
		} else if (action == GLFW_RELEASE) {
			inputManager.setKeyPressState(key, false);
		}
	}
}