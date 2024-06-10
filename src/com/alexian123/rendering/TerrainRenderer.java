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
import com.alexian123.entity.Light;
import com.alexian123.model.RawModel;
import com.alexian123.shader.TerrainShader;
import com.alexian123.terrain.Terrain;
import com.alexian123.texture.TerrainTexture;
import com.alexian123.util.Constants;
import com.alexian123.util.Maths;

public class TerrainRenderer {
	
	private TerrainShader shader = new TerrainShader();
	
	private final int numTextures;
	
	public TerrainRenderer() {
		shader.start();
		shader.loadProjectionMatrix(Constants.PROJECTION_MATRIX);
		this.numTextures = shader.connectTextureUnits();
		shader.stop();
	}
	
	public void render(List<Terrain> terrains, List<Light> lights, Camera camera, Vector4f clipPlane) {
		shader.start();
		shader.loadClipPlane(clipPlane);
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
		TerrainTexture[] textures = terrain.getOrderedTextures();
		for (int i = 0; i < numTextures; ++i) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0 + i);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures[i].getID());
		}
	}
	
	private void unbind() {
		for (int i = 0; i < shader.getNumAttributes(); ++i) {
			GL20.glDisableVertexAttribArray(0);
		}
		GL30.glBindVertexArray(0);
	}
	
	private void loadModelMatrix(Terrain terrain) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(new Vector3f(terrain.getX(), 0, terrain.getZ()), new Vector3f(0, 0, 0), 1);
		shader.loadTransformationMatrix(transformationMatrix);
	}
}
