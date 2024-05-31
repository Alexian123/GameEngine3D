#version 400 core

#define MAX_LIGHTS 4

in vec2 passTextureCoord;
in vec3 surfaceNormal;
in vec3 toLightVector[MAX_LIGHTS];
in vec3 toCameraVector;
in float visibility;

out vec4 outColor;

uniform sampler2D bgTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;
uniform sampler2D blendMap;

uniform vec3 lightColor[MAX_LIGHTS];
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;

void main(void) {
	vec4 blendMapColor = texture(blendMap, passTextureCoord);
	vec2 tiledTextureCoord = passTextureCoord * 40.0;

	float bgTextureAmount = 1 - (blendMapColor.r + blendMapColor.g + blendMapColor.b);
	vec4 bgTextureColor = texture(bgTexture, tiledTextureCoord) * bgTextureAmount;
	vec4 rTextureColor = texture(rTexture, tiledTextureCoord) * blendMapColor.r;
	vec4 gTextureColor = texture(gTexture, tiledTextureCoord) * blendMapColor.g;
	vec4 bTextureColor = texture(bTexture, tiledTextureCoord) * blendMapColor.b;
	vec4 totalColor = bgTextureColor + rTextureColor + gTextureColor + bTextureColor;

	vec3 unitSurfaceNormal = normalize(surfaceNormal);
	vec3 unitToCameraVector = normalize(toCameraVector);

	vec3 diffuse = vec3(0.0);
	vec3 specular = vec3(0.0);
	for (int i = 0; i < MAX_LIGHTS; ++i) {
		vec3 unitToLightVector = normalize(toLightVector[i]);

		// diffuse light
		float brightness = dot(unitSurfaceNormal, unitToLightVector);
		brightness = max(brightness, 0.0);
		diffuse += brightness * lightColor[i];

		// specular light
		vec3 lightDirection = -unitToLightVector;
		vec3 reflectedLightDirection = reflect(lightDirection, unitSurfaceNormal);
		float specularFactor = dot(reflectedLightDirection, unitToCameraVector);
		specularFactor = max(specularFactor, 0.0);
		float dampedFactor = pow(specularFactor, shineDamper);
		specular += dampedFactor * reflectivity * lightColor[i];
	}
	diffuse = max(diffuse, 0.2); // minimum value = ambient light

	outColor = vec4(diffuse, 1.0) * totalColor + vec4(specular, 1.0);
	outColor = mix(vec4(skyColor, 1.0), outColor, visibility);
}
