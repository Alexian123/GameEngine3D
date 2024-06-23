#version 330

#define MAX_LIGHTS 4

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 textureCoord;
layout (location = 2) in vec3 normal;

out vec2 passTextureCoord;
out vec3 surfaceNormal;
out vec3 toLightVector[MAX_LIGHTS];
out vec3 toCameraVector;
out vec4 shadowMapCoord;
out float visibility;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 toShadowMapSpace;
uniform vec3 lightPosition[MAX_LIGHTS];
uniform vec4 clipPlane;
uniform float fogDensity;
uniform float fogGradient;
uniform float shadowDistance;
uniform float shadowTransition;

void main(void) {
	vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
	shadowMapCoord = toShadowMapSpace * worldPosition;

	gl_ClipDistance[0] = dot(worldPosition, clipPlane);

	vec4 positionRelativeToCamera = viewMatrix * worldPosition;
	gl_Position = projectionMatrix * positionRelativeToCamera;
	passTextureCoord = textureCoord;

	surfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz;
	for (int i = 0; i < MAX_LIGHTS; ++i) {
		toLightVector[i] = lightPosition[i] - worldPosition.xyz;
	}
	toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;

	float distance = length(positionRelativeToCamera.xyz);
	visibility = exp(-pow((distance * fogDensity), fogGradient));
	visibility = clamp(visibility, 0.0, 1.0);

	distance -= (shadowDistance - shadowTransition);
	distance /= shadowTransition;
	shadowMapCoord.w = clamp(1.0 - distance, 0.0, 1.0);
}
