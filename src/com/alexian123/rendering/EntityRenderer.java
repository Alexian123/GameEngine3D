package com.alexian123.rendering;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import com.alexian123.engine.RenderingManager;
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
	
	private static final int MAX_NUM_TEXTURES = 2;

	protected final EntityShader shader;
	
	protected final int numTextures;
	protected final int[] textures = new int[MAX_NUM_TEXTURES];
	
	public EntityRenderer() {
		this(new EntityShader());
	}
	
	public EntityRenderer(EntityShader shader) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(Constants.PROJECTION_MATRIX);
		this.numTextures = shader.connectTextureUnits();
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
		textures[0] = texture.getID();
		textures[1] = texture.getNormalMap();
		GL30.glBindVertexArray(rawModel.getVaoID());
		for (int i = 0; i < shader.getNumAttributes(); ++i) {
			GL20.glEnableVertexAttribArray(i);
		}
		shader.loadAtlasDimension(texture.getAtlasDimension());
		if (texture.isTransparency()) {
			RenderingManager.disableCulling();
		}
		shader.loadUseFakeLighting(texture.isFakeLighting());
		shader.loadShineParameters(texture.getShineDamper(), texture.getReflectivity());
		for (int i = 0; i < numTextures; ++i) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0 + i);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures[i]);
		}
	}
	
	protected void unbind() {
		RenderingManager.enableCulling();
		for (int i = 0; i < shader.getNumAttributes(); ++i) {
			GL20.glDisableVertexAttribArray(i);
		}
		GL30.glBindVertexArray(0);
	}
	
	private void prepareEntity(Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		shader.loadAtlasOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
	}
}
