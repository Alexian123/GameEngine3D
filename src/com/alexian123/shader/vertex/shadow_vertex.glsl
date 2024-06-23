#version 330

#define MAX_JOINTS 50
#define MAX_WEIGHTS 3

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 textureCoord;
layout (location = 4) in ivec3 jointIndices;
layout (location = 5) in vec3 weights;

out vec2 passTextureCoord;

uniform mat4 mvpMatrix;
uniform mat4 jointTransforms[MAX_JOINTS];
uniform float isAnimated;

void main(void) {

	vec4 totalLocalPos = vec4(position, 1.0);
	if (isAnimated > 0.5) {
		totalLocalPos = vec4(0.0);
		for(int i = 0; i < MAX_WEIGHTS; ++i) {
			mat4 jointTransform = jointTransforms[jointIndices[i]];
			vec4 posePosition =  jointTransform * vec4(position, 1.0);
			totalLocalPos += posePosition * weights[i];
		}
	}

	gl_Position = mvpMatrix * totalLocalPos;
	passTextureCoord = textureCoord;

}
