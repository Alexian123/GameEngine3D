#include "core/Application.h"

namespace engine
{
	void Application::setShouldClose(bool value)
	{
		shouldClose = value;
	}

	bool Application::getShouldClose() const
	{
		return shouldClose;
	}
}