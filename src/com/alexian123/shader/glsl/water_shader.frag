#version 400 core

in vec4 clipSpace;

out vec4 outColor;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;

void main(void) {
	vec2 ndc = (clipSpace.xy / clipSpace.w) / 2.0 + 0.5;
	vec2 reflectionTextureCoord = vec2(ndc.x, -ndc.y);
	vec2 refractionTextureCoord = vec2(ndc.x, ndc.y);

	vec4 reflectionColor = texture(reflectionTexture, reflectionTextureCoord);
	vec4 refractionColor = texture(refractionTexture, refractionTextureCoord);

	outColor = mix(reflectionColor, refractionColor, 0.5);
}
