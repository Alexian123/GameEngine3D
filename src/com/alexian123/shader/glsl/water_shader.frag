#version 400 core

#define MAX_LIGHTS 4

in vec4 clipSpace;
in vec2 textureCoord;
in vec3 toCameraVector;
in vec3 fromLightVector[MAX_LIGHTS];

out vec4 outColor;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D dudvMap;
uniform sampler2D normalMap;
uniform float moveFactor;
uniform vec3 lightColor[MAX_LIGHTS];
uniform vec3 attenuation[MAX_LIGHTS];
uniform float shineDamper;
uniform float reflectivity;

const float waveStrength = 0.02;

void main(void) {
	vec2 ndc = (clipSpace.xy / clipSpace.w) / 2.0 + 0.5;
	vec2 reflectionTextureCoord = vec2(ndc.x, -ndc.y);
	vec2 refractionTextureCoord = vec2(ndc.x, ndc.y);

	vec2 distortedTextureCoord = texture(dudvMap, vec2(textureCoord.x + moveFactor, textureCoord.y)).rg * 0.1;
	distortedTextureCoord = textureCoord + vec2(distortedTextureCoord.x, distortedTextureCoord.y + moveFactor);
	vec2 totalDistortion = (texture(dudvMap, distortedTextureCoord).rg * 2.0 - 1.0) * waveStrength;

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

	vec4 normalMapColor = texture(normalMap, distortedTextureCoord);
	vec3 normal = normalize(vec3(normalMapColor.r * 2.0 - 1.0, normalMapColor.b, normalMapColor.g * 2.0 - 1.0));

	vec3 specular = vec3(0.0);
	for (int i = 0; i < MAX_LIGHTS; ++i) {
		float distanceToLight = length(fromLightVector[i]);
		vec3 reflectedLight = reflect(normalize(fromLightVector[i]), normal);
		float specularFactor = max(dot(reflectedLight, unitToCameraVector), 0.0);
		specularFactor = pow(specularFactor, shineDamper);
		float attenuationFactor = attenuation[i].x + attenuation[i].y * distanceToLight + attenuation[i].z * distanceToLight * distanceToLight;
		specular += lightColor[i] * specularFactor * reflectivity * (1.0 - fresnelFactor) / attenuationFactor;
	}

	outColor = mix(reflectionColor, refractionColor, fresnelFactor);
	outColor = mix(outColor, vec4(0.0, 0.3, 0.5, 1.0), 0.2) + vec4(specular, 0.0);
}
