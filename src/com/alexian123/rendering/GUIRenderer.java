package com.alexian123.rendering;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import com.alexian123.loader.Loader;
import com.alexian123.model.RawModel;
import com.alexian123.shader.GUIShader;
import com.alexian123.texture.GUITexture;
import com.alexian123.util.mathematics.MatrixCreator;

public class GUIRenderer {
	
	private final GUIShader shader = new GUIShader();
	
	private RawModel quad = null;
	
	public GUIRenderer(Loader loader) {
		quad = loader.loadToVao(new float[] { -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f, -1.0f }, 2);
	}
	
	public void render(List<GUITexture> guis) {
		shader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		for (int i = 0; i < shader.getNumAttributes(); ++i) {
			GL20.glEnableVertexAttribArray(i);
		}
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		for (GUITexture gui : guis) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getID());
			Matrix4f transformationMatrix = MatrixCreator.createTransformationMatrix(gui.getPosition(), gui.getScale());
			shader.loadTransformationMatrix(transformationMatrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		for (int i = 0; i < shader.getNumAttributes(); ++i) {
			GL20.glDisableVertexAttribArray(i);
		}
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	public void cleanup() {
		shader.cleanup();
	}
}
