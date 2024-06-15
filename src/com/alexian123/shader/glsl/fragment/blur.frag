#version 150

#define NUM_PIXELS 11

in vec2 blurTextureCoords[NUM_PIXELS];

out vec4 outColor;

uniform sampler2D originalTexture;

const float weights[NUM_PIXELS] = {
	0.0093, 0.028002, 0.065984, 0.121703, 0.175713, 0.198596,
	0.175713, 0.121703, 0.065984, 0.028002, 0.0093
};

void main(void) {

	outColor = vec4(0.0);
	for (int i = 0; i < NUM_PIXELS; ++i) {
		outColor += texture(originalTexture, blurTextureCoords[i]) * weights[i];
	}
}
