package com.alexian123.rendering.postProcessing;

import java.util.HashMap;
import java.util.Map;

import com.alexian123.rendering.ImageRenderer;
import com.alexian123.shader.ShaderProgram;
import com.alexian123.util.enums.AttributeName;
import com.alexian123.util.enums.UniformName;
import com.alexian123.util.gl.TextureSampler;
import com.alexian123.util.gl.uniforms.UniformFloat;

public class HorizontalBlur {
	
	private static final String VERTEX_SHADER_FILE = "blur_h";
	private static final String FRAGMENT_SHADER_FILE = "blur";
	
	private final Map<Integer, AttributeName> attributes = new HashMap<>();

	private final ImageRenderer renderer;
	
	private final ShaderProgram shader;
	private UniformFloat targetWidth;

	
	public HorizontalBlur(int targetFboWidth, int targetFboHeight) {
		renderer = new ImageRenderer(targetFboWidth, targetFboHeight);
		
		attributes.put(0, AttributeName.POSITION);
		
		shader = new ShaderProgram(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE, attributes);
		targetWidth = new UniformFloat(UniformName.TARGET_WIDTH, shader.getProgramID());
		
		shader.start();
		targetWidth.load((float) targetFboWidth);
		shader.stop();
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
