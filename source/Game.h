#pragma once

#include <memory>

#include <LibEngine.h>

class Game : public engine::Application
{
private:
	engine::Material material;
	std::unique_ptr<engine::Mesh> mesh;
public:
	bool init() override;
	void update(float deltaTime) override; // seconds
	void cleanup() override;
};