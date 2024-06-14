#version 400 core

#define MAX_LIGHTS 4

in vec2 passTextureCoord;
in vec3 toLightVector[MAX_LIGHTS];
in vec3 toCameraVector;
in float visibility;

out vec4 outColor;

uniform sampler2D modelTexture;
uniform sampler2D normalMap;

uniform vec3 lightColor[MAX_LIGHTS];
uniform vec3 attenuation[MAX_LIGHTS];
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 fogColor;

void main(void) {
	vec4 normalMapColor = 2.0 * texture(normalMap, passTextureCoord) - 1.0;

	vec3 unitSurfaceNormal = normalize(normalMapColor.rgb);
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
	diffuse = max(diffuse, 0.2); // minimum value = ambient light

	vec4 textureColor = texture(modelTexture, passTextureCoord);
	if (textureColor.a < 0.5) {	// transparency
		discard;
	}

	outColor = vec4(diffuse, 1.0) * textureColor + vec4(specular, 1.0);
	outColor = mix(vec4(fogColor, 1.0), outColor, visibility);
}