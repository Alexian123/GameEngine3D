#pragma once

#include <LibEngine.h>

class Game : public engine::Application
{
private:
	engine::Material material;
public:
	bool init() override;
	void update(float deltaTime) override; // seconds
	void cleanup() override;
};