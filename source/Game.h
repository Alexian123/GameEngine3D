#pragma once

#include <LibEngine.h>

class Game : public engine::Application
{
public:
	bool init() override;
	void update(float deltaTime) override; // seconds
	void cleanup() override;
};