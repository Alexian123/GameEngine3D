#version 330

in vec2 passTextureCoord;

out vec4 outColour;

uniform sampler2D modelTexture;

void main(void) {

	float alpha = texture(modelTexture, passTextureCoord).a;
	if (alpha < 0.5) {
		discard;
	}

	outColour = vec4(1.0);

}
