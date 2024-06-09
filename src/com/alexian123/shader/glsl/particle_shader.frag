#version 140

in vec2 currentTextureCoord;
in vec2 nextTextureCoord;
in float blendFactor;

out vec4 outColour;

uniform sampler2D particleTexture;

void main(void) {

	vec4 currentColor = texture(particleTexture, currentTextureCoord);
	vec4 nextColor = texture(particleTexture, nextTextureCoord);

	outColour = mix(currentColor, nextColor, blendFactor);

}
