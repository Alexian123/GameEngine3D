#pragma once

#include <memory>
#include <string>
#include <vector>

#include <GL/glew.h>

namespace engine
{
	class ShaderProgram;
	class Material;

	class GraphicsAPI
	{
	public:
		std::shared_ptr<ShaderProgram> createShaderProgram(
			const std::string& vertexShaderSource, 
			const std::string& fragmentShaderSource
		);

		GLuint createVertexBuffer(const std::vector<float>& vertices);
		GLuint createIndexBuffer(const std::vector<uint32_t>& indices);


		void bindShaderProgram(ShaderProgram* shaderProgram);
		void bindMaterial(Material* material);
	};
}