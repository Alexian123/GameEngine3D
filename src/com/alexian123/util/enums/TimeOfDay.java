package com.alexian123.util.enums;

public enum TimeOfDay {
	DAWN(5000.0f),
	MORNING(8000.0f),
	NOON(12000.0f),
	AFTERNOON(15000.0f),
	EVENING(18000.0f),
	NIGHT(22000.0f),
	MAX_TIME(24000.0f);
	
	private final float value;

	private TimeOfDay(float value) {
		this.value = value;
	}
	
	public float getValue() {
		return value;
	}
}