#include "scene/GameObject.h"

namespace engine
{
	void GameObject::update(float deltaTime)
	{
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
}