#pragma once

#include <glm/mat4x4.hpp>

#include "scene/Component.h"

namespace engine
{
	class CameraComponent : public Component
	{
		COMPONENT_TYPE_ID_IMPL(CameraComponent)

	private:
		float fov = 60.0f;
		float nearPlane = 0.1f;
		float farPlane = 1000.0f;

	public:
		void update(float deltaTime) override;

		glm::mat4 getViewMatrix() const;
		glm::mat4 getProjectionMatrix(float aspectRatio) const;
	};
}