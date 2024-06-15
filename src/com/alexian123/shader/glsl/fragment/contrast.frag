#version 140

in vec2 textureCoord;

out vec4 outColour;

uniform sampler2D colourTexture;
uniform float contrast;

void main(void) {
	outColour = texture(colourTexture, textureCoord);
	outColour.rgb = (outColour.rgb - 0.5) * (1.0 + contrast) + 0.5;
}
