package com.alexian123.renderer;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import com.alexian123.entity.Camera;
import com.alexian123.entity.Entity;
import com.alexian123.entity.Light;
import com.alexian123.model.RawModel;
import com.alexian123.model.TexturedModel;
import com.alexian123.shader.EntityShader;
import com.alexian123.texture.ModelTexture;
import com.alexian123.util.Constants;
import com.alexian123.util.Maths;

public class EntityRenderer {

	protected final EntityShader shader;
	
	public EntityRenderer(Matrix4f projectionMatrix) {
		this(projectionMatrix, new EntityShader());
	}
	
	protected EntityRenderer(Matrix4f projectionMatrix, EntityShader shader) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.connectTextureUnits();
		shader.stop();
	}
	
	public void render(Map<TexturedModel, List<Entity>> entities, List<Light> lights, Camera camera, Vector4f clipPlane) {
		shader.start();
		shader.loadClipPlane(clipPlane);
		shader.loadFog(Constants.FOG_DENSITY, Constants.FOG_GRADIENT, Constants.FOG_COLOR);
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		shader.loadLights(lights, viewMatrix);
		shader.loadViewMatrix(viewMatrix);
		for (TexturedModel model : entities.keySet()) {
			prepareTexturedModel(model);
			for (Entity entity : entities.get(model)) {
				prepareEntity(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);	
			}
			unbind();
		}
		shader.stop();
	}
	
	public void cleanup() {
		shader.cleanup();
	}
	
	public EntityShader getShader() {
		return shader;
	}
	
	protected void prepareTexturedModel(TexturedModel model) {
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
	
	protected void unbind() {
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
