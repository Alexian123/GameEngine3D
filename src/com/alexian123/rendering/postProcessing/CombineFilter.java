package com.alexian123.rendering.postProcessing;

import java.util.HashMap;
import java.util.Map;

import com.alexian123.rendering.ImageRenderer;
import com.alexian123.shader.ShaderProgram;
import com.alexian123.util.Constants;
import com.alexian123.util.enums.AttributeName;
import com.alexian123.util.enums.UniformName;
import com.alexian123.util.gl.TextureSampler;
import com.alexian123.util.gl.uniforms.UniformFloat;
import com.alexian123.util.gl.uniforms.UniformInt;

public class CombineFilter {
	
	private static final String VERTEX_SHADER_FILE = "simple_quad";
	private static final String FRAGMENT_SHADER_FILE = "combine_filter";
	
	private static final int NUM_TEXTURES = 2;
	
	private final Map<Integer, AttributeName> attributes = new HashMap<>();
	
	private final TextureSampler[] textures = new TextureSampler[NUM_TEXTURES];
	
	private final ImageRenderer renderer;
	
	private final ShaderProgram shader;
	private final UniformFloat bloomFactor;
	private final UniformInt colorTexture, highlightTexture;
	
	public CombineFilter() {
		renderer = new ImageRenderer();
		attributes.put(0, AttributeName.POSITION);
		shader = new ShaderProgram(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE, attributes);
		int id = shader.getProgramID();
		bloomFactor = new UniformFloat(UniformName.BLOOM_FACTOR, id);
		colorTexture = new UniformInt(UniformName.COLOR_TEXTURE, id);
		highlightTexture = new UniformInt(UniformName.HIGHLIGHT_TEXTURE, id);
		
		shader.start();
		bloomFactor.load(Constants.BLOOM_FACTOR);
		colorTexture.load(0);
		highlightTexture.load(1);
		shader.stop();
	}
	
	public void run(TextureSampler colourTexture, TextureSampler highlightTexture) {
		textures[0] = colourTexture;
		textures[1] = highlightTexture;
		shader.start();
		for (int i = 0; i < NUM_TEXTURES; ++i) {
			textures[i].bindToUnit(i);
		}
		renderer.renderQuad();
		shader.stop();
	}
	
	public void cleanup() {
		shader.cleanup();
	}

}