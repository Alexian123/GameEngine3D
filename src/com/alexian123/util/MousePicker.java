package com.alexian123.util;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.alexian123.entity.Camera;

public class MousePicker {
	
	private final Matrix4f projectionMatrix;
	private final Camera camera;

	private Vector3f currentRay;
	private Matrix4f viewMatrix;
	
	public MousePicker(Matrix4f projectionMatrix, Camera camera) {
		this.projectionMatrix = projectionMatrix;
		this.camera = camera;
		this.viewMatrix = Maths.createViewMatrix(camera);
	}

	public Vector3f getCurrentRay() {
		return currentRay;
	}
	
	public void update() {
		viewMatrix = Maths.createViewMatrix(camera);
		currentRay = calculateMouseRay();
	}
	
	
	private Vector3f calculateMouseRay() {
		float mouseX = Mouse.getX();
		float mouseY = Mouse.getY();
		Vector2f normalizedCoords = getNormalizedDeviceCoords(mouseX, mouseY);
		Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1f, 1f);
		Vector4f eyeCoords = clipToEyeSpace(clipCoords);
		Vector4f worldCoords = eyeToWorldSpace(eyeCoords);
		Vector3f mouseRay = new Vector3f(worldCoords.x, worldCoords.y, worldCoords.z);
		mouseRay.normalise();
		return mouseRay;
	}
	
	private Vector2f getNormalizedDeviceCoords(float mouseX, float mouseY) {
		float x = (2f * mouseX) / Display.getWidth() - 1f;
		float y = (2f * mouseY) / Display.getHeight() - 1f;
		return new Vector2f(x, y);
	}
	
	private Vector4f clipToEyeSpace(Vector4f clipCoords) {
		Matrix4f inverseProjectionMatrix = Matrix4f.invert(projectionMatrix, null);
		Vector4f eyeCoords = Matrix4f.transform(inverseProjectionMatrix, clipCoords, null);
		return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
	}
	
	private Vector4f eyeToWorldSpace(Vector4f eyeCoords) {
		Matrix4f inverseViewMatrix = Matrix4f.invert(viewMatrix, null);
		Vector4f worldCoords = Matrix4f.transform(inverseViewMatrix, eyeCoords, null);
		return worldCoords;
	}
}
