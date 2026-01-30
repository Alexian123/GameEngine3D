#include <GL/glew.h>
#include <GLFW/glfw3.h>
#include <iostream>
#include <vector>
#include <string>

int main()
{
#if defined (__LINUX__)
	glfwInitHint(GLFW_PLATFORM, GLFW_PLATFORM_X11);
#endif
	if (!glfwInit()) {
		std::cerr << "Failed to initialize GLFW" << std::endl;
		return -1;
	}

	// OpenGL version 3.3 core
	glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
	glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
	glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

	GLFWwindow *window = glfwCreateWindow(1280, 720, "GameEngine3D", nullptr, nullptr);
	if (!window) {
		std::cerr << "Failed to create GLFW window" << std::endl;
		glfwTerminate();
		return -1;
	}

	glfwMakeContextCurrent(window);

	// Initialize GLEW
	if (glewInit() != GLEW_OK) {
		std::cerr << "Failed to initialize GLEW" << std::endl;
		glfwDestroyWindow(window);
		glfwTerminate();
		return -1;
	}

	std::string vertexShaderSource = R"(
		#version 330 core
		layout(location = 0) in vec3 position;
		void main()
		{
			gl_Position = vec4(position, 1.0);
		}
	)";

	GLuint vertexShader = glCreateShader(GL_VERTEX_SHADER);
	const char* vertexSourceCStr = vertexShaderSource.c_str();
	glShaderSource(vertexShader, 1, &vertexSourceCStr, nullptr);
	glCompileShader(vertexShader);

	GLint success;
	glGetShaderiv(vertexShader, GL_COMPILE_STATUS, &success);
	if (!success) {
		char infoLog[512];
		glGetShaderInfoLog(vertexShader, 512, nullptr, infoLog);
		std::cerr << "ERROR::SHADER::VERTEX::COMPILATION_FAILED: " << infoLog << std::endl;
	}

	std::string fragmentShaderSource = R"(
		#version 330 core
		out vec4 fragColor;
		void main()
		{
			fragColor = vec4(1.0, 0.0, 0.0, 1.0);
		}
	)";

	GLuint fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
	const char* fragmentSourceCStr = fragmentShaderSource.c_str();
	glShaderSource(fragmentShader, 1, &fragmentSourceCStr, nullptr);
	glCompileShader(fragmentShader);

	glGetShaderiv(fragmentShader, GL_COMPILE_STATUS, &success);
	if (!success) {
		char infoLog[512];
		glGetShaderInfoLog(fragmentShader, 512, nullptr, infoLog);
		std::cerr << "ERROR::SHADER::FRAGMENT::COMPILATION_FAILED: " << infoLog << std::endl;
	}

	GLuint shaderProgram = glCreateProgram();
	glAttachShader(shaderProgram, vertexShader);
	glAttachShader(shaderProgram, fragmentShader);
	glLinkProgram(shaderProgram);

	glGetProgramiv(shaderProgram, GL_LINK_STATUS, &success);
	if (!success) {
		char infoLog[512];
		glGetProgramInfoLog(shaderProgram, 512, nullptr, infoLog);
		std::cerr << "ERROR::SHADER::PROGRAM::LINKING_FAILED: " << infoLog << std::endl;
	}

	glDeleteShader(vertexShader);
	glDeleteShader(fragmentShader);

	std::vector<float> vertices = {
		 0.0f,  0.5f, 0.0f,
		-0.5f, -0.5f, 0.0f,
		 0.5f, -0.5f, 0.0f
	};

	GLuint vbo;
	glGenBuffers(1, &vbo);
	glBindBuffer(GL_ARRAY_BUFFER, vbo);
	glBufferData(GL_ARRAY_BUFFER, vertices.size() * sizeof(float), vertices.data(), GL_STATIC_DRAW);
	glBindBuffer(GL_ARRAY_BUFFER, 0); // Unbind VBO

	GLuint vao;
	glGenVertexArrays(1, &vao);
	glBindVertexArray(vao);
	glBindBuffer(GL_ARRAY_BUFFER, vbo);

	glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 3 * sizeof(float), (void*)0);
	glEnableVertexAttribArray(0);

	glBindBuffer(GL_ARRAY_BUFFER, 0); // Unbind VBO
	glBindVertexArray(0); // Unbind VAO

	// Main loop
	while (!glfwWindowShouldClose(window)) {
		glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT);

		glUseProgram(shaderProgram);
		glBindVertexArray(vao);
		glDrawArrays(GL_TRIANGLES, 0, 3);

		glfwSwapBuffers(window);
		glfwPollEvents();
	}

	glfwDestroyWindow(window);
	glfwTerminate();
    return 0;
}