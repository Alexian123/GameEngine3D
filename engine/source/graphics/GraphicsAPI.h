#pragma once

#include <memory>
#include <string>
#include <vector>

#include <GL/glew.h>

namespace engine
{
	class ShaderProgram;
	class Material;
	class Mesh;

	class GraphicsAPI
	{
	public:
		std::shared_ptr<ShaderProgram> createShaderProgram(
			const std::string& vertexShaderSource, 
			const std::string& fragmentShaderSource
		);

		GLuint createVertexBuffer(const std::vector<float>& vertices);
		GLuint createIndexBuffer(const std::vector<uint32_t>& indices);

		void setClearColor(float r, float g, float b, float a);
		void clearBuffer();

		void bindShaderProgram(ShaderProgram* shaderProgram);
		void bindMaterial(Material* material);
		void bindMesh(Mesh* mesh);
		void drawMesh(Mesh* mesh);
	};
}