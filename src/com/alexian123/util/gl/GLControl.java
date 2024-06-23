package com.alexian123.util.gl;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;

public class GLControl {

	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	public static void enableDepthTest() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	
	public static void disableDepthTest() {
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	public static void clearDepthBuffer() {
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public static void clearColorBuffer() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}
	
	public static void clearColorAndDepthBuffers() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public static void enableClipDistance(int unit) {
		GL11.glEnable(GL30.GL_CLIP_DISTANCE0 + unit);
	}
	
	public static void disableClipDistace(int unit) {
		GL11.glDisable(GL30.GL_CLIP_DISTANCE0 + unit);
	}
	
	public static void enableBlending() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public static void enableAdditiveBlending() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
	}
	
	public static void disableBlending() {
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	public static void enableDepthMask() {
		GL11.glDepthMask(true);
	}
	
	public static void disableDepthMask() {
		GL11.glDepthMask(false);
	}
	
	public static void clearColor(float x, float y, float z) {
		GL11.glClearColor(x, y, z, 1.0f);
	}
	
	public static void drawElementsT(int count) {
		GL11.glDrawElements(GL11.GL_TRIANGLES, count, GL11.GL_UNSIGNED_INT, 0);
	}
	
	public static void drawArraysT(int count) {
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, count);
	}
	
	public static void drawArraysTS(int count) {
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, count);
	}
	
	public static void drawArraysInstancedTS(int count, int size) {
		GL31.glDrawArraysInstanced(GL11.GL_TRIANGLE_STRIP, 0, count, size);
	}
}
