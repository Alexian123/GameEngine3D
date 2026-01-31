#include "render/Material.h"
#include "graphics/ShaderProgram.h"

namespace engine
{
	void Material::setShaderProgram(const std::shared_ptr<ShaderProgram>& program)
	{
		shaderProgram = program;
	}

	void Material::setParameter(const std::string& name, float value)
	{
		floatParams[name] = value;
	}

	void Material::setParameter(const std::string& name, float v0, float v1)
	{
		vec2Params[name] = std::make_pair(v0, v1);
	}

	void Material::bind() const
	{
		if (shaderProgram) {
			shaderProgram->bind();
			for (const auto& param : floatParams) {
				shaderProgram->setUniform(param.first, param.second);
			}
			for (const auto& param : vec2Params) {
				const auto& vec2 = param.second;
				shaderProgram->setUniform(param.first, vec2.first, vec2.second);
			}
		}
	}
}