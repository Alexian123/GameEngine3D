package com.alexian123.rendering.postProcessing;

import com.alexian123.rendering.ImageRenderer;
import com.alexian123.shader.ShaderProgram;
import com.alexian123.util.enums.UniformName;
import com.alexian123.util.gl.TextureSampler;
import com.alexian123.util.gl.uniforms.UniformFloat;
import com.alexian123.util.gl.uniforms.UniformInt;

public class CombineFilter implements IDualInputFilter {
	
	private static final String VERTEX_SHADER_FILE = "simple_quad";
	private static final String FRAGMENT_SHADER_FILE = "combine_filter";
	
	private static final int NUM_TEXTURES = 2;
	
	private final TextureSampler[] textures = new TextureSampler[NUM_TEXTURES];
	
	private final ImageRenderer renderer;
	
	private final ShaderProgram shader;
	private final UniformFloat combineFactor;
	private final UniformInt texture1, texture2;
	
	public CombineFilter(float factor, int targetFboWidth, int targetFboHeight) {
		renderer = new ImageRenderer(targetFboWidth, targetFboHeight);
		shader = new ShaderProgram(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
		int id = shader.getProgramID();
		combineFactor = new UniformFloat(UniformName.COMBINE_FACTOR, id);
		texture1 = new UniformInt(UniformName.TEXTURE_1, id);
		texture2 = new UniformInt(UniformName.TEXTURE_2, id);
		
		shader.start();
		combineFactor.load(factor);
		texture1.load(0);
		texture2.load(1);
		shader.stop();
	}
	
	public CombineFilter(float factor) {
		renderer = new ImageRenderer();
		shader = new ShaderProgram(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
		int id = shader.getProgramID();
		combineFactor = new UniformFloat(UniformName.COMBINE_FACTOR, id);
		texture1 = new UniformInt(UniformName.TEXTURE_1, id);
		texture2 = new UniformInt(UniformName.TEXTURE_2, id);
		
		shader.start();
		combineFactor.load(factor);
		texture1.load(0);
		texture2.load(1);
		shader.stop();
	}
	
	@Override
	public TextureSampler run(TextureSampler sampler1, TextureSampler sampler2) {
		textures[0] = sampler1;
		textures[1] = sampler2;
		shader.start();
		for (int i = 0; i < NUM_TEXTURES; ++i) {
			textures[i].bindToUnit(i);
		}
		renderer.renderQuad();
		shader.stop();
		return renderer.getOutputTexture();
	}
	
	@Override
	public void cleanup() {
		shader.cleanup();
	}

}