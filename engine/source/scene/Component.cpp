#include "scene/Component.h"

namespace engine
{
	size_t Component::nextId = 1;

	GameObject* Component::getOwner()
	{
		return owner;
	}
}