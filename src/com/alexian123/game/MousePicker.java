package com.alexian123.game;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.alexian123.terrain.Terrain;
import com.alexian123.terrain.TerrainGrid;
import com.alexian123.util.Constants;

public class MousePicker {
	
	private static final int RECURSION_COUNT = 200;
	private static final float RAY_RANGE = 600;
	
	private final TerrainGrid terrainGrid;
	
	private Vector3f cameraPosition = new Vector3f();
	private Matrix4f viewMatrix = new Matrix4f();
	
	private Vector3f currentRay = new Vector3f();
	private Vector3f currentTerrainPoint = new Vector3f();
	
	
	public MousePicker(TerrainGrid terrainGrid) {
		this.terrainGrid = terrainGrid;
	}

	public Vector3f getCurrentRay() {
		return currentRay;
	}
	
	public Vector3f getCurrentTerrainPoint() {
		return currentTerrainPoint;
	}
	
	public void update(Camera camera) {
		cameraPosition = camera.getPosition();
		viewMatrix = camera.getViewMatrix();
		currentRay = calculateMouseRay();
		if (intersectionInRange(0, RAY_RANGE, currentRay)) {
			currentTerrainPoint = binarySearch(0, 0, RAY_RANGE, currentRay);
		} else {
			currentTerrainPoint = null;
		}
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
		Matrix4f inverseProjectionMatrix = Matrix4f.invert(Constants.PROJECTION_MATRIX, null);
		Vector4f eyeCoords = Matrix4f.transform(inverseProjectionMatrix, clipCoords, null);
		return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
	}
	
	private Vector4f eyeToWorldSpace(Vector4f eyeCoords) {
		Matrix4f inverseViewMatrix = Matrix4f.invert(viewMatrix, null);
		Vector4f worldCoords = Matrix4f.transform(inverseViewMatrix, eyeCoords, null);
		return worldCoords;
	}
	
	private Vector3f getPointOnRay(Vector3f ray, float distance) {
		Vector3f start = new Vector3f(cameraPosition.x, cameraPosition.y, cameraPosition.z);
		Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);
		return Vector3f.add(start, scaledRay, null);
	}
	
	private Vector3f binarySearch(int count, float start, float finish, Vector3f ray) {
		float half = start + ((finish - start) / 2f);
		if (count >= RECURSION_COUNT) {
			Vector3f endPoint = getPointOnRay(ray, half);
			Terrain terrain = terrainGrid.getTerrainAt(endPoint.getX(), endPoint.getZ());
			if (terrain != null) {
				return endPoint;
			} else {
				return null;
			}
		}
		if (intersectionInRange(start, half, ray)) {
			return binarySearch(count + 1, start, half, ray);
		} else {
			return binarySearch(count + 1, half, finish, ray);
		}
	}

	private boolean intersectionInRange(float start, float finish, Vector3f ray) {
		Vector3f startPoint = getPointOnRay(ray, start);
		Vector3f endPoint = getPointOnRay(ray, finish);
		return !isUnderGround(startPoint) && isUnderGround(endPoint);
	}

	private boolean isUnderGround(Vector3f testPoint) {
		Terrain terrain = terrainGrid.getTerrainAt(testPoint.getX(), testPoint.getZ());
		float height = 0;
		if (terrain != null) {
			height = terrain.getHeightAtPosition(testPoint.getX(), testPoint.getZ());
		}
		return testPoint.y < height;
	}
}
