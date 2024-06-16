#version 150

in vec2 textureCoord;

out vec4 outColor;

uniform sampler2D colorTexture;

void main(void) {
	vec4 color = texture(colorTexture, textureCoord);
	float brightness = color.r * 0.2126 + color.g * 0.7152 + color.b * 0.0722;	// Luma conversio
	outColor = color * brightness;
}
