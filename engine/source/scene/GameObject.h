#pragma once

#include <string>
#include <vector>
#include <memory>

namespace engine
{
	class GameObject
	{
	private:
		std::string name;
		GameObject* parent = nullptr;
		std::vector<std::unique_ptr<GameObject>> children;
		bool alive = true;

	public:
		virtual ~GameObject() = default;

		virtual void update(float deltaTime);
		
		const std::string& getName() const;
		void setName(const std::string& name);
		GameObject* getParent();
		bool isAlive() const;

		void markForCleanup();

	protected:
		GameObject() = default;

	private:
		friend class Scene;
	};
}