cmake_minimum_required(VERSION 3.10)

project(Engine)

set(PROJECT_SOURCE_FILES
	source/Engine.h
	source/Engine.cpp
	source/Application.h
	source/Application.cpp
	source/input/InputManager.h
	source/input/InputManager.cpp
	source/graphics/ShaderProgram.h
	source/graphics/ShaderProgram.cpp
	source/graphics/GraphicsAPI.h
	source/graphics/GraphicsAPI.cpp
	source/graphics/VertexLayout.h
	source/render/Material.h
	source/render/Material.cpp
	source/render/Mesh.h
	source/render/Mesh.cpp
	source/render/RenderQueue.h
	source/render/RenderQueue.cpp
	source/scene/GameObject.h
	source/scene/GameObject.cpp
	source/scene/Scene.h
	source/scene/Scene.cpp
)

include_directories(source)

add_library(${PROJECT_NAME} ${PROJECT_SOURCE_FILES})

# Add GLFW library
add_subdirectory(external/glfw-3.4 "${CMAKE_CURRENT_BINARY_DIR}/glfw_build")
include_directories(external/glfw-3.4/include)

# Add Glew library
add_compile_definitions(GLEW_NO_GLU)
set(BUILD_UTILS OFF CACHE BOOL "utilities" FORCE)
add_subdirectory(external/glew/build/cmake "${CMAKE_CURRENT_BINARY_DIR}/glew_build")
include_directories(external/glew/include)
set_property(TARGET glew_s PROPERTY FOLDER GLEW)
set_property(TARGET glew PROPERTY FOLDER GLEW)

# Add GLM library
add_subdirectory(external/glm-1.0.1/glm)
include_directories(external/glm-1.0.1)

# Link all external libraries
target_link_libraries(${PROJECT_NAME} 
    glfw 
    glew_s
	glm
)