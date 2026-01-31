#pragma once

#include <GL/glew.h>

#include "graphics/VertexLayout.h"

namespace engine
{

	class Mesh
	{
	private:
		VertexLayout vertexLayout;
		size_t vertexCount = 0;
		size_t indexCount = 0;
		GLuint vao = 0;
		GLuint vbo = 0;
		GLuint ebo = 0;

	public:
		Mesh(const VertexLayout& layout, const std::vector<float>& vertices, const std::vector<uint32_t>& indices);
		Mesh(const VertexLayout& layout, const std::vector<float>& vertices);

		Mesh() = delete;
		Mesh(const Mesh&) = delete;
		Mesh(Mesh&&) = delete;
		Mesh& operator=(const Mesh&) = delete;
		Mesh& operator=(Mesh&&) = delete;

		~Mesh();

		void bind();
		void draw();
	};

}