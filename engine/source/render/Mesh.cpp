#include "Mesh.h"

#include "Engine.h"
#include "graphics/GraphicsAPI.h"

namespace engine
{
	Mesh::Mesh(const VertexLayout& layout, const std::vector<float>& vertices, const std::vector<uint32_t>& indices)
		: vertexLayout(layout)
	{
		auto& graphcisAPI = Engine::getInstance().getGraphicsAPI();
		vbo = graphcisAPI.createVertexBuffer(vertices);
		ebo = graphcisAPI.createIndexBuffer(indices);

		glGenVertexArrays(1, &vao);
		glBindVertexArray(vao);

		glBindBuffer(GL_ARRAY_BUFFER, vbo);

		for (auto& elem : layout.elements) {
			glVertexAttribPointer(elem.index, elem.size, elem.type, GL_FALSE, layout.stride, (void*)(uintptr_t)elem.offset);
			glEnableVertexAttribArray(elem.index);
		}

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);

		glBindVertexArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

		vertexCount = (vertices.size() * sizeof(float)) / layout.stride;
		indexCount = indices.size();
	}

	Mesh::Mesh(const VertexLayout& layout, const std::vector<float>& vertices) : vertexLayout(layout)
	{
		auto& graphcisAPI = Engine::getInstance().getGraphicsAPI();
		vbo = graphcisAPI.createVertexBuffer(vertices);

		glGenVertexArrays(1, &vao);
		glBindVertexArray(vao);

		glBindBuffer(GL_ARRAY_BUFFER, vbo);

		for (auto& elem : layout.elements) {
			glVertexAttribPointer(elem.index, elem.size, elem.type, GL_FALSE, layout.stride, (void*)(uintptr_t)elem.offset);
			glEnableVertexAttribArray(elem.index);
		}

		glBindVertexArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		vertexCount = (vertices.size() * sizeof(float)) / layout.stride;
	}

	Mesh::~Mesh()
	{
	}

	void Mesh::bind()
	{
		glBindVertexArray(vao);
	}

	void engine::Mesh::draw()
	{
		if (indexCount > 0) { // Draw with indices
			glDrawElements(GL_TRIANGLES, static_cast<GLsizei>(indexCount), GL_UNSIGNED_INT, 0);
		}
		else { // No indices, draw arrays
			glDrawArrays(GL_TRIANGLES, 0, static_cast<GLsizei>(vertexCount));
		}
	}
}