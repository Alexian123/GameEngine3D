package com.alexian123.shader;

public enum Attribute {
	POSITION("position"),
	TEXTURE_COORD("textureCoord"),
	NORMAL("normal"),
	TANGENT("tangent");

	private final String name;
	
	private Attribute(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
