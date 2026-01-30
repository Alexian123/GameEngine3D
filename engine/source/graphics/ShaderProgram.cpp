#include "ShaderProgram.h"

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

}