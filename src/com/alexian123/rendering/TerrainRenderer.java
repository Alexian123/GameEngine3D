package com.alexian123.rendering;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.alexian123.entity.Camera;
import com.alexian123.lighting.Light;
import com.alexian123.model.RawModel;
import com.alexian123.shader.TerrainShader;
import com.alexian123.terrain.Terrain;
import com.alexian123.texture.TerrainTexturePack;
import com.alexian123.util.Constants;
import com.alexian123.util.mathematics.MatrixCreator;

public class TerrainRenderer {
	
	private static final int MAX_NUM_TEXTURES = 6;
	
	private TerrainShader shader = new TerrainShader();
	
	private final int numTextures;
	protected final int[] textures = new int[MAX_NUM_TEXTURES];
	
	public TerrainRenderer(int shadowMapID) {
		shader.start();
		shader.loadProjectionMatrix(Constants.PROJECTION_MATRIX);
		shader.loadShadowParameters(Constants.SHADOW_DISTANCE, Constants.SHADOW_TRANSITION);
		shader.loadShadowMapSize(Constants.SHADOW_MAP_SIZE);
		shader.loadPcfCount(Constants.PCF_COUNT);
		shader.loadAmbientLight(Constants.AMBIENT_LIGHT);
		this.numTextures = shader.connectTextureUnits();
		textures[MAX_NUM_TEXTURES - 1] = shadowMapID;
		shader.stop();
	}
	
	public void render(List<Terrain> terrains, List<Light> lights, Camera camera, Vector4f clipPlane, Matrix4f toShadowMapSpace) {
		shader.start();
		shader.loadClipPlane(clipPlane);
		shader.loadToShadowMapSpaceMatrix(toShadowMapSpace);
		shader.loadFog(Constants.FOG_DENSITY, Constants.FOG_GRADIENT, Constants.FOG_COLOR);
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		for (Terrain terrain : terrains) {
			prepareTerrain(terrain);
			loadModelMatrix(terrain);
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			unbind();
		}
		shader.stop();
	}
	
	public void cleanup() {
		shader.cleanup();
	}
	
	public TerrainShader getShader() {
		return shader;
	}
	
	private void prepareTerrain(Terrain terrain) {
		RawModel rawModel = terrain.getModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		for (int i = 0; i < shader.getNumAttributes(); ++i) {
			GL20.glEnableVertexAttribArray(i);
		}
		bindTextures(terrain);
		shader.loadShineParameters(terrain.getTexturePack().getShineDamper(), terrain.getTexturePack().getReflectivity());
	}
	
	private void bindTextures(Terrain terrain) {
		TerrainTexturePack pack = terrain.getTexturePack();
		textures[0] = pack.getBackgroundTexture().getID();
		textures[1] = pack.getRedTexture().getID();
		textures[2] = pack.getGreenTexture().getID();
		textures[3] = pack.getBlueTexture().getID();
		textures[4] = terrain.getBlendMap().getID();
		for (int i = 0; i < numTextures; ++i) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0 + i);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures[i]);
		}
	}
	
	private void unbind() {
		for (int i = 0; i < shader.getNumAttributes(); ++i) {
			GL20.glDisableVertexAttribArray(0);
		}
		GL30.glBindVertexArray(0);
	}
	
	private void loadModelMatrix(Terrain terrain) {
		Matrix4f transformationMatrix = MatrixCreator.createTransformationMatrix(new Vector3f(terrain.getX(), 0, terrain.getZ()), new Vector3f(0, 0, 0), 1);
		shader.loadTransformationMatrix(transformationMatrix);
	}
}
