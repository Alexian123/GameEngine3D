#version 140

in vec2 position;
in mat4 modelViewMatrix;
in vec4 atlasOffsets;
in float blendFactor;

out vec2 currentTextureCoord;
out vec2 nextTextureCoord;
out float passBlendFactor;

uniform mat4 projectionMatrix;
uniform float atlasDimension;

void main(void) {

	vec2 textureCoord = position + vec2(0.5, 0.5);
	textureCoord.y = 1.0 - textureCoord.y;

	textureCoord /= atlasDimension;
	currentTextureCoord = textureCoord + atlasOffsets.xy;
	nextTextureCoord = textureCoord + atlasOffsets.zw;
	passBlendFactor = blendFactor;

	gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 0.0, 1.0);

}
