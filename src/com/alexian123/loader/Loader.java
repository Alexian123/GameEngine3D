package com.alexian123.loader;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import com.alexian123.model.ModelData;
import com.alexian123.model.ModelDataNM;
import com.alexian123.model.RawModel;
import com.alexian123.texture.TextureData;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class Loader {
	
	private final List<Integer> vaos = new ArrayList<>();
	private final List<Integer> vbos = new ArrayList<>();
	private final List<Integer> textures = new ArrayList<>();
	
	public RawModel loadToVao(float[] vertices, float[] textureCoords, float[] normals, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, vertices);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}
	
	public RawModel loadToVao(float[] vertices, float[] textureCoords, float[] normals, float[] tangents, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, vertices);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		storeDataInAttributeList(3, 3, tangents);
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}
	
	public RawModel loadToVao(ModelData data) {
		return loadToVao(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
	}
	
	public RawModel loadToVao(ModelDataNM data) {
		return loadToVao(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getTangents(), data.getIndices());
	}
	
	public RawModel loadToVao(float[] vertices, int dimensions) {
		int vaoID = createVAO();
		this.storeDataInAttributeList(0, dimensions, vertices);
		unbindVAO();
		return new RawModel(vaoID, vertices.length / dimensions);
	}
	
	public int loadTexture(String fileName) {
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream("res/textures/" + fileName + ".png"));
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -2.4f);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error loading texture: " + fileName);
			System.exit(-1);
		}
		int textureID = texture.getTextureID();
		textures.add(textureID);
		return textureID;
	}
	
	/** 
	 * Order of file names (cube faces): Right, Left, Top, Bottom, Back, Front
	 * @param fileNames
	 * @return id
	 */
	public int loadCubeMap(String[] fileNames) {
		int id = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, id);
		for (int i = 0; i < fileNames.length; ++i) {
			TextureData data = decodeTexture("res/textures/" + fileNames[i] + ".png");
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, 
					data.getWidth(), data.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
			
		}
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		textures.add(id);
		return id;
	}
	
	public void cleanup() {
		for (int vao : vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		for (int vbo : vbos) {
			GL15.glDeleteBuffers(vbo);
		}
		for (int texture : textures) {
			GL11.glDeleteTextures(texture);
		}
		vaos.clear();
		vbos.clear();
		textures.clear();
	}
	
	private int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}

	private void storeDataInAttributeList(int attributeNum, int size, float[] data) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNum, size, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	private void unbindVAO() {
		GL30.glBindVertexArray(0);
	}
	
	private void bindIndicesBuffer(int[] indices) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	private static IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer ib = BufferUtils.createIntBuffer(data.length);
		ib.put(data);
		ib.flip();
		return ib;
	}
	
	private static FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer fb = BufferUtils.createFloatBuffer(data.length);
		fb.put(data);
		fb.flip();
		return fb;
	}
	
	private TextureData decodeTexture(String fileName) {
		int width = 0, height = 0;
		ByteBuffer buffer = null;
		try {
			FileInputStream in = new FileInputStream(fileName);
			PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			buffer = ByteBuffer.allocateDirect(width * height * 4);
			decoder.decode(buffer, width * 4, Format.RGBA);
			buffer.flip();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error loading texture: " + fileName);
			System.exit(-1);
		}
		return new TextureData(width, height, buffer);
	}
}
