#include "scene/Component.h"

namespace engine
{
	GameObject* Component::getOwner()
	{
		return owner;
	}
}