package com.alexian123.renderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import com.alexian123.entity.Camera;
import com.alexian123.loader.Loader;
import com.alexian123.model.RawModel;
import com.alexian123.shader.SkyBoxShader;

public class SkyBoxRenderer {
	
	private static final float SIZE = 500f;
	
	private static final float[] VERTICES = {
			-SIZE,  SIZE, -SIZE,
		    -SIZE, -SIZE, -SIZE,
		     SIZE, -SIZE, -SIZE,
		     SIZE, -SIZE, -SIZE,
		     SIZE,  SIZE, -SIZE,
		    -SIZE,  SIZE, -SIZE,

		    -SIZE, -SIZE,  SIZE,
		    -SIZE, -SIZE, -SIZE,
		    -SIZE,  SIZE, -SIZE,
		    -SIZE,  SIZE, -SIZE,
		    -SIZE,  SIZE,  SIZE,
		    -SIZE, -SIZE,  SIZE,

		     SIZE, -SIZE, -SIZE,
		     SIZE, -SIZE,  SIZE,
		     SIZE,  SIZE,  SIZE,
		     SIZE,  SIZE,  SIZE,
		     SIZE,  SIZE, -SIZE,
		     SIZE, -SIZE, -SIZE,

		    -SIZE, -SIZE,  SIZE,
		    -SIZE,  SIZE,  SIZE,
		     SIZE,  SIZE,  SIZE,
		     SIZE,  SIZE,  SIZE,
		     SIZE, -SIZE,  SIZE,
		    -SIZE, -SIZE,  SIZE,
	
		    -SIZE,  SIZE, -SIZE,
		     SIZE,  SIZE, -SIZE,
		     SIZE,  SIZE,  SIZE,
		     SIZE,  SIZE,  SIZE,
		    -SIZE,  SIZE,  SIZE,
		    -SIZE,  SIZE, -SIZE,
	
		    -SIZE, -SIZE, -SIZE,
		    -SIZE, -SIZE,  SIZE,
		     SIZE, -SIZE, -SIZE,
		     SIZE, -SIZE, -SIZE,
		    -SIZE, -SIZE,  SIZE,
		     SIZE, -SIZE,  SIZE
	};
	
	private static final String TEXTURE_DIR = "skybox/";
	private static final String[] TEXTURE_FILE_NAMES = { 
			TEXTURE_DIR + "right", 
			TEXTURE_DIR + "left", 
			TEXTURE_DIR + "top", 
			TEXTURE_DIR + "bottom", 
			TEXTURE_DIR + "back", 
			TEXTURE_DIR + "front" 
	};
	
	private static final SkyBoxShader shader = new SkyBoxShader();
	
	private static RawModel cube;
	private static int textureID;
	private static boolean isInitalized = false;
	
	public static void init(Loader loader, Matrix4f projectionMatrix) {
		if (!isInitalized) {
			cube = loader.loadToVao(VERTICES, 3);
			textureID = loader.loadCubeMap(TEXTURE_FILE_NAMES);
			shader.start();
			shader.loadProjectionMatrix(projectionMatrix);
			shader.stop();
			isInitalized = true;
		}
	}
	
	public static void render(Camera camera) {
		shader.start();
		shader.loadViewMatrix(camera);
		GL30.glBindVertexArray(cube.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, textureID);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	public static void cleanup() {
		shader.cleanup();
	}
}
