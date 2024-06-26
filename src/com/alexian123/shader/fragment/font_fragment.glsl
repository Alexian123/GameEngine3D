#version 330

in vec2 passTextureCoord;

out vec4 outColor;

uniform sampler2D fontAtlas;
uniform vec3 color;
uniform vec3 outlineColor;
uniform vec2 offset;
uniform float characterWidth;
uniform float characterEdge;
uniform float borderWidth;
uniform float borderEdge;

// use in case 'smoothstep' makes the game crash
float smoothlyStep(float edge0, float edge1, float x) {
    float t = clamp((x - edge0) / (edge1 - edge0), 0.0, 1.0);
    return t * t * (3.0 - 2.0 * t);
}

void main(void) {
	float charDistance = 1.0 - texture(fontAtlas, passTextureCoord).a;
	float charAlpha = 1.0 - smoothstep(characterWidth, characterWidth + characterEdge, charDistance);

	float borderDistance = 1.0 - texture(fontAtlas, passTextureCoord + offset).a;
	float borderAlpha = 1.0 - smoothstep(borderWidth, borderWidth + borderEdge, borderDistance);

	float totalAlpha = charAlpha + (1.0 - charAlpha) * borderAlpha;
	vec3 totalColor = mix(outlineColor, color, charAlpha / totalAlpha);

	outColor = vec4(totalColor, totalAlpha);
}
