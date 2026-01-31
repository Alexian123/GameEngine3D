#pragma once

#include <vector>
#include <string>
#include <memory>

#include "scene/GameObject.h"

namespace engine
{
	class Scene
	{
	private:
		std::vector<std::unique_ptr<GameObject>> objects;

	public:
		void update(float deltaTime);
		void clear();

		GameObject* createObject(const std::string& name, GameObject* parent = nullptr);

		template<typename T, typename = typename std::enable_if_t<std::is_base_of_v<GameObject, T>>>
		T* createObject(const std::string& name, GameObject* parent = nullptr)
		{
			auto obj = new T();
			obj->setName(name);
			setParent(obj, parent);
			return obj;
		}

		bool setParent(GameObject* obj, GameObject* parent);
	};
}