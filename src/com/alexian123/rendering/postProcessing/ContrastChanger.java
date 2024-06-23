package com.alexian123.rendering.postProcessing;

import java.util.HashMap;
import java.util.Map;

import com.alexian123.rendering.ImageRenderer;
import com.alexian123.shader.ShaderProgram;
import com.alexian123.util.enums.AttributeName;
import com.alexian123.util.enums.UniformName;
import com.alexian123.util.gl.TextureSampler;
import com.alexian123.util.gl.uniforms.UniformFloat;

public class ContrastChanger {
	
	private static final String VERTEX_SHADER_FILE = "simple_quad";
	private static final String FRAGMENT_SHADER_FILE = "contrast";

	private final Map<Integer, AttributeName> attributes = new HashMap<>();

	private final ImageRenderer renderer;
	
	private final ShaderProgram shader;
	private final UniformFloat contrast;
	
	public ContrastChanger(float contrastValue) {
		renderer = new ImageRenderer();
		
		attributes.put(0, AttributeName.POSITION);
		shader = new ShaderProgram(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE, attributes);
		contrast = new UniformFloat(UniformName.CONTRAST, shader.getProgramID());
		
		shader.start();
		contrast.load(contrastValue);
		shader.stop();
	}

	public void run(TextureSampler texture) {
		shader.start();
		texture.bindToUnit(0);
		renderer.renderQuad();
		shader.stop();
	}
	
	public void cleanup() {
		shader.cleanup();
	}
}
