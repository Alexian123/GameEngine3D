package com.alexian123.util.gl;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLContext;

public class TextureSampler {

	private final int type;
	private final int id;
	
	private final int width;
	private final int height;
	
	public TextureSampler(int type, int width, int height) {
		this.type = type;
		this.id = GL11.glGenTextures();
		this.width = width;
		this.height = height;
	}
	
	public TextureSampler(int width, int height, ByteBuffer pixels) {
		this.type = GL11.GL_TEXTURE_2D;
		this.id = GL11.glGenTextures();
		this.width = width;
		this.height = height;
		createSampler2D(pixels);
	}
	
	public TextureSampler(int width, int height, ByteBuffer[] faces) {
		this.type = GL13.GL_TEXTURE_CUBE_MAP;
		this.id = GL11.glGenTextures();
		this.width = width;
		this.height = height;
		createSamplerCubeMap(faces);
	}
	
	public TextureSampler withMipmapping(float lodBias) {
		bindToUnit(0);
		GL30.glGenerateMipmap(type);
		GL11.glTexParameteri(type, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
		GL11.glTexParameterf(type, GL14.GL_TEXTURE_LOD_BIAS, lodBias);
		unbind();
		return this;
	}
	
	public TextureSampler withAnisotropicFiltering() {
		bindToUnit(0);
		if (GLContext.getCapabilities().GL_EXT_texture_filter_anisotropic) {
			float amount = Math.min(4f, GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
			GL11.glTexParameterf(type, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, amount);
		} else {
			System.out.println("Anisotropic filtering is not supported on this hardware");
		}
		unbind();
		return this;
	}
	
	public TextureSampler withLinearMinMagFilter() {
		bindToUnit(0);
		GL11.glTexParameteri(type, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(type, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		unbind();
		return this;
	}
	
	public TextureSampler withClampToEdgeWrapping() {
		bindToUnit(0);
		GL11.glTexParameteri(type, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(type, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		if (type == GL13.GL_TEXTURE_CUBE_MAP) {
			GL11.glTexParameteri(type, GL12.GL_TEXTURE_WRAP_R, GL12.GL_CLAMP_TO_EDGE);
		}
		unbind();
		return this;
	}

	public int getType() {
		return type;
	}

	public int getID() {
		return id;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void delete() {
		GL11.glDeleteTextures(id);
	}
	
	public void bind() {
		GL11.glBindTexture(type, id);
	}
	
	public void bindToUnit(int unit) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + unit);
		bind();
	}
	
	public void unbind() {
		GL11.glBindTexture(type, 0);
	}
	
	private void createSampler2D(ByteBuffer pixels) {
		bindToUnit(0);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, 
				width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);
		unbind();
	}
	
	public void createSamplerCubeMap(ByteBuffer[] faces) {
		bindToUnit(0);
		for (int i = 0; i < faces.length; ++i) {
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, 
					width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, faces[i]);
		}
		unbind();
	}
}
