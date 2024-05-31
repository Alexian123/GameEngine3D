package com.alexian123.renderer;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import com.alexian123.entity.Camera;
import com.alexian123.entity.Entity;
import com.alexian123.entity.Light;
import com.alexian123.model.RawModel;
import com.alexian123.model.TexturedModel;
import com.alexian123.shader.IShader3D;
import com.alexian123.shader.StaticShader;
import com.alexian123.texture.ModelTexture;
import com.alexian123.util.Maths;

public class EntityRenderer implements IRenderer3D {
	
	private final StaticShader shader = new StaticShader();
	
	public EntityRenderer(Matrix4f projectionMatrix) {
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	@Override
	public void render() {
		Map<TexturedModel, List<Entity>> entities = RenderingManager.getEntities();
		for (TexturedModel model : entities.keySet()) {
			prepareTexturedModel(model);
			for (Entity entity : entities.get(model)) {
				prepareEntity(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);	
			}
			unbindTexturedModel();
		}
	}
	
	@Override
	public void cleanup() {
		shader.cleanup();
	}
	
	@Override
	public IShader3D getShader3D() {
		return shader;
	}
	
	private void prepareTexturedModel(TexturedModel model) {
		RawModel rawModel = model.getRawModel();
		ModelTexture texture = model.getTexture();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		shader.loadAtlasDimension(texture.getAtlasDimension());
		if (texture.isTransparency()) {
			RenderingManager.disableCulling();
		}
		shader.loadUseFakeLighting(texture.isFakeLighting());
		shader.loadShineParameters(texture.getShineDamper(), texture.getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());
	}
	
	private void unbindTexturedModel() {
		RenderingManager.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void prepareEntity(Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		shader.loadAtlasOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
	}
}
