package com.alexian123.util.enums;

public enum Attribute {
	POSITION("position"),
	TEXTURE_COORD("textureCoord"),
	NORMAL("normal"),
	TANGENT("tangent"),
	MODEL_VIEW_MATRIX("modelViewMatrix"),
	ATLAS_OFFSETS("atlasOffsets"),
	BLEND_FACTOR("blendFactor"),
	JOINT_INDICES("jointIndices"),
	WEIGHTS("weights"),
	;

	private final String name;
	
	private Attribute(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}