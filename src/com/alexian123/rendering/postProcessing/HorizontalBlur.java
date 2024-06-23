package com.alexian123.rendering.postProcessing;

import com.alexian123.rendering.ImageRenderer;
import com.alexian123.shader.ShaderProgram;
import com.alexian123.util.enums.UniformName;
import com.alexian123.util.gl.TextureSampler;
import com.alexian123.util.gl.uniforms.UniformFloat;

public class HorizontalBlur implements ISingleInputFilter {
	
	private static final String VERTEX_SHADER_FILE = "blur_h";
	private static final String FRAGMENT_SHADER_FILE = "blur";

	private final ImageRenderer renderer;
	
	private final ShaderProgram shader;
	private UniformFloat targetWidth;

	
	public HorizontalBlur(int targetFboWidth, int targetFboHeight) {
		renderer = new ImageRenderer(targetFboWidth, targetFboHeight);
		
		shader = new ShaderProgram(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
		targetWidth = new UniformFloat(UniformName.TARGET_WIDTH, shader.getProgramID());
		
		shader.start();
		targetWidth.load((float) targetFboWidth);
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
