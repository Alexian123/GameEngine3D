#include "graphics/GraphicsAPI.h"
#include "graphics/ShaderProgram.h"
#include "render/Material.h"
#include "render/Mesh.h"

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

	GLuint GraphicsAPI::createVertexBuffer(const std::vector<float>& vertices)
	{
		GLuint vbo = 0;
		glGenBuffers(1, &vbo);
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, vertices.size() * sizeof(float), vertices.data(), GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0); // Unbind VBO
		return vbo;
	}

	GLuint GraphicsAPI::createIndexBuffer(const std::vector<uint32_t>& indices)
	{
		GLuint ebo = 0;
		glGenBuffers(1, &ebo);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices.size() * sizeof(uint32_t), indices.data(), GL_STATIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0); // Unbind EBO
		return ebo;
	}

	void GraphicsAPI::setClearColor(float r, float g, float b, float a)
	{
		glClearColor(r, g, b, a);
	}

	void GraphicsAPI::clearBuffer()
	{
		glClear(GL_COLOR_BUFFER_BIT);
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

	void engine::GraphicsAPI::bindMesh(Mesh* mesh)
	{
		if (mesh) {
			mesh->bind();
		}
	}

	void engine::GraphicsAPI::drawMesh(Mesh* mesh)
	{
		if (mesh) {
			mesh->draw();
		}
	}
}