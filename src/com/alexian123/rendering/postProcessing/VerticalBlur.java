package com.alexian123.rendering.postProcessing;

import com.alexian123.rendering.ImageRenderer;
import com.alexian123.shader.ShaderProgram;
import com.alexian123.util.enums.UniformName;
import com.alexian123.util.gl.TextureSampler;
import com.alexian123.util.gl.uniforms.UniformFloat;

public class VerticalBlur implements ISingleInputFilter {
	
	private static final String VERTEX_SHADER_FILE = "blur_v";
	private static final String FRAGMENT_SHADER_FILE = "blur";

	private final ImageRenderer renderer;
	
	private final ShaderProgram shader;
	private UniformFloat targetHeight;
	
	public VerticalBlur(int targetFboWidth, int targetFboHeight) {
		renderer = new ImageRenderer(targetFboWidth, targetFboHeight);
		
		shader = new ShaderProgram(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
		targetHeight = new UniformFloat(UniformName.TARGET_HEIGHT, shader.getProgramID());
		
		shader.start();
		targetHeight.load((float) targetFboHeight);
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
