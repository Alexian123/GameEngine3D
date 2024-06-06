#version 400 core

#define MAX_LIGHTS 4

in vec2 position;

out vec4 clipSpace;
out vec2 textureCoord;
out vec3 toCameraVector;
out vec3 fromLightVector[MAX_LIGHTS];
out float visibility;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
uniform vec3 cameraPosition;
uniform vec3 lightPosition[MAX_LIGHTS];
uniform float tilingFactor;
uniform float fogDensity;
uniform float fogGradient;

void main(void) {
	vec4 worldPos = modelMatrix * vec4(position.x, 0.0, position.y, 1.0);
	vec4 positionRelativeToCamera = viewMatrix * worldPos;
	clipSpace = projectionMatrix * positionRelativeToCamera;
	gl_Position = clipSpace;
	textureCoord = vec2(position.x / 2.0 + 0.5, position.y / 2.0 + 0.5) * tilingFactor;
	toCameraVector = cameraPosition - worldPos.xyz;

	for (int i = 0; i < MAX_LIGHTS; ++i) {
		fromLightVector[i] = worldPos.xyz - lightPosition[i];
	}

	float distance = length(positionRelativeToCamera.xyz);
	visibility = exp(-pow((distance * fogDensity), fogGradient));
	visibility = clamp(visibility, 0.0, 1.0);
}
