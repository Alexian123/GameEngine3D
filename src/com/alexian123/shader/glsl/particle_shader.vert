#version 140

in vec2 position;

out vec2 currentTextureCoord;
out vec2 nextTextureCoord;
out float blendFactor;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;
uniform vec2 currentAtlasOffset;
uniform vec2 nextAtlasOffset;
uniform vec2 textureCoordInfo;

void main(void) {

	vec2 textureCoord = position + vec2(0.5, 0.5);
	textureCoord.y = 1.0 - textureCoord.y;

	textureCoord /= textureCoordInfo.x;
	currentTextureCoord = textureCoord + currentAtlasOffset;
	nextTextureCoord = textureCoord + nextAtlasOffset;
	blendFactor = textureCoordInfo.y;

	gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 0.0, 1.0);

}
