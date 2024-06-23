package com.alexian123.loader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.alexian123.engine.GameManager;
import com.alexian123.font.FontType;
import com.alexian123.loader.data.AnimatedMeshData;
import com.alexian123.loader.data.ModelData;
import com.alexian123.loader.data.ModelDataNM;
import com.alexian123.loader.data.TextureData;
import com.alexian123.model.ModelMesh;
import com.alexian123.util.gl.TextureSampler;
import com.alexian123.util.gl.Vao;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class Loader {
	
	private final List<Vao> vaos = new ArrayList<>();
	private final List<TextureSampler> textures = new ArrayList<>();
	
	public ModelMesh loadToVao(float[] vertices, float[] textureCoords, float[] normals, int[] indices, float modelHeight) {
		Vao vao = new Vao();
		vao.bind();
		vao.createIndicesBuffer(indices);
		vao.addAttribute(0, 3, vertices);
		vao.addAttribute(1, 2, textureCoords);
		vao.addAttribute(2, 3, normals);
		vao.unbind();
		vaos.add(vao);
		return new ModelMesh(vao, modelHeight);
	}
	
	public ModelMesh loadToVao(float[] vertices, float[] textureCoords, float[] normals, float[] tangents, int[] indices, float modelHeight) {
		Vao vao = new Vao();
		vao.bind();
		vao.createIndicesBuffer(indices);
		vao.addAttribute(0, 3, vertices);
		vao.addAttribute(1, 2, textureCoords);
		vao.addAttribute(2, 3, normals);
		vao.addAttribute(3, 3, tangents);
		vao.unbind();
		vaos.add(vao);
		return new ModelMesh(vao, modelHeight);
	}
	
	public ModelMesh loadToVao(float[] vertices, float[] textureCoords, float[] normals, int[] jointIds, float[] vertexWeights, int[] indices) {
		Vao vao = new Vao();
		vao.bind();
		vao.createIndicesBuffer(indices);
		vao.addAttribute(0, 3, vertices);
		vao.addAttribute(1, 2, textureCoords);
		vao.addAttribute(2, 3, normals);
		vao.addAttribute(4, 3, jointIds);
		vao.addAttribute(5, 3, vertexWeights);
		vao.unbind();
		vaos.add(vao);
		return new ModelMesh(vao);
	}
	
	public Vao loadToVao(float[] vertices, float[] textureCoords) {
		Vao vao = new Vao();
		vao.bind();
		vao.addAttribute(0, 2, vertices);
		vao.addAttribute(1, 2, textureCoords);
		vao.unbind();
		vaos.add(vao);
		return vao;
	}
	
	public ModelMesh loadToVao(float[] vertices, int dimensions) {
		Vao vao = new Vao();
		vao.bind();
		vao.addAttribute(0, dimensions, vertices);
		vao.unbind();
		vao.setIndexCount(vertices.length / dimensions);
		vaos.add(vao);
		return new ModelMesh(vao);
	}
	
	public ModelMesh loadToVao(ModelData data) {
		return loadToVao(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices(), data.calculateHeight());
	}
	
	public ModelMesh loadToVao(ModelDataNM data) {
		return loadToVao(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getTangents(), data.getIndices(), data.calculateHeight());
	}
	
	public ModelMesh loadToVao(AnimatedMeshData data) {
		return loadToVao(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getJointIds(), data.getVertexWeights(), data.getIndices());
	}
	
	public TextureSampler loadTexture(String fileName) {
		TextureData data = decodeTexture(GameManager.SETTINGS.texturesDir + fileName + GameManager.SETTINGS.textureFileExtension);
		TextureSampler sampler = new TextureSampler(data.getWidth(), data.getHeight(), data.getBuffer())
				.withMipmapping(0f)
				.withAnisotropicFiltering();
		return sampler;
	}
	
	public FontType loadFont(String fontName) {
		TextureData data = decodeTexture(GameManager.SETTINGS.fontsDir + fontName + GameManager.SETTINGS.textureFileExtension);
		TextureSampler sampler = new TextureSampler(data.getWidth(), data.getHeight(), data.getBuffer())
				.withMipmapping(0f);
		return new FontType(sampler, GameManager.SETTINGS.fontsDir + fontName + GameManager.SETTINGS.fontFileExtension);
	}
	
	/** 
	 * Order of file names (cube faces): Right, Left, Top, Bottom, Back, Front
	 * @param fileNames
	 * @return cube map id
	 */
	public TextureSampler loadCubeMap(String[] fileNames) {
		ByteBuffer[] faces = new ByteBuffer[fileNames.length];
		int width = 0, height = 0;
		for (int i = 0; i < fileNames.length; ++i) {
			TextureData data = decodeTexture(GameManager.SETTINGS.texturesDir + fileNames[i] + GameManager.SETTINGS.textureFileExtension);
			width = data.getWidth();
			height = data.getHeight();
			faces[i] = data.getBuffer();
		}
		TextureSampler sampler = new TextureSampler(width, height, faces)
				.withLinearMinMagFilter()
				.withClampToEdgeWrapping();
		return sampler;
	}
	
	public void cleanup() {
		for (Vao vao : vaos) {
			vao.delete();
		}
		vaos.clear();
		for (TextureSampler texture : textures) {
			texture.delete();
		}
		textures.clear();
	}
	
	private TextureData decodeTexture(String fileName) {
		int width = 0, height = 0;
		ByteBuffer buffer = null;
		try {
			InputStream in = Loader.class.getResourceAsStream(fileName);
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
