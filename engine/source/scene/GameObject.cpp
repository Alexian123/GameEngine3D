#include "scene/GameObject.h"

#include <glm/gtc/matrix_transform.hpp>

namespace engine
{
	void GameObject::update(float deltaTime)
	{
		// Update components
		for (auto& component : components) {
			component->update(deltaTime);
		}

		// Update children
		for (auto it = children.begin(); it != children.end();) {
			if ((*it)->isAlive()) {
				(*it)->update(deltaTime);
				++it;
			}
			else {
				it = children.erase(it);
			}
		}
	}

	const std::string& GameObject::getName() const
	{
		return name;
	}

	void GameObject::setName(const std::string& name)
	{
		this->name = name;
	}

	GameObject* GameObject::getParent()
	{
		return parent;
	}

	bool GameObject::isAlive() const
	{
		return alive;
	}

	void GameObject::markForCleanup()
	{
		alive = false;
	}

	void GameObject::addComponent(Component* component)
	{
		components.emplace_back(component);
		component->owner = this;
	}

	const glm::vec3& GameObject::getPosition() const
	{
		return position;
	}

	void GameObject::setPosition(const glm::vec3& position)
	{
		this->position = position;
	}

	const glm::quat& GameObject::getRotation() const
	{
		return rotation;
	}

	void GameObject::setRotation(const glm::quat& rotation)
	{
		this->rotation = rotation;
	}

	const glm::vec3& GameObject::getScale() const
	{
		return scale;
	}

	void GameObject::setScale(const glm::vec3& scale)
	{
		this->scale = scale;
	}

	glm::mat4 GameObject::getLocalTransformMatrix() const
	{
		glm::mat4 mat = glm::mat4(1.0f); // Identity matrix

		mat = glm::translate(mat, position); // Translation

		mat = mat * glm::mat4_cast(rotation); // Quaternion rotation

		mat = glm::scale(mat, scale); // Scaling

		return mat;
	}

	glm::mat4 GameObject::getWorldTransformMatrix() const
	{
		if (parent) {
			return parent->getWorldTransformMatrix() * getLocalTransformMatrix();
		}
		else {
			return getLocalTransformMatrix();
		}
	}
}