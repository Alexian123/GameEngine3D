#include "scene/components/MeshComponent.h"
#include "render/Material.h"
#include "render/Mesh.h"
#include "render/RenderQueue.h"
#include "scene/GameObject.h"
#include "Engine.h"

namespace engine
{
	MeshComponent::MeshComponent(const std::shared_ptr<Material>& material, const std::shared_ptr<Mesh>& mesh)
		: material(material), mesh(mesh)
	{
	}

	void MeshComponent::update(float deltaTime)
	{
		if (material && mesh) {
			RenderCmd cmd;
			cmd.material = material.get();
			cmd.mesh = mesh.get();
			cmd.modelMatrix = getOwner()->getWorldTransformMatrix();

			auto& renderQueue = Engine::getInstance().getRenderQueue();
			renderQueue.enqueue(cmd);
		}
	}
}