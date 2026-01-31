#include "input/InputManager.h"

namespace engine
{

	void InputManager::setKeyPressState(int key, bool pressed)
	{
		if (key >= 0 && key < static_cast<int>(keyPressStates.size())) {
			keyPressStates[key] = pressed;
		}
	}

	bool InputManager::getKeyPressState(int key) const
	{
		if (key >= 0 && key < static_cast<int>(keyPressStates.size())) {
			return keyPressStates[key];
		}
		return false;
	}

	void InputManager::setMouseBtnPressState(int button, bool pressed)
	{
		if (button >= 0 && button < static_cast<int>(mouseBtnPressStates.size())) {
			mouseBtnPressStates[button] = pressed;
		}
	}

	bool InputManager::getMouseBtnPressState(int button) const
	{
		if (button >= 0 && button < static_cast<int>(mouseBtnPressStates.size())) {
			return mouseBtnPressStates[button];
		}
		return false;
	}

	void InputManager::setLastMousePos(const glm::vec2& pos)
	{
		lastMousePos = pos;
	}

	const glm::vec2& InputManager::getLastMousePos() const
	{
		return lastMousePos;
	}

	void InputManager::setCurrentMousePos(const glm::vec2& pos)
	{
		currentMousePos = pos;
	}

	const glm::vec2& InputManager::getCurrentMousePos() const
	{
		return currentMousePos;
	}
}