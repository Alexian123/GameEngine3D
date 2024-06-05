#version 400 core

in vec2 position;

out vec4 clipSpace;
out vec2 textureCoord;
out vec3 toCameraVector;
out vec3 fromLightVector;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
uniform vec3 cameraPosition;
uniform vec3 lightPosition;

const float tiling = 6.0;

void main(void) {
	vec4 worldPos = modelMatrix * vec4(position.x, 0.0, position.y, 1.0);
	clipSpace = projectionMatrix * viewMatrix * worldPos;
	gl_Position = clipSpace;
	textureCoord = vec2(position.x / 2.0 + 0.5, position.y / 2.0 + 0.5) * tiling;
	toCameraVector = cameraPosition - worldPos.xyz;
	fromLightVector = worldPos.xyz - lightPosition;
}
