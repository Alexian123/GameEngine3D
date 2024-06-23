package com.alexian123.util.mathematics;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class MatrixCreator {
	
	public static Matrix4f createProjectionMatrix(float fov, float farPlane, float nearPlane, float displayWidth, float displayHeight) {
		float aspectRatio = displayWidth / displayHeight;
		float xScale = (float) (1.0f / Math.tan(Math.toRadians(fov / 2.0f)));
		float yScale = xScale * aspectRatio;
		float frustumLength = farPlane - nearPlane;
		Matrix4f projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = xScale;
		projectionMatrix.m11 = yScale;
		projectionMatrix.m22 = -((farPlane + nearPlane) / frustumLength);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * nearPlane * farPlane) / frustumLength);
		projectionMatrix.m33 = 0;
		return projectionMatrix;
	}
	
	public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
		return matrix;
	}
	
	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
		return matrix;
	}
	
	public static Matrix4f createViewMatrix(Vector3f position, float pitch, float yaw) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(pitch), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(yaw), new Vector3f(0, 1, 0), matrix, matrix);
		Vector3f negPos = new Vector3f(-position.x, -position.y, -position.z);
		Matrix4f.translate(negPos, matrix, matrix);
		return matrix;
	}
}
