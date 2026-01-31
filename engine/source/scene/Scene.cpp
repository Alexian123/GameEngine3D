#include "scene/Scene.h"

namespace engine
{
	void Scene::update(float deltaTime)
	{
		for (auto it = objects.begin(); it != objects.end();) {
			if ((*it)->isAlive()) {
				(*it)->update(deltaTime);
				++it;
			}
			else {
				it = objects.erase(it);
			}
		}
	}

	void Scene::clear()
	{
		objects.clear();
	}

	GameObject* Scene::createObject(const std::string& name, GameObject* parent)
	{
		auto obj = new GameObject();
		obj->setName(name);
		setParent(obj, parent);
		return obj;
	}

	bool Scene::setParent(GameObject* obj, GameObject* parent)
	{
		if (!obj) {
			return false;
		}
		bool result = false;
		auto currentParent = obj->getParent();

		if (!parent) {
			if (currentParent) { // Object already has a parent
				auto it = std::find_if(
					currentParent->children.begin(),
					currentParent->children.end(),
					[obj](const std::unique_ptr<GameObject>& el) {
						return el.get() == obj;
					}
				);

				if (it != currentParent->children.end()) {
					objects.push_back(std::move(*it));
					obj->parent = nullptr;
					currentParent->children.erase(it);
					result = true;
				}
			}
			else { // Object does not have a parent
				auto it = std::find_if(
					objects.begin(),
					objects.end(),
					[obj](const std::unique_ptr<GameObject>& el) {
						return el.get() == obj;
					}
				);

				if (it == objects.end()) { // Object is new
					std::unique_ptr<GameObject> objHolder(obj);
					objects.push_back(std::move(objHolder));
					result = true;
				}
			}
		}
		else {
			if (currentParent) { // Object already has a parent
				auto it = std::find_if(
					currentParent->children.begin(),
					currentParent->children.end(),
					[obj](const std::unique_ptr<GameObject>& el) {
						return el.get() == obj;
					}
				);

				if (it != currentParent->children.end()) {
					// Ensure no circular parenting
					bool found = false;
					auto currentElem = parent;
					while (currentElem) {
						if (currentElem == obj) {
							found = true;
							break;
						}
						currentElem = currentElem->getParent();
					}

					if (!found) {
						parent->children.push_back(std::move(*it));
						obj->parent = parent;
						currentParent->children.erase(it);
						result = true;
					}
				}
			}
			else { // Object does not have a parent
				auto it = std::find_if(
					objects.begin(),
					objects.end(),
					[obj](const std::unique_ptr<GameObject>& el) {
						return el.get() == obj;
					}
				);

				if (it == objects.end()) { // Object is new
					std::unique_ptr<GameObject> objHolder(obj);
					parent->children.push_back(std::move(objHolder));
					obj->parent = parent;
					result = true;
				}
				else { // Object is in scene root, must check for circular parenting
					bool found = false;
					auto currentElem = parent;
					while (currentElem) {
						if (currentElem == obj) {
							found = true;
							break;
						}
						currentElem = currentElem->getParent();
					}

					if (!found) {
						parent->children.push_back(std::move(*it));
						obj->parent = parent;
						objects.erase(it);
						result = true;
					}
				}
			}
		}

		return result;
	}
}