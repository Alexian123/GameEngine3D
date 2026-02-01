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
		glm::mat4 mat = glm::mat4(1.0f); // Identity matrix

		// Apply rotation
		mat = glm::mat4_cast(owner->getRotation()) * mat;

		// Apply translation
		mat[3] = glm::vec4(owner->getPosition(), 1.0f);

		// If the owner has a parent, apply the parent's world transform
		if (owner->getParent()) {
			mat = owner->getParent()->getWorldTransformMatrix() * mat;
		}

		return glm::inverse(mat); // Invert the matrix to get the view matrix
	}

	glm::mat4 engine::CameraComponent::getProjectionMatrix(float aspectRatio) const
	{
		return glm::perspective(glm::radians(fov), aspectRatio, nearPlane, farPlane);
	}
}