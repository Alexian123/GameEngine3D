package com.alexian123.util.gl;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;

public class Vao {
	
	private final List<Vbo> dataVbos = new ArrayList<>();

	private final int id;
	
	private Vbo indexVbo = null;
	private int indexCount = 0;
	
	private Vbo instancedVbo = null;
	private FloatBuffer instancedVboData;
	
	public Vao() {
		this.id = GL30.glGenVertexArrays();
	}
	
	public int getID() {
		return id;
	}
	
	public void delete() {
		GL30.glDeleteVertexArrays(id);
		for (Vbo vbo : dataVbos) {
			vbo.delete();
		}
		dataVbos.clear();
		if (indexVbo != null) {
			indexVbo.delete();
		}
		if (instancedVbo != null) {
			instancedVbo.delete();
		}
		if (instancedVboData != null) {
			instancedVboData.clear();
		}
	}

	public void bind(int... attributes) {
		bindVao();
		for (int attribNo : attributes) {
			GL20.glEnableVertexAttribArray(attribNo);
		}
	}
	
	public void unbind(int... attributes) {
		for (int attribNo : attributes) {
			GL20.glDisableVertexAttribArray(attribNo);
		}
		unbindVao();
	}
	
	public void setIndexCount(int indexCount) {
		this.indexCount = indexCount;
	}
	
	public int getIndexCount() {
		return indexCount;
	}
	
	public void addAttribute(int attributeNo, int attributeSize, float[] data) {
		Vbo vbo = new Vbo(GL15.GL_ARRAY_BUFFER);
		vbo.bind();
		vbo.store(data);
		GL20.glVertexAttribPointer(attributeNo, attributeSize, GL11.GL_FLOAT, false, 0, 0);
		vbo.unbind();
		dataVbos.add(vbo);
	}
	
	public void addAttribute(int attributeNo, int attributeSize, int[] data) {
		Vbo vbo = new Vbo(GL15.GL_ARRAY_BUFFER);
		vbo.bind();
		vbo.store(data);
		GL30.glVertexAttribIPointer(attributeNo, attributeSize, GL11.GL_INT, attributeSize * 4, 0);
		vbo.unbind();
		dataVbos.add(vbo);
	}
	
	public void createIndicesBuffer(int[] indices) {
		if (indexVbo != null) {
			indexVbo.delete();
		}
		indexVbo = new Vbo(GL15.GL_ELEMENT_ARRAY_BUFFER);
		indexVbo.bind();
		indexVbo.store(indices);
		indexCount = indices.length;
	}
	
	public void createInstancedBuffer(int size) {
		if (instancedVbo != null) {
			instancedVbo.delete();
		}
		instancedVbo = Vbo.createEmptyVbo(size);
		instancedVboData = BufferUtils.createFloatBuffer(size);
	}
	
	public void addInstancedAttribute(int attribNo, int attributeSize, int dataLength, int dataOffset) {
		if (instancedVbo == null) {
			System.err.println("Instanced Buffer does not exist.");
			return;
		}
		bindVao();
		instancedVbo.bind();
		GL20.glVertexAttribPointer(attribNo, attributeSize, GL11.GL_FLOAT, false, dataLength * 4, dataOffset * 4);
		GL33.glVertexAttribDivisor(attribNo, 1);	
		instancedVbo.unbind();
		unbindVao();
	}
	
	public void updateInstancedBuffer(float[] data) {
		instancedVboData.clear();
		instancedVboData.put(data);
		instancedVboData.flip();
		instancedVbo.bind();
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, instancedVboData.capacity() * 4, GL15.GL_STREAM_DRAW);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, instancedVboData);
		instancedVbo.unbind();
	}
	
	private void bindVao() {
		GL30.glBindVertexArray(id);
	}
	
	private void unbindVao() {
		GL30.glBindVertexArray(0);
	}
}
