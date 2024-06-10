package com.alexian123.util;

import com.alexian123.engine.DisplayManager;

public class Clock {

	private float timeSpeed;
	private float timeVal = TimeOfDay.NOON.getValue();
	
	public Clock(float timeSpeed) {
		this.timeSpeed = timeSpeed;
	}
	
	public Clock() {
		this.timeSpeed = 0.0f;
	}

	public void tick() {
		timeVal += DisplayManager.getFrameTimeSeconds() * timeSpeed;
		timeVal %= TimeOfDay.MAX_TIME.getValue();
	}
	
	public float getPreciseTime() {
		return timeVal;
	}
	
	public TimeOfDay tellTime() {
		 if (timeVal >= TimeOfDay.NIGHT.getValue() || timeVal < TimeOfDay.DAWN.getValue()) {
			 return TimeOfDay.NIGHT;
		 } else if (timeVal >= TimeOfDay.DAWN.getValue() && timeVal < TimeOfDay.MORNING.getValue()) {
			 return TimeOfDay.DAWN;
		 } else if (timeVal >= TimeOfDay.MORNING.getValue() && timeVal < TimeOfDay.NOON.getValue()) {
			 return TimeOfDay.MORNING;
		 } else if (timeVal >= TimeOfDay.NOON.getValue() && timeVal < TimeOfDay.AFTERNOON.getValue()) {
			 return TimeOfDay.NOON;
		 } else if (timeVal >= TimeOfDay.AFTERNOON.getValue() && timeVal < TimeOfDay.EVENING.getValue()) {
			 return TimeOfDay.AFTERNOON;
		 } else if (timeVal >= TimeOfDay.EVENING.getValue() && timeVal < TimeOfDay.NIGHT.getValue()) {
			 return TimeOfDay.EVENING;
		 }
		 return TimeOfDay.MORNING;
	}
	
	public void setTimeOfDay(TimeOfDay time) {
		timeVal = time.getValue();
	}

	public float getTimeSpeed() {
		return timeSpeed;
	}

	public void setTimeSpeed(float timeSpeed) {
		this.timeSpeed = timeSpeed;
	}
}
