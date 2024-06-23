package com.alexian123.util.enums;

public enum ConfigSection {
	WINDOW("WINDOW"),
	ENGINE("ENGINE"),
	CAMERA("CAMERA"),
	PHYSICS("PHYSICS"),
	PLAYER("PLAYER"),
	FOG("FOG"),
	LIGHTING("LIGHTING"),
	POST_PROCESSING("POST PROCESSING"),
	TERRAIN("TERRAIN"),
	WATER("WATER"),
	SHADOWS("SHADOWS"),
	TEXT("TEXT"),
	;
	
    private final String value;

    ConfigSection(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
	
}
