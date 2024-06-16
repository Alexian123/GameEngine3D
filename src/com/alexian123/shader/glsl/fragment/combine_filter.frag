#version 150

in vec2 textureCoord;

out vec4 outColor;

uniform sampler2D colorTexture;
uniform sampler2D highlightTexture;

uniform float bloomFactor;

void main(void) {

	vec4 sceneColor = texture(colorTexture, textureCoord);
	vec4 highlightColor = texture(highlightTexture, textureCoord);
	outColor = sceneColor + highlightColor * bloomFactor;
}
