#version 400 core

in vec4 clipSpace;
in vec2 textureCoord;
in vec3 toCameraVector;

out vec4 outColor;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D dudvMap;
uniform float moveFactor;

const float waveStrength = 0.02;

void main(void) {
	vec2 ndc = (clipSpace.xy / clipSpace.w) / 2.0 + 0.5;
	vec2 reflectionTextureCoord = vec2(ndc.x, -ndc.y);
	vec2 refractionTextureCoord = vec2(ndc.x, ndc.y);

	vec2 distortion1 = (texture(dudvMap, vec2(textureCoord.x + moveFactor, textureCoord.y)).rg * 2.0 - 1.0) * waveStrength;
	vec2 distortion2 = (texture(dudvMap, vec2(-textureCoord.x + moveFactor, textureCoord.y + moveFactor)).rg * 2.0 - 1.0) * waveStrength;
	vec2 totalDistortion = distortion1 + distortion2;

	reflectionTextureCoord += totalDistortion;
	reflectionTextureCoord.x = clamp(reflectionTextureCoord.x, 0.001, 0.999);
	reflectionTextureCoord.y = clamp(reflectionTextureCoord.y, -0.999, -0.001);

	refractionTextureCoord += totalDistortion;
	refractionTextureCoord = clamp(refractionTextureCoord, 0.001, 0.999);

	vec4 reflectionColor = texture(reflectionTexture, reflectionTextureCoord);
	vec4 refractionColor = texture(refractionTexture, refractionTextureCoord);

	vec3 unitToCameraVector = normalize(toCameraVector);
	float fresnelFactor = dot(unitToCameraVector, vec3(0.0, 1.0, 0.0));
	fresnelFactor = pow(fresnelFactor, 2.0);

	outColor = mix(reflectionColor, refractionColor, fresnelFactor);
	outColor = mix(outColor, vec4(0.0, 0.3, 0.5, 1.0), 0.2);
}
