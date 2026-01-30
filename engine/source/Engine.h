#pragma once

#include <memory>
#include <chrono>

#include "input/InputManager.h"
#include "graphics/GraphicsAPI.h"

struct GLFWwindow;

namespace engine
{
	class Application;

	class Engine
	{
	private:
		std::unique_ptr<Application> application;
		std::chrono::steady_clock::time_point lastTimePoint;
		int windowWidth = 1280;
		int windowHeight = 720;
		GLFWwindow* window = nullptr;
		InputManager inputManager;
		GraphicsAPI graphicsAPI;

	public:
		static Engine& getInstance();

		bool init(int windowWidth, int windowHeight);
		void run();
		void cleanup();

		void setApplication(Application* app);
		Application* getApplication() const;

		InputManager& getInputManager();
		GraphicsAPI& getGraphicsAPI();

	private:
		static void keyCallback(GLFWwindow* window, int key, int scancode, int action, int mods);

		Engine() = default;
		~Engine() = default;
		Engine(const Engine&) = delete;
		Engine(Engine&&) = delete;
		Engine& operator=(const Engine&) = delete;
		Engine& operator=(Engine&&) = delete;
	};
}