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
import com.alexian123.entity.Entity;
import com.alexian123.game.Camera;
import com.alexian123.lighting.Light;
import com.alexian123.model.RawModel;
import com.alexian123.model.TexturedModel;
import com.alexian123.shader.EntityShader;
import com.alexian123.texture.ModelTexture;
import com.alexian123.util.Constants;
import com.alexian123.util.mathematics.MatrixCreator;

public class EntityRenderer {
	
	private static final int MAX_NUM_TEXTURES = 4;

	private final EntityShader shader;
	
	private final int numTextures;
	private final int[] textures = new int[MAX_NUM_TEXTURES];
	
	public EntityRenderer(int shadowMapID) {
		this(new EntityShader(), shadowMapID);
	}
	
	public EntityRenderer(EntityShader shader, int shadowMapID) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(Constants.PROJECTION_MATRIX);
		shader.loadShadowParameters(Constants.SHADOW_DISTANCE, Constants.SHADOW_TRANSITION);
		shader.loadShadowMapSize(Constants.SHADOW_MAP_SIZE);
		shader.loadAmbientLight(Constants.AMBIENT_LIGHT);
		this.numTextures = shader.connectTextureUnits();
		textures[0] = shadowMapID;
		shader.stop();
	}
	
	public void render(Map<TexturedModel, List<Entity>> entities, List<Light> lights, Camera camera, Vector4f clipPlane, Matrix4f toShadowMapSpace) {
		shader.start();
		shader.loadClipPlane(clipPlane);
		shader.loadToShadowMapSpaceMatrix(toShadowMapSpace);
		shader.loadFog(Constants.FOG_DENSITY, Constants.FOG_GRADIENT, Constants.FOG_COLOR);
		Matrix4f viewMatrix = MatrixCreator.createViewMatrix(camera);
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
	
	private void prepareTexturedModel(TexturedModel model) {
		RawModel rawModel = model.getRawModel();
		ModelTexture texture = model.getTexture();
		textures[1] = texture.getID();
		textures[2] = texture.getLightingMap();
		textures[3] = texture.getNormalMap();
		
		GL30.glBindVertexArray(rawModel.getVaoID());
		for (int i = 0; i < shader.getNumAttributes(); ++i) {
			GL20.glEnableVertexAttribArray(i);
		}
		
		if (texture.isTransparency()) {
			RenderingManager.disableCulling();
		}
		
		for (int i = 0; i < numTextures; ++i) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0 + i);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures[i]);
		}
		
		shader.loadAtlasDimension(texture.getAtlasDimension());
		shader.loadUseFakeLighting(texture.isFakeLighting());
		shader.loadShineParameters(texture.getShineDamper(), texture.getReflectivity());
		shader.loadUseLightingMap(texture.isUsingLightingMap());
	}
	
	private void prepareEntity(Entity entity) {
		Matrix4f transformationMatrix = MatrixCreator.createTransformationMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		shader.loadAtlasOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
		shader.loadPcfCount(entity.isNoShading() ? -1 : Constants.PCF_COUNT);
	}
	
	private void unbind() {
		RenderingManager.enableCulling();
		for (int i = 0; i < shader.getNumAttributes(); ++i) {
			GL20.glDisableVertexAttribArray(i);
		}
		GL30.glBindVertexArray(0);
	}
}
