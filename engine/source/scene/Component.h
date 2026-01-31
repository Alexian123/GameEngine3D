#pragma once

namespace engine
{
	class GameObject;

	class Component
	{
	protected:
		GameObject* owner = nullptr;

	public:
		virtual ~Component() = default;
		virtual void update(float deltaTime) = 0;

		GameObject* getOwner();

	private:
		friend class GameObject;
	};
}