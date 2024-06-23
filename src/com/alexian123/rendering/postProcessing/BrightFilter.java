package com.alexian123.rendering.postProcessing;

import java.util.HashMap;
import java.util.Map;

import com.alexian123.rendering.ImageRenderer;
import com.alexian123.shader.ShaderProgram;
import com.alexian123.util.enums.AttributeName;
import com.alexian123.util.gl.TextureSampler;

public class BrightFilter {
	
	private static final String VERTEX_SHADER_FILE = "simple_quad";
	private static final String FRAGMENT_SHADER_FILE = "bright_filter";
	
	private final Map<Integer, AttributeName> attributes = new HashMap<>();

	private final ImageRenderer renderer;
	
	private final ShaderProgram shader;
	
	public BrightFilter(int width, int height) {
		renderer = new ImageRenderer(width, height);
		attributes.put(0, AttributeName.POSITION);
		shader = new ShaderProgram(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE, attributes);
	}
	
	public void run(TextureSampler texture) {
		shader.start();
		texture.bindToUnit(0);
		renderer.renderQuad();
		shader.stop();
	}
	
	public TextureSampler getOutputTexture() {
		return renderer.getOutputTexture();
	}
	
	public void cleanup() {
		shader.cleanup();
	}
}
	