#version 400

in vec3 position;

out vec3 textureCoord;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main(void) {
	gl_Position = projectionMatrix * viewMatrix * vec4(position, 1.0);
	textureCoord = position;
}
