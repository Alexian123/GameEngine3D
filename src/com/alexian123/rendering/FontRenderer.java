package com.alexian123.rendering;

import java.util.List;
import java.util.Map;

import com.alexian123.font.FontType;
import com.alexian123.font.GUIText;
import com.alexian123.shader.ShaderProgram;
import com.alexian123.util.enums.UniformName;
import com.alexian123.util.gl.GLControl;
import com.alexian123.util.gl.Vao;
import com.alexian123.util.gl.uniforms.UniformFloat;
import com.alexian123.util.gl.uniforms.UniformVec2;
import com.alexian123.util.gl.uniforms.UniformVec3;

public class FontRenderer {
	
	private static final String VERTEX_SHADER_FILE = "font";
	private static final String FRAGMENT_SHADER_FILE = "font";
	
	private final ShaderProgram shader;
	private final UniformVec2 translation, offset;
	private final UniformVec3 color, outlineColor;
	private final UniformFloat charWidth, charEdge, borderWidth, borderEdge;

	public FontRenderer() {
		shader = new ShaderProgram(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
		int id = shader.getProgramID();
		translation = new UniformVec2(UniformName.TRANSLATION, id);
		offset = new UniformVec2(UniformName.OFFSET, id);
		color = new UniformVec3(UniformName.COLOR, id);
		outlineColor = new UniformVec3(UniformName.OUTLINE_COLOR, id);
		charWidth = new UniformFloat(UniformName.CHARACTER_WIDTH, id);
		charEdge = new UniformFloat(UniformName.CHARACTER_EDGE, id);
		borderWidth = new UniformFloat(UniformName.BORDER_WIDTH, id);
		borderEdge = new UniformFloat(UniformName.BORDER_EDGE, id);
	}
	
	public void render(Map<FontType, List<GUIText>> texts) {
		prepare();
		for (FontType font : texts.keySet()) {
			font.getTextureAtlas().bindToUnit(0);
			for (GUIText text : texts.get(font)) {
				renderText(text);
			}
		}
		endRendering();
	}

	public void cleanup() {
		shader.cleanup();
	}
	
	private void prepare() {
		GLControl.enableBlending();
		GLControl.disableDepthTest();
		shader.start();
	}
	
	private void renderText(GUIText text) {
		Vao vao = text.getMesh();
		vao.bind(0, 1);
		color.load(text.getColor());
		outlineColor.load(text.getOutlineColor());
		offset.load(text.getOffset());
		translation.load(text.getPosition());
		charWidth.load(text.getCharacterWidth());
		charEdge.load(text.getCharacterEdge());
		borderWidth.load(text.getBorderWidth());
		borderEdge.load(text.getBorderEdge());
		GLControl.drawArraysT(text.getVertexCount());
		vao.unbind(0, 1);
	}
	
	private void endRendering() {
		shader.stop();
		GLControl.disableBlending();
		GLControl.enableDepthTest();
	}
}
