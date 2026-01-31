#pragma once

#include <array>

#include <glm/vec2.hpp>

namespace engine
{

	class InputManager
	{
	private:
		std::array<bool, 349> keyPressStates = { false }; // GLFW_KEY_LAST is 348
		std::array<bool, 8> mouseBtnPressStates = { false };
		glm::vec2 lastMousePos = glm::vec2(0.0f);
		glm::vec2 currentMousePos = glm::vec2(0.0f);

	public:
		void setKeyPressState(int key, bool pressed);
		bool getKeyPressState(int key) const;

		void setMouseBtnPressState(int button, bool pressed);
		bool getMouseBtnPressState(int button) const;

		void setLastMousePos(const glm::vec2& pos);
		const glm::vec2& getLastMousePos() const;

		void setCurrentMousePos(const glm::vec2& pos);
		const glm::vec2& getCurrentMousePos() const;

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