#pragma once

#include <memory>

#include <LibEngine.h>

class TestObject : public engine::GameObject
{
private:

public:
	TestObject();

	void update(float deltaTime) override;
};