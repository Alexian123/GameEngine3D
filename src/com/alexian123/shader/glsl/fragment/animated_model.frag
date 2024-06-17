#version 330 core

#define MAX_LIGHTS 4

in vec2 passTextureCoord;
in vec3 passNormal;
in vec3 toLightVector[MAX_LIGHTS];
in vec3 toCameraVector;
in vec4 shadowMapCoord;
in float visibility;

layout (location = 0) out vec4 outColor;
layout (location = 1) out vec4 outBrightColor;

uniform sampler2D modelTexture;
uniform sampler2D lightingMap;
uniform sampler2D shadowMap;

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

	vec4 diffuseColour = texture(modelTexture, passTextureCoord);
	vec3 unitNormal = normalize(passNormal);
	float diffuseLight = max(dot(-lightDirection, unitNormal), 0.0) * lightBias.x + lightBias.y;
	outColor = diffuseColour * diffuseLight;
	outBrightColor = vec4(0.0);
}
