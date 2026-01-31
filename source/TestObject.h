#pragma once

#include <memory>

#include <LibEngine.h>

class TestObject : public engine::GameObject
{
private:
	engine::Material material;
	std::shared_ptr<engine::Mesh> mesh;

public:
	TestObject();

	void update(float deltaTime) override;
};