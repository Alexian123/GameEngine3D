#pragma once

#include <memory>
#include <string>

#include <GL/glew.h>

namespace engine
{
	class ShaderProgram;

	class GraphicsAPI
	{
	public:
		std::shared_ptr<ShaderProgram> createShaderProgram(
			const std::string& vertexShaderSource, 
			const std::string& fragmentShaderSource
		);

		void bindShaderProgram(ShaderProgram* shaderProgram);
	};
}