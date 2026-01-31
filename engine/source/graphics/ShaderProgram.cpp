#include "graphics/ShaderProgram.h"

#include <glm/gtc/type_ptr.hpp>

namespace engine
{
	ShaderProgram::ShaderProgram(GLuint id) : programID(id)
	{
	}

	ShaderProgram::~ShaderProgram()
	{
		glDeleteProgram(programID);
	}

	void ShaderProgram::bind() const
	{
		glUseProgram(programID);
	}

	GLint ShaderProgram::getUniformLocation(const std::string& name)
	{
		auto it = uniformLocationCache.find(name);
		if (it != uniformLocationCache.end()) { // Found in cache
			return it->second;
		}
		GLint location = glGetUniformLocation(programID, name.c_str());
		uniformLocationCache[name] = location; // Cache it
		return location;
	}

	void ShaderProgram::setUniform(const std::string& name, float value)
	{
		GLint location = getUniformLocation(name);
		glUniform1f(location, value);
	}

	void ShaderProgram::setUniform(const std::string& name, float v0, float v1)
	{
		GLint location = getUniformLocation(name);
		glUniform2f(location, v0, v1);
	}

	void ShaderProgram::setUniform(const std::string& name, const glm::mat4& mat)
	{
		auto location = getUniformLocation(name);
		glUniformMatrix4fv(location, 1, GL_FALSE, glm::value_ptr(mat));
	}
}