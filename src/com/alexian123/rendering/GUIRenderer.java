package com.alexian123.rendering;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alexian123.loader.Loader;
import com.alexian123.model.ModelMesh;
import com.alexian123.shader.ShaderProgram;
import com.alexian123.texture.GUITexture;
import com.alexian123.util.enums.AttributeName;
import com.alexian123.util.enums.UniformName;
import com.alexian123.util.gl.GLControl;
import com.alexian123.util.gl.uniforms.UniformMat4;
import com.alexian123.util.mathematics.MatrixCreator;

public class GUIRenderer {
	
	private static final String VERTEX_SHADER_FILE = "gui";
	private static final String FRAGMENT_SHADER_FILE = "gui";
	
	private final Map<Integer, AttributeName> attributes = new HashMap<>();
	
	private final ModelMesh quad;
	
	private final ShaderProgram shader;
	private final UniformMat4 transformationMatrix;
	
	public GUIRenderer(Loader loader) {
		attributes.put(0, AttributeName.POSITION);
		quad = loader.loadToVao(new float[] { -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f, -1.0f }, 2);
		shader = new ShaderProgram(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE, attributes);
		transformationMatrix = new UniformMat4(UniformName.TRANSFORMATION_MATRIX, shader.getProgramID());
	}
	
	public void render(List<GUITexture> guis) {
		shader.start();
		quad.getVao().bind(0);
		GLControl.enableBlending();
		GLControl.disableDepthTest();
		for (GUITexture gui : guis) {
			gui.getSampler().bindToUnit(0);
			transformationMatrix.load(MatrixCreator.createTransformationMatrix(gui.getPosition(), gui.getScale()));
			GLControl.drawArraysTS(quad.getVertexCount());
		}
		GLControl.enableDepthTest();
		GLControl.disableBlending();
		quad.getVao().unbind(0);
		shader.stop();
	}

	public void cleanup() {
		shader.cleanup();
	}
}
