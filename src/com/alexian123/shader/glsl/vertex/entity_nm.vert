#version 400 core

#define MAX_LIGHTS 4

in vec3 position;
in vec2 textureCoord;
in vec3 normal;
in vec3 tangent;

out vec2 passTextureCoord;
out vec3 toLightVector[MAX_LIGHTS];
out vec3 toCameraVector;
out vec4 shadowMapCoord;
out float visibility;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 toShadowMapSpace;
uniform vec3 lightPosition[MAX_LIGHTS];
uniform float useFakeLighting;
uniform float atlasDimension;
uniform vec2 atlasOffset;
uniform vec4 clipPlane;
uniform float fogDensity;
uniform float fogGradient;
uniform float shadowDistance;
uniform float shadowTransition;

void main(void) {
	vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
	shadowMapCoord = toShadowMapSpace * worldPosition;

	gl_ClipDistance[0] = dot(worldPosition, clipPlane);

	mat4 modelViewMatrix = viewMatrix * transformationMatrix;
	vec4 positionRelativeToCamera = modelViewMatrix * vec4(position, 1.0);
	gl_Position = projectionMatrix * positionRelativeToCamera;

	passTextureCoord = (textureCoord / atlasDimension) + atlasOffset;

	vec3 actualNormal = normal;
	if (useFakeLighting > 0.5) {
		actualNormal = vec3(0.0, 1.0, 0.0);
	}

	vec3 surfaceNormal = (modelViewMatrix * vec4(actualNormal, 0.0)).xyz;

	vec3 norm = normalize(surfaceNormal);
	vec3 tang = normalize((modelViewMatrix * vec4(tangent, 0.0)).xyz);
	vec3 bitang = normalize(cross(norm, tang));

	mat3 toTangentSpace = mat3(
		tang.x, bitang.x, norm.x,
		tang.y, bitang.y, norm.y,
		tang.z, bitang.z, norm.z
	);

	for (int i = 0; i < MAX_LIGHTS; ++i) {
		toLightVector[i] = toTangentSpace * (lightPosition[i] - positionRelativeToCamera.xyz);
	}
	toCameraVector = toTangentSpace * (-positionRelativeToCamera.xyz);

	float distance = length(positionRelativeToCamera.xyz);
	visibility = exp(-pow((distance * fogDensity), fogGradient));
	visibility = clamp(visibility, 0.0, 1.0);

	distance -= (shadowDistance - shadowTransition);
	distance /= shadowTransition;
	shadowMapCoord.w = clamp(1.0 - distance, 0.0, 1.0);
}
