#version 330 core

#define MAX_LIGHTS 4

in vec2 passTextureCoord;
in vec3 surfaceNormal;
in vec3 toLightVector[MAX_LIGHTS];
in vec3 toCameraVector;
in vec4 shadowMapCoord;
in float visibility;
in float passUseNormalMap;

layout (location = 0) out vec4 outColor;
layout (location = 1) out vec4 outBrightColor;

uniform sampler2D colorTexture;
uniform sampler2D lightingMap;
uniform sampler2D shadowMap;
uniform sampler2D normalMap;

uniform vec3 lightColor[MAX_LIGHTS];
uniform vec3 attenuation[MAX_LIGHTS];
uniform vec3 fogColor;
uniform int shadowMapSize;
uniform int pcfCount;
uniform float shineDamper;
uniform float reflectivity;
uniform float useLightingMap;
uniform float ambientLight;


void main(void) {
	float totalTexels = (pcfCount * 2.0 + 1.0) * (pcfCount * 2.0 + 1.0);
	float texelSize = 1.0 / shadowMapSize;
	float total = 0;

	for (int x = -pcfCount; x <= pcfCount; ++x) {
		for (int y = -pcfCount; y <= pcfCount; ++y) {
			float objectNearestLight = texture(shadowMap, shadowMapCoord.xy + vec2(x, y) * texelSize).r;
			if (shadowMapCoord.z > objectNearestLight + 0.002) {
				total += 1.0;
			}
		}
	}

	total /= totalTexels;

	float lightFactor = 1.0 - (total * shadowMapCoord.w);
	
	vec3 unitSurfaceNormal = vec3(0.0);
	if (passUseNormalMap > 0.5) {
		vec4 normalMapColor = 2.0 * texture(normalMap, passTextureCoord) - 1.0;
		unitSurfaceNormal = normalize(normalMapColor.rgb);	
	} else {
		unitSurfaceNormal = normalize(surfaceNormal);
	}

	vec3 unitToCameraVector = normalize(toCameraVector);

	vec3 diffuse = vec3(0.0);
	vec3 specular = vec3(0.0);
	for (int i = 0; i < MAX_LIGHTS; ++i) {
		float distanceToLight = length(toLightVector[i]);
		float attenuationFactor = attenuation[i].x + attenuation[i].y * distanceToLight + attenuation[i].z * distanceToLight * distanceToLight;
		vec3 unitToLightVector = normalize(toLightVector[i]);

		// diffuse light
		float brightness = dot(unitSurfaceNormal, unitToLightVector);
		brightness = max(brightness, 0.0);
		diffuse += brightness * lightColor[i] / attenuationFactor;

		// specular light
		vec3 lightDirection = -unitToLightVector;
		vec3 reflectedLightDirection = reflect(lightDirection, unitSurfaceNormal);
		float specularFactor = dot(reflectedLightDirection, unitToCameraVector);
		specularFactor = max(specularFactor, 0.0);
		float dampedFactor = pow(specularFactor, shineDamper);
		specular += dampedFactor * reflectivity * lightColor[i] / attenuationFactor;
	}
	diffuse = max(diffuse * lightFactor, ambientLight); // minimum value = ambient light

	vec4 textureColor = texture(colorTexture, passTextureCoord);
	if (textureColor.a < 0.5) {	// transparency
		discard;
	}

	if (useLightingMap > 0.5) {
		vec4 lightingMapInfo = texture(lightingMap, passTextureCoord);
		specular *= lightingMapInfo.r;
		if (lightingMapInfo.g > 0.5) {
			outBrightColor = textureColor + vec4(specular, 1.0);
			diffuse = vec3(1.0);
		}
	}

	outColor = vec4(diffuse, 1.0) * textureColor + vec4(specular, 1.0);
	outColor = mix(vec4(fogColor, 1.0), outColor, visibility);
}
