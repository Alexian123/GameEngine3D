#pragma once

#include <vector>
#include <cstdint>

#include <GL/glew.h>

namespace engine
{
	struct VertexElement
	{
		GLuint index;	 // Attribute location index
		GLuint size;	 // Number of components
		GLuint type;	 // Data type
		uint32_t offset; // Offset in bytes
	};

	struct VertexLayout
	{
		std::vector<VertexElement> elements;
		uint32_t stride = 0; // Total size of a vertex in bytes
	};
}