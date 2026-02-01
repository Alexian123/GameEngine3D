#pragma once

#include "scene/Component.h"

namespace engine
{
	class PlayerControllerComponent : public Component
	{
		COMPONENT_TYPE_ID_IMPL(PlayerControllerComponent)

	private:
		float mouseSensitivity = 2.5f;
		float movementSpeed = 1.0f;

	public:
		void update(float deltaTime) override;
	};
}