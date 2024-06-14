#version 150

in vec3 position;
in vec2 textureCoord;

out vec2 passTextureCoord;

uniform mat4 mvpMatrix;

void main(void) {

	gl_Position = mvpMatrix * vec4(position, 1.0);
	passTextureCoord = textureCoord;

}
