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

	public:
		void setShaderProgram(const std::shared_ptr<ShaderProgram>& program); 
		void setParameter(const std::string& name, float value);

		void bind() const;
	};
}