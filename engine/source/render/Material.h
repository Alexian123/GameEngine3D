#pragma once

#include <memory>
#include <unordered_map>
#include <string>

namespace engine
{
	class ShaderProgram;

	class Material
	{
	private:
		std::shared_ptr<ShaderProgram> shaderProgram = nullptr;
		std::unordered_map<std::string, float> floatParams;
		std::unordered_map<std::string, std::pair<float, float>> vec2Params;

	public:
		void setShaderProgram(const std::shared_ptr<ShaderProgram>& program);
		ShaderProgram* getShaderProgram();

		void setParameter(const std::string& name, float value);
		void setParameter(const std::string& name, float v0, float v1);

		void bind() const;
	};
}