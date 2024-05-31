#version 400

in vec3 textureCoord;

out vec4 outColor;

uniform samplerCube cubeMap;

void main(void) {
	outColor = texture(cubeMap, textureCoord);
}
