#pragma once

#include <memory>
#include <chrono>

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

	public:
		bool init(int windowWidth, int windowHeight);
		void run();
		void cleanup();

		void setApplication(Application* app);
		Application* getApplication() const;
	};
}