#pragma once

#include <memory>
#include <chrono>

namespace engine
{
	class Application;

	class Engine
	{
	private:
		std::unique_ptr<Application> application;
		std::chrono::steady_clock::time_point lastTimePoint;

	public:
		bool init();
		void run();
		void cleanup();

		void setApplication(Application* app);
		Application* getApplication() const;
	};
}