package com.alexian123.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import com.alexian123.font.FontType;
import com.alexian123.model.RawModel;
import com.alexian123.texture.TextureData;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class Loader {
	
	private final Map<Integer, List<Integer>> vaos = new HashMap<>();
	private final List<Integer> textures = new ArrayList<>();
	
	public void addInstancedAttribute(int vao, int vbo, int attribNo, int size, int length, int offset) {
		List<Integer> vbos = vaos.get(vao);
		if (!vbos.contains(vbo)) {
			vbos.add(vbo);
		}
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL30.glBindVertexArray(vao);
		GL20.glVertexAttribPointer(attribNo, size, GL11.GL_FLOAT, false, length * 4, offset * 4);
		GL33.glVertexAttribDivisor(attribNo, 1);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
	}
	
	public void updateVbo(int vbo, float[] data, FloatBuffer buffer) {
		buffer.clear();
		buffer.put(data);
		buffer.flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer.capacity() * 4, GL15.GL_STREAM_DRAW);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, buffer);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	public int createEmptyVbo(int size) {
		int vbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, size * 4, GL15.GL_STREAM_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		return vbo;
	}
	
	public RawModel loadToVao(float[] vertices, float[] textureCoords, float[] normals, int[] indices) {
		int vaoID = createVao();
		bindIndicesBuffer(indices, vaos.get(vaoID));
		storeDataInAttributeList(0, 3, vertices, vaos.get(vaoID));
		storeDataInAttributeList(1, 2, textureCoords, vaos.get(vaoID));
		storeDataInAttributeList(2, 3, normals, vaos.get(vaoID));
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}
	
	public RawModel loadToVao(float[] vertices, float[] textureCoords, float[] normals, float[] tangents, int[] indices) {
		int vaoID = createVao();
		bindIndicesBuffer(indices, vaos.get(vaoID));
		storeDataInAttributeList(0, 3, vertices, vaos.get(vaoID));
		storeDataInAttributeList(1, 2, textureCoords, vaos.get(vaoID));
		storeDataInAttributeList(2, 3, normals, vaos.get(vaoID));
		storeDataInAttributeList(3, 3, tangents, vaos.get(vaoID));
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}
	
	public int loadToVao(float[] vertices, float[] textureCoords) {
		int vaoID = createVao();
		storeDataInAttributeList(0, 2, vertices, vaos.get(vaoID));
		storeDataInAttributeList(1, 2, textureCoords, vaos.get(vaoID));
		unbindVAO();
		return vaoID;
	}
	
	public RawModel loadToVao(float[] vertices, int dimensions) {
		int vaoID = createVao();
		storeDataInAttributeList(0, dimensions, vertices, vaos.get(vaoID));
		unbindVAO();
		return new RawModel(vaoID, vertices.length / dimensions);
	}
	
	public RawModel loadToVao(ModelData data) {
		return loadToVao(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
	}
	
	public RawModel loadToVao(ModelDataNM data) {
		return loadToVao(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getTangents(), data.getIndices());
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
	
	public FontType loadFont(String fontName) {
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream("res/fonts/" + fontName + ".png"));
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0f);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error loading font: " + fontName);
			System.exit(-1);
		}
		int textureID = texture.getTextureID();
		textures.add(textureID);
		return new FontType(textureID, new File("res/fonts/" + fontName + ".fnt"));
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
		for (int vao : vaos.keySet()) {
			deleteVao(vao);
		}
		vaos.clear();
		for (int texture : textures) {
			GL11.glDeleteTextures(texture);
		}
		textures.clear();
	}
	
	public void deleteVao(int vaoID) {
		List<Integer> vbos = vaos.get(vaoID);
		if (vbos != null) {
			for (int vbo : vbos) {
				GL15.glDeleteBuffers(vbo);
			}
			vbos.clear();
			GL30.glDeleteVertexArrays(vaoID);
		}
	}
	
	private int createVao() {
		int vaoID = GL30.glGenVertexArrays();
		vaos.put(vaoID, new ArrayList<Integer>());
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}

	private void storeDataInAttributeList(int attributeNum, int size, float[] data, List<Integer> vbos) {
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
	
	private void bindIndicesBuffer(int[] indices, List<Integer> vbos) {
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
