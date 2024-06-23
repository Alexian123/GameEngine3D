package com.alexian123.util.gl;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

public class Vbo {
	
	private final int id;
	private final int type;
	
	private FloatBuffer floatBuffer;
	private IntBuffer intBuffer;
	
	public Vbo(int type) {
		this.id =  GL15.glGenBuffers();
		this.type = type;
	}
	
	public int getID() {
		return id;
	}
	
	public void bind() {
		GL15.glBindBuffer(type, id);
	}
	
	public void unbind() {
		GL15.glBindBuffer(type, 0);
	}	
	
	public void delete() {
		GL15.glDeleteBuffers(id);
	}
	
	public void store(float[] data) {
		storeDataInFloatBuffer(data);
		GL15.glBufferData(type, floatBuffer, GL15.GL_STATIC_DRAW);
	}
	
	public void store(int[] data) {
		storeDataInIntBuffer(data);
		GL15.glBufferData(type, intBuffer, GL15.GL_STATIC_DRAW);
	}
	
	private void storeDataInFloatBuffer(float[] data) {
		floatBuffer = BufferUtils.createFloatBuffer(data.length);
		floatBuffer.put(data);
		floatBuffer.flip();
	}
	
	private void storeDataInIntBuffer(int[] data) {
		intBuffer = BufferUtils.createIntBuffer(data.length);
		intBuffer.put(data);
		intBuffer.flip();
	}
	
	public static Vbo createEmptyVbo(int size) {
		Vbo vbo = new Vbo(GL15.GL_ARRAY_BUFFER);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo.getID());
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, size * 4, GL15.GL_STREAM_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		return vbo;
	}
}
