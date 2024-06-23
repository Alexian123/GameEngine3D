package com.alexian123.rendering;

import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Matrix4f;

import com.alexian123.engine.GameManager;
import com.alexian123.entity.AnimatedEntity;
import com.alexian123.entity.Entity;
import com.alexian123.model.ModelMesh;
import com.alexian123.model.TexturedModel;
import com.alexian123.shader.ShaderProgram;
import com.alexian123.texture.ModelTexture;
import com.alexian123.util.enums.EntityType;
import com.alexian123.util.enums.UniformName;
import com.alexian123.util.gl.GLControl;
import com.alexian123.util.gl.uniforms.UniformArrayMat4;
import com.alexian123.util.gl.uniforms.UniformBoolean;
import com.alexian123.util.gl.uniforms.UniformMat4;
import com.alexian123.util.mathematics.MatrixCreator;

public class ShadowRenderer {
	
	private static final String VERTEX_SHADER_FILE = "shadow";
	private static final String FRAGMENT_SHADER_FILE = "shadow";

	private final Matrix4f projectionViewMatrix;
	
	private final ShaderProgram shader;
	private final UniformMat4 mvpMatrix;
	private final UniformArrayMat4 jointTransforms;
	private final UniformBoolean isAnimated;

	/**
	 * @param shader
	 *            - the simple shader program being used for the shadow render
	 *            pass.
	 * @param projectionViewMatrix
	 *            - the orthographic projection matrix multiplied by the light's
	 *            "view" matrix.
	 */
	public ShadowRenderer(Matrix4f projectionViewMatrix) {	
		this.projectionViewMatrix = projectionViewMatrix;
		shader = new ShaderProgram(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
		int id = shader.getProgramID();
		mvpMatrix = new UniformMat4(UniformName.MVP_MATRIX, id);
		jointTransforms = new UniformArrayMat4(UniformName.JOINT_TRANSFORMS, GameManager.SETTINGS.maxJoints, id);
		isAnimated = new UniformBoolean(UniformName.IS_ANIMATED, id);
	}

	/**
	 * Renders entities to the shadow map. Each model is first bound and then all
	 * of the entities using that model are rendered to the shadow map.
	 * 
	 * @param entities
	 *            - the entities to be rendered to the shadow map.
	 */
	public void render(Map<TexturedModel, List<Entity>> entities) {
		shader.start();
		for (TexturedModel model : entities.keySet()) {
			ModelMesh rawModel = model.getMesh();
			ModelTexture texture = model.getTexture();
			rawModel.getVao().bind(0, 1, 4, 5);
			texture.getColorTexture().bindToUnit(0);
			if (texture.isUsingTransparency()) {
				GLControl.disableCulling();
			}
			for (Entity entity : entities.get(model)) {
				prepareInstance(entity);
				GLControl.drawElementsT(rawModel.getVertexCount());
			}
			
			if (texture.isUsingTransparency()) {
				GLControl.enableCulling();
			}
			
			rawModel.getVao().unbind(0, 1, 4, 5);
		}
		shader.stop();
	}
	
	public void cleanup() {
		shader.cleanup();
	}

	/**
	 * Prepares an entity to be rendered. The model matrix is created in the
	 * usual way and then multiplied with the projection and view matrix (often
	 * in the past we've done this in the vertex shader) to create the
	 * mvp-matrix. This is then loaded to the vertex shader as a uniform.
	 * 
	 * @param entity
	 *            - the entity to be prepared for rendering.
	 */
	private void prepareInstance(Entity entity) {
		Matrix4f modelMatrix = MatrixCreator.createTransformationMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
		Matrix4f mvpMatrixVal = Matrix4f.mul(projectionViewMatrix, modelMatrix, null);
		mvpMatrix.load(mvpMatrixVal);
		boolean animated = entity.getType() == EntityType.ANIMATED;
		isAnimated.load(animated);
		if (animated) {
			AnimatedEntity animatedEntity = (AnimatedEntity) entity;
			jointTransforms.load(animatedEntity.getModel().getJointTransforms());
		}
	}

}