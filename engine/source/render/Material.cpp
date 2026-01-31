#include "Material.h"
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

	void Material::bind() const
	{
		if (shaderProgram) {
			shaderProgram->bind();
			for (const auto& param : floatParams) {
				shaderProgram->setUniform(param.first, param.second);
			}
		}
	}
}