cmake_minimum_required(VERSION 3.10)
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED True)

project (GameEngine3D)

set(PROJECT_SOURCE_FILES
  source/main.cpp # entry point
  source/Game.h
  source/Game.cpp
  source/TestObject.h
  source/TestObject.cpp
)

# Include GLEW library
include_directories(engine/external/glew/include)

# Add the application
include_directories(source)
add_executable(${PROJECT_NAME} ${PROJECT_SOURCE_FILES})

# Add Engine subdirectory
add_subdirectory(engine "${CMAKE_CURRENT_BINARY_DIR}/engine_build")
include_directories(engine/source)

# Link Engine lib
target_link_libraries(${PROJECT_NAME} 
    Engine
)
