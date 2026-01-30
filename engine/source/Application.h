#pragma once

namespace engine
{
	class Application
	{
	private:
		bool shouldClose = false;

	public:
		virtual bool init() = 0;
		virtual void update(float deltaTime) = 0; // seconds
		virtual void cleanup() = 0;

		void setShouldClose(bool value);
		bool getShouldClose() const;
	};
}