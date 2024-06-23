#version 150

in vec2 textureCoord;

out vec4 outColor;

uniform sampler2D texture1;
uniform sampler2D texture2;

uniform float combineFactor;

void main(void) {

	vec4 color1 = texture(texture1, textureCoord);
	vec4 color2 = texture(texture2, textureCoord);
	outColor = color1 + color2 * combineFactor;
}
