#include "Engine.h"
#include "Application.h"

namespace engine
{
	bool Engine::init()
	{
		if (!application) {
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
		while (!application->getShouldClose()) {
			auto currentTimePoint = std::chrono::high_resolution_clock::now();
			std::chrono::duration<float> deltaTime = currentTimePoint - lastTimePoint;
			lastTimePoint = currentTimePoint;
			application->update(deltaTime.count());
		}
	}

	void Engine::cleanup()
	{
		if (application) {
			application->cleanup();
			application.reset();
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
}