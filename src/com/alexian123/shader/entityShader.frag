#version 400 core

in vec2 passTextureCoord;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;

out vec4 outColor;

uniform sampler2D textureSampler;
uniform vec3 lightColor;
uniform float shineDamper;
uniform float reflectivity;

void main(void) {
	vec3 unitSurfaceNormal = normalize(surfaceNormal);
	vec3 unitToLightVector = normalize(toLightVector);
	vec3 unitToCameraVector = normalize(toCameraVector);

	// diffuse light
	float brightness = dot(unitSurfaceNormal, unitToLightVector);
	brightness = max(brightness, 0.4);	// minimum value = ambient light
	vec3 diffuse = brightness * lightColor;

	// specular light
	vec3 lightDirection = -unitToLightVector;
	vec3 reflectedLightDirection = reflect(lightDirection, unitSurfaceNormal);
	float specularFactor = dot(reflectedLightDirection, unitToCameraVector);
	specularFactor = max(specularFactor, 0.0);
	float dampedFactor = pow(specularFactor, shineDamper);
	vec3 specular = dampedFactor * reflectivity * lightColor;

	vec4 textureColor = texture(textureSampler, passTextureCoord);
	if (textureColor.a < 0.5) {
		discard;
	}

	outColor = vec4(diffuse, 1.0) * textureColor + vec4(specular, 1.0);
}
