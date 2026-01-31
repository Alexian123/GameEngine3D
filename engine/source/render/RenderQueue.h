#pragma once

#include <vector>

namespace engine
{
	class Material;
	class Mesh;
	class GraphicsAPI;

	struct RenderCmd
	{
		Material* material = nullptr;
		Mesh* mesh = nullptr;
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