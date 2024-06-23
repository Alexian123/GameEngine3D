package com.alexian123.rendering.postProcessing;

import com.alexian123.rendering.ImageRenderer;
import com.alexian123.shader.ShaderProgram;
import com.alexian123.util.enums.UniformName;
import com.alexian123.util.gl.TextureSampler;
import com.alexian123.util.gl.uniforms.UniformFloat;

public class ContrastChanger implements ISingleInputFilter {
	
	private static final String VERTEX_SHADER_FILE = "simple_quad";
	private static final String FRAGMENT_SHADER_FILE = "contrast";

	private final ImageRenderer renderer;
	
	private final ShaderProgram shader;
	private final UniformFloat contrast;
	
	public ContrastChanger(float contrastVal, int targetFboWidth, int targetFboHeight) {
		renderer = new ImageRenderer(targetFboWidth, targetFboHeight);
		
		shader = new ShaderProgram(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
		contrast = new UniformFloat(UniformName.CONTRAST, shader.getProgramID());
		
		shader.start();
		contrast.load(contrastVal);
		shader.stop();
	}
	
	public ContrastChanger(float contrastVal) {
		renderer = new ImageRenderer();
		
		shader = new ShaderProgram(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
		contrast = new UniformFloat(UniformName.CONTRAST, shader.getProgramID());
		
		shader.start();
		contrast.load(contrastVal);
		shader.stop();
	}

	@Override
	public TextureSampler run(TextureSampler texture) {
		shader.start();
		texture.bindToUnit(0);
		renderer.renderQuad();
		shader.stop();
		return renderer.getOutputTexture();
	}
	
	@Override
	public void cleanup() {
		shader.cleanup();
	}
}
