#pragma once

#include <string>
#include <unordered_map>

#include <GL/glew.h>

namespace engine
{
	class ShaderProgram
	{
	private:
		GLuint programID = 0;
		std::unordered_map <std::string, GLint> uniformLocationCache;

	public:
		ShaderProgram() = delete;
		ShaderProgram(const ShaderProgram&) = delete;
		ShaderProgram& operator=(const ShaderProgram&) = delete;
		ShaderProgram(ShaderProgram&&) = delete;
		ShaderProgram& operator=(ShaderProgram&&) = delete;
		explicit ShaderProgram(GLuint id);

		~ShaderProgram();

		void bind() const;
		GLint getUniformLocation(const std::string& name);
		void setUniform(const std::string& name, float value);
	};
}