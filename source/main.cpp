#include "Game.h"

int main()
{
	Game* game = new Game();
	engine::Engine engine;

	engine.setApplication(game);

	if (engine.init()) {
		engine.run();
	}

	engine.cleanup();
	return 0;
}