#pragma once

#include <memory>
#include <chrono>

#include "input/InputManager.h"

struct GLFWwindow;

namespace engine
{
	class Application;

	class Engine
	{
	private:
		std::unique_ptr<Application> application;
		std::chrono::steady_clock::time_point lastTimePoint;
		int windowWidth;
		int windowHeight;
		GLFWwindow* window = nullptr;
		InputManager inputManager;

	public:
		static Engine& getInstance();

		bool init(int windowWidth, int windowHeight);
		void run();
		void cleanup();

		void setApplication(Application* app);
		Application* getApplication() const;

		InputManager& getInputManager();

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