#include "scene/components/CameraComponent.h"
#include "scene/GameObject.h"

#include <glm/gtc/matrix_transform.hpp>

namespace engine
{
	void CameraComponent::update(float deltaTime) 
	{

	}

	glm::mat4 engine::CameraComponent::getViewMatrix() const
	{
		return glm::inverse(owner->getWorldTransformMatrix());
	}

	glm::mat4 engine::CameraComponent::getProjectionMatrix(float aspectRatio) const
	{
		return glm::perspective(glm::radians(fov), aspectRatio, nearPlane, farPlane);
	}
}