#pragma once

#include <vector>

#include <glm/mat4x4.hpp>

namespace engine
{
	class Material;
	class Mesh;
	class GraphicsAPI;

	struct RenderCmd
	{
		Material* material = nullptr;
		Mesh* mesh = nullptr;
		glm::mat4 modelMatrix = glm::mat4(1.0f);
	};

	class RenderQueue
	{
	private:
		std::vector<RenderCmd> commands;

	public:
		void enqueue(const RenderCmd& cmd);
		void draw(GraphicsAPI& graphicsAPI);
	};
}