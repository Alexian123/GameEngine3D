#version 140

in vec2 textureCoord;

out vec4 outColor;

uniform sampler2D guiTexture;

void main(void){
	outColor = texture(guiTexture, textureCoord);
}
