#version 140

in vec2 textureCoord;

out vec4 outColor;

uniform sampler2D colourTexture;
uniform float contrast;

void main(void) {
	outColor = texture(colourTexture, textureCoord);
	outColor.rgb = (outColor.rgb - 0.5) * (1.0 + contrast) + 0.5;
}
