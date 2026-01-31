#include "render/RenderQueue.h"
#include "render/Material.h"
#include "render/Mesh.h"
#include "graphics/GraphicsAPI.h"
#include "graphics/ShaderProgram.h"

namespace engine
{
	void RenderQueue::enqueue(const RenderCmd& cmd)
	{
		commands.push_back(cmd);
	}

	void RenderQueue::draw(GraphicsAPI& graphicsAPI)
	{
		for (const auto& cmd : commands) {
			if (cmd.material && cmd.mesh) {
				graphicsAPI.bindMaterial(cmd.material);
				auto shaderProgram = cmd.material->getShaderProgram();
				if (shaderProgram) {
					shaderProgram->setUniform("uModelMatrix", cmd.modelMatrix);
				}
				graphicsAPI.bindMesh(cmd.mesh);
				graphicsAPI.drawMesh(cmd.mesh);
			}
		}
		commands.clear();
	}
}