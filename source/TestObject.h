#pragma once

#include <memory>

#include <LibEngine.h>

class TestObject : public engine::GameObject
{
private:
	engine::Material material;
	std::shared_ptr<engine::Mesh> mesh;
	float offsetX = 0.0f;
	float offsetY = 0.0f;

public:
	TestObject();

	void update(float deltaTime) override;
};