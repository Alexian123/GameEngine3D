#version 400

in vec3 textureCoord;

out vec4 outColor;

uniform samplerCube cubeMap0;
uniform samplerCube cubeMap1;
uniform vec3 fogColor;
uniform float blendFactor;
uniform float lowerLimit;
uniform float upperLimit;

void main(void) {
	vec4 color0 = texture(cubeMap0, textureCoord);
	vec4 color1 = texture(cubeMap1, textureCoord);
	vec4 finalColor = mix(color0, color1, blendFactor);
	float visibilityFactor = (textureCoord.y - lowerLimit) / (upperLimit - lowerLimit);
	visibilityFactor = clamp(visibilityFactor, 0.0, 1.0);
	outColor = mix(vec4(fogColor, 1.0), finalColor, visibilityFactor);
}
