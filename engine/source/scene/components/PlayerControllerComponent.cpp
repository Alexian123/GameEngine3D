#include "scene/components/PlayerControllerComponent.h"
#include "core/Engine.h"
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
			float yAngle = -deltaX * mouseSensitivity * deltaTime;
			glm::quat yRot = glm::angleAxis(yAngle, glm::vec3(0.0f, 1.0f, 0.0f));

			// Rotation on X axis (pitch)
			float xAngle = -deltaY * mouseSensitivity * deltaTime;
			glm::vec3 right = rotation * glm::vec3(1.0f, 0.0f, 0.0f);
			glm::quat xRot = glm::angleAxis(xAngle, right);

			// Apply combined rotation
			glm::quat deltaRot = yRot * xRot;
			rotation = glm::normalize(deltaRot * rotation);

			owner->setRotation(rotation);
		}

		glm::vec3 forward = rotation * glm::vec3(0.0f, 0.0f, -1.0f);
		glm::vec3 right = rotation * glm::vec3(1.0f, 0.0f, 0.0f);

		auto position = owner->getPosition();
		if (inputManager.getKeyPressState(GLFW_KEY_W)) {
			position += forward * movementSpeed * deltaTime;
		}
		if (inputManager.getKeyPressState(GLFW_KEY_A)) {
			position -= right * movementSpeed * deltaTime;
		}
		if (inputManager.getKeyPressState(GLFW_KEY_S)) {
			position -= forward * movementSpeed * deltaTime;
		}
		if (inputManager.getKeyPressState(GLFW_KEY_D)) {
			position += right * movementSpeed * deltaTime;
		}
		if (inputManager.getKeyPressState(GLFW_KEY_SPACE)) {
			position.y += movementSpeed * deltaTime;
		}
		if (inputManager.getKeyPressState(GLFW_KEY_LEFT_SHIFT)) {
			position.y -= movementSpeed * deltaTime;
		}
		owner->setPosition(position);
	}
}