#include "scene/components/PlayerControllerComponent.h"
#include "Engine.h"
#include "input/InputManager.h"

#include <GLFW/glfw3.h>
#include <glm/gtc/matrix_transform.hpp>

namespace engine
{
	void PlayerControllerComponent::update(float deltaTime)
	{
		auto& inputManager = Engine::getInstance().getInputManager();
		auto rotation = owner->getRotation();

		if (inputManager.getMouseBtnPressState(GLFW_MOUSE_BUTTON_RIGHT)) {
			const auto& lastMosuePos = inputManager.getLastMousePos();
			const auto& currentMousePos = inputManager.getCurrentMousePos();

			float deltaX = currentMousePos.x - lastMosuePos.x;
			float deltaY = currentMousePos.y - lastMosuePos.y;

			// Rotation on Y axis (yaw)
			rotation.y -= deltaX * mouseSensitivity * deltaTime;

			// Rotation on X axis (pitch)
			rotation.x -= deltaY * mouseSensitivity * deltaTime;

			owner->setRotation(rotation);
		}

		glm::mat4 rotationMatrix(1.0f);
		rotationMatrix = glm::rotate(rotationMatrix, rotation.x, glm::vec3(1.0f, 0.0f, 0.0f)); // Rotate around X axis
		rotationMatrix = glm::rotate(rotationMatrix, rotation.y, glm::vec3(0.0f, 1.0f, 0.0f)); // Rotate around Y axis
		rotationMatrix = glm::rotate(rotationMatrix, rotation.z, glm::vec3(0.0f, 0.0f, 1.0f)); // Rotate around Z axis

		glm::vec3 forward = glm::normalize(glm::vec3(rotationMatrix * glm::vec4(0.0f, 0.0f, -1.0f, 0.0f)));
		glm::vec3 right = glm::normalize(glm::vec3(rotationMatrix * glm::vec4(1.0f, 0.0f, 0.0f, 0.0f)));
		glm::vec3 up = glm::normalize(glm::vec3(rotationMatrix * glm::vec4(0.0f, 1.0f, 0.0f, 0.0f)));

		auto position = owner->getPosition();
		if (inputManager.getKeyPressState(GLFW_KEY_W)) {
			position += forward * movementSpeed * deltaTime;
		}
		else if (inputManager.getKeyPressState(GLFW_KEY_A)) {
			position -= right * movementSpeed * deltaTime;
		}
		else if (inputManager.getKeyPressState(GLFW_KEY_S)) {
			position -= forward * movementSpeed * deltaTime;
		}
		else if (inputManager.getKeyPressState(GLFW_KEY_D)) {
			position += right * movementSpeed * deltaTime;
		}
		owner->setPosition(position);
	}
}