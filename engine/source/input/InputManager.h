#pragma once

#include <array>

namespace engine
{

	class InputManager
	{
	private:
		std::array<bool, 349> keyPressStates = { false }; // GLFW_KEY_LAST is 348

	public:
		void setKeyPressState(int key, bool pressed);
		bool getKeyPressState(int key) const;

	private:
		InputManager() = default;
		~InputManager() = default;
		InputManager(const InputManager&) = delete;
		InputManager(InputManager&&) = delete;
		InputManager& operator=(const InputManager&) = delete;
		InputManager& operator=(InputManager&&) = delete;

	private:
		friend class Engine;
	};

}