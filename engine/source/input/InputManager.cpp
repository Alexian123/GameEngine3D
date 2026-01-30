#include "InputManager.h"

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
}