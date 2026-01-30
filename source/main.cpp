#include <GLFW/glfw3.h>
#include <iostream>

int main()
{
#if defined (__LINUX__)
	glfwInitHint(GLFW_PLATFORM, GLFW_PLATFORM_X11);
#endif
	if (!glfwInit()) {
		std::cerr << "Failed to initialize GLFW" << std::endl;
		return -1;
	}

	GLFWwindow *window = glfwCreateWindow(1280, 720, "GameEngine3D", nullptr, nullptr);
	if (!window) {
		std::cerr << "Failed to create GLFW window" << std::endl;
		glfwTerminate();
		return -1;
	}

	// Main loop
	while (!glfwWindowShouldClose(window)) {
		glfwPollEvents();
	}

	glfwDestroyWindow(window);
	glfwTerminate();
    return 0;
}