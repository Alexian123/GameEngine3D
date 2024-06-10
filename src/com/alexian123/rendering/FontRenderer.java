package com.alexian123.rendering;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.alexian123.font.FontType;
import com.alexian123.font.GUIText;
import com.alexian123.shader.FontShader;

public class FontRenderer {

	private final FontShader shader;

	public FontRenderer() {
		shader = new FontShader();
	}
	
	public void render(Map<FontType, List<GUIText>> texts) {
		prepare();
		for (FontType font : texts.keySet()) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.getTextureAtlas());
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
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		shader.start();
	}
	
	private void renderText(GUIText text) {
		GL30.glBindVertexArray(text.getMesh());
		for (int i = 0; i < shader.getNumAttributes(); ++i) {
			GL20.glEnableVertexAttribArray(i);
		}
		shader.loadColor(text.getColor());
		shader.loadOutlineColor(text.getOutlineColor());
		shader.loadOffset(text.getOffset());
		shader.loadTranslation(text.getPosition());
		shader.loadCharacterDimensions(text.getCharacterWidth(), text.getCharacterEdge());
		shader.loadBorderDimensions(text.getBorderWidth(), text.getBorderEdge());
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.getVertexCount());
		for (int i = 0; i < shader.getNumAttributes(); ++i) {
			GL20.glDisableVertexAttribArray(i);
		}
		GL30.glBindVertexArray(0);
	}
	
	private void endRendering() {
		shader.stop();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
}
