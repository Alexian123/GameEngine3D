#version 150

#define NUM_PIXELS 11
#define MIN_OFFSET -5
#define MAX_OFFSET 5

in vec2 position;

out vec2 blurTextureCoords[NUM_PIXELS];

uniform float targetWidth;

void main(void) {

	gl_Position = vec4(position, 0.0, 1.0);
	vec2 centerTexCoord = position * 0.5 + 0.5;
	float pixelSize = 1.0 / targetWidth;

	for (int i = MIN_OFFSET; i <= MAX_OFFSET; ++i) {
		blurTextureCoords[i - MIN_OFFSET] = centerTexCoord + vec2(pixelSize * i, 0.0);
	}
}
