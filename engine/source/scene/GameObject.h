#pragma once

#include <string>
#include <vector>
#include <memory>

#include <glm/vec3.hpp>
#include <glm/mat4x4.hpp>

#include "scene/Component.h"

namespace engine
{
	class GameObject
	{
	private:
		std::string name;
		GameObject* parent = nullptr;
		std::vector<std::unique_ptr<GameObject>> children;
		std::vector<std::unique_ptr<Component>> components;
		bool alive = true;

		// Transform components
		glm::vec3 position = glm::vec3(0.0f);
		glm::vec3 rotation = glm::vec3(0.0f);
		glm::vec3 scale = glm::vec3(1.0f);

	public:
		virtual ~GameObject() = default;

		virtual void update(float deltaTime);
		
		const std::string& getName() const;
		void setName(const std::string& name);
		GameObject* getParent();
		bool isAlive() const;

		void markForCleanup();

		void addComponent(Component* component);

		const glm::vec3& getPosition() const;
		void setPosition(const glm::vec3& position);

		const glm::vec3& getRotation() const;
		void setRotation(const glm::vec3& rotation);

		const glm::vec3& getScale() const;
		void setScale(const glm::vec3& scale);

		glm::mat4 getLocalTransformMatrix() const;
		glm::mat4 getWorldTransformMatrix() const;

	protected:
		GameObject() = default;

	private:
		friend class Scene;
	};
}