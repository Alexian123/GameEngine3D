#version 150 core

#define MAX_JOINTS 50
#define MAX_WEIGHTS 3
#define MAX_LIGHTS 4

in vec3 position;
in vec2 textureCoord;
in vec3 normal;
in vec3 tangent;
in ivec3 jointIndices;
in vec3 weights;

out vec2 passTextureCoord;
out vec3 surfaceNormal;
out vec3 toLightVector[MAX_LIGHTS];
out vec3 toCameraVector;
out vec4 shadowMapCoord;
out float visibility;
out float passUseNormalMap;

uniform mat4 jointTransforms[MAX_JOINTS];
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
uniform float isAnimated;
uniform float useNormalMap;

void main(void) {
	passUseNormalMap = useNormalMap;

	vec4 totalLocalPos = vec4(position, 1.0);
	vec4 totalNormal = vec4(normal, 0.0);
	if (isAnimated > 0.5) {
		totalLocalPos = vec4(0.0);
		totalNormal = vec4(0.0);
		for(int i = 0; i < MAX_WEIGHTS; ++i) {
			mat4 jointTransform = jointTransforms[jointIndices[i]];
			vec4 posePosition =  jointTransform * vec4(position, 1.0);
			totalLocalPos += posePosition * weights[i];
	
			vec4 worldNormal = jointTransform * vec4(normal, 0.0);
			totalNormal += worldNormal * weights[i];
		}
	}

	vec4 worldPosition = transformationMatrix * totalLocalPos;
	shadowMapCoord = toShadowMapSpace * worldPosition;

	gl_ClipDistance[0] = dot(worldPosition, clipPlane);

	vec4 positionRelativeToCamera = viewMatrix * worldPosition;
	gl_Position = projectionMatrix * positionRelativeToCamera;
	
	passTextureCoord = (textureCoord / atlasDimension) + atlasOffset;

	vec3 actualNormal = totalNormal.xyz;
	if (useFakeLighting > 0.5) {
		actualNormal = vec3(0.0, 1.0, 0.0);
	}

	
	
	if (useNormalMap >  0.5) {
		mat4 modelViewMatrix = transformationMatrix * viewMatrix;
		surfaceNormal = (modelViewMatrix * vec4(actualNormal, 0.0)).xyz;
		
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
	} else {
		surfaceNormal = (transformationMatrix * vec4(actualNormal, 0.0)).xyz;
		for (int i = 0; i < MAX_LIGHTS; ++i) {
			toLightVector[i] = lightPosition[i] - worldPosition.xyz;
		}
		toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;
	}

	float distance = length(positionRelativeToCamera.xyz);
	visibility = exp(-pow((distance * fogDensity), fogGradient));
	visibility = clamp(visibility, 0.0, 1.0);

	distance -= (shadowDistance - shadowTransition);
	distance /= shadowTransition;
	shadowMapCoord.w = clamp(1.0 - distance, 0.0, 1.0);
}
