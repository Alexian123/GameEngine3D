package com.alexian123.rendering;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import com.alexian123.engine.RenderingManager;
import com.alexian123.entity.AnimatedEntity;
import com.alexian123.entity.Camera;
import com.alexian123.lighting.Light;
import com.alexian123.model.RawModel;
import com.alexian123.model.TexturedModel;
import com.alexian123.model.animated.AnimatedModel;
import com.alexian123.shader.AnimatedEntityShader;
import com.alexian123.texture.ModelTexture;
import com.alexian123.util.Constants;
import com.alexian123.util.mathematics.MatrixCreator;

/**
 * 
 * This class deals with rendering an animated entity. Nothing particularly new
 * here. The only exciting part is that the joint transforms get loaded up to
 * the shader in a uniform array.
 * 
 * @author Karl
 *
 */
public class AnimatedEntityRenderer {
	
	private static final int MAX_NUM_TEXTURES = 4;

	private final AnimatedEntityShader shader = new AnimatedEntityShader();
	
	private final int numTextures;
	private final int[] textures = new int[MAX_NUM_TEXTURES];

	/**
	 * Initializes the shader program used for rendering animated models.
	 */
	public AnimatedEntityRenderer(int shadowMapID) {
		shader.start();
		shader.loadProjectionMatrix(Constants.PROJECTION_MATRIX);
		shader.loadShadowParameters(Constants.SHADOW_DISTANCE, Constants.SHADOW_TRANSITION);
		shader.loadShadowMapSize(Constants.SHADOW_MAP_SIZE);
		shader.loadAmbientLight(Constants.AMBIENT_LIGHT);
		this.numTextures = shader.connectTextureUnits();
		textures[0] = shadowMapID;
		shader.stop();
	}

	/**
	 * Renders an animated entity. The main thing to note here is that all the
	 * joint transforms are loaded up to the shader to a uniform array. Also 5
	 * attributes of the VAO are enabled before rendering, to include joint
	 * indices and weights.
	 * 
	 * @param entity
	 *            - the animated entity to be rendered.
	 * @param camera
	 *            - the camera used to render the entity.
	 * @param lightDir
	 *            - the direction of the light in the scene.
	 */
	public void render(List<AnimatedEntity> entities, List<Light> lights, Camera camera, Vector4f clipPlane, Matrix4f toShadowMapSpace) {
		shader.start();
		shader.loadClipPlane(clipPlane);
		shader.loadToShadowMapSpaceMatrix(toShadowMapSpace);
		shader.loadFog(Constants.FOG_DENSITY, Constants.FOG_GRADIENT, Constants.FOG_COLOR);
		Matrix4f viewMatrix = MatrixCreator.createViewMatrix(camera);
		shader.loadLights(lights, viewMatrix);
		shader.loadViewMatrix(viewMatrix);
		for (AnimatedEntity entity : entities) {
			prepareEntity(entity);
			AnimatedModel animatedModel = entity.getAnimatedModel();
			shader.loadJointTransforms(animatedModel.getJointTransforms());
			TexturedModel texturedModel = animatedModel.getTexturedModel();
			prepareTexturedModel(texturedModel);
			RawModel rawModel = animatedModel.getTexturedModel().getRawModel();
			GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);	
			unbind();
		}
		shader.stop();
	}

	/**
	 * Deletes the shader program when the game closes.
	 */
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
	
	private void prepareEntity(AnimatedEntity entity) {
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