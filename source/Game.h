#pragma once

#include <memory>

#include <LibEngine.h>

class Game : public engine::Application
{
private:
	engine::Scene* scene = nullptr;

public:
	bool init() override;
	void update(float deltaTime) override; // seconds
	void cleanup() override;
};