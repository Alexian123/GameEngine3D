#include "GraphicsAPI.h"
#include "ShaderProgram.h"
#include "render/Material.h"

#include <iostream>

namespace engine
{
	std::shared_ptr<ShaderProgram> GraphicsAPI::createShaderProgram(
		const std::string& vertexShaderSource, 
		const std::string& fragmentShaderSource
	)
	{
		GLuint vertexShader = glCreateShader(GL_VERTEX_SHADER);
		const char* vertexSourceCStr = vertexShaderSource.c_str();
		glShaderSource(vertexShader, 1, &vertexSourceCStr, nullptr);
		glCompileShader(vertexShader);

		GLint success;
		glGetShaderiv(vertexShader, GL_COMPILE_STATUS, &success);
		if (!success) {
			char infoLog[512];
			glGetShaderInfoLog(vertexShader, 512, nullptr, infoLog);
			std::cerr << "ERROR::SHADER::VERTEX::COMPILATION_FAILED: " << infoLog << std::endl;
			return nullptr;
		}

		GLuint fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		const char* fragmentSourceCStr = fragmentShaderSource.c_str();
		glShaderSource(fragmentShader, 1, &fragmentSourceCStr, nullptr);
		glCompileShader(fragmentShader);

		glGetShaderiv(fragmentShader, GL_COMPILE_STATUS, &success);
		if (!success) {
			char infoLog[512];
			glGetShaderInfoLog(fragmentShader, 512, nullptr, infoLog);
			std::cerr << "ERROR::SHADER::FRAGMENT::COMPILATION_FAILED: " << infoLog << std::endl;
			glDeleteShader(vertexShader);
			return nullptr;
		}

		GLuint shaderProgramID = glCreateProgram();
		glAttachShader(shaderProgramID, vertexShader);
		glAttachShader(shaderProgramID, fragmentShader);
		glLinkProgram(shaderProgramID);

		glGetProgramiv(shaderProgramID, GL_LINK_STATUS, &success);
		if (!success) {
			char infoLog[512];
			glGetProgramInfoLog(shaderProgramID, 512, nullptr, infoLog);
			std::cerr << "ERROR::SHADER::PROGRAM::LINKING_FAILED: " << infoLog << std::endl;
			glDeleteShader(vertexShader);
			glDeleteShader(fragmentShader);
			return nullptr;
		}

		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader);
		return std::make_shared<ShaderProgram>(shaderProgramID);
	}

	void GraphicsAPI::bindShaderProgram(ShaderProgram* shaderProgram)
	{
		if (shaderProgram) {
			shaderProgram->bind();
		}
	}

	void engine::GraphicsAPI::bindMaterial(Material* material)
	{
		if (material) {
			material->bind();
		}
	}
}