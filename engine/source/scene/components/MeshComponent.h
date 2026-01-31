#pragma once

#include <memory>

#include "scene/Component.h"

namespace engine
{
	class Material;
	class Mesh;

	class MeshComponent : public Component
	{
		COMPONENT_TYPE_ID_IMPL(MeshComponent)
	private:
		std::shared_ptr<Material> material;
		std::shared_ptr<Mesh> mesh;

	public:
		MeshComponent(const std::shared_ptr<Material>& material, const std::shared_ptr<Mesh>& mesh);
		void update(float deltaTime) override;
	};
}