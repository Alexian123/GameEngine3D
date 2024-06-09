package com.alexian123.shader;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

public class ParticleShader extends ShaderProgram{
	
	private static final String VERTEX_SHADER_FILE = "src/com/alexian123/shader/glsl/particle_shader.vert";
	private static final String FRAGMENT_SHADER_FILE = "src/com/alexian123/shader/glsl/particle_shader.frag";

	public ParticleShader() {
		super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
	}
	
	public void loadModelViewMatrix(Matrix4f modelView) {
		loadMatrix(uniforms.get(Uniform.MODEL_VIEW.getName()), modelView);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix) {
		loadMatrix(uniforms.get(Uniform.PROJECTION_MATRIX.getName()), matrix);
	}
	
	public void loadAtlasInfo(Vector2f currentOffset, Vector2f nextOffset, float dimension, float blendFactor) {
		loadVector(uniforms.get(Uniform.CURRENT_ATLAS_OFFSET.getName()), currentOffset);
		loadVector(uniforms.get(Uniform.NEXT_ATLAS_OFFSET.getName()), nextOffset);
		loadVector(uniforms.get(Uniform.TEXTURE_COORD_INFO.getName()), new Vector2f(dimension, blendFactor));
	}

	@Override
	protected int setAttributes() {
		int attribNo = 0;
		attributes.put(Attribute.POSITION.getName(), attribNo++);
		return attribNo;
	}

	@Override
	protected void setUniforms() {
		uniforms.put(Uniform.MODEL_VIEW.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.PROJECTION_MATRIX.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.CURRENT_ATLAS_OFFSET.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.NEXT_ATLAS_OFFSET.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.TEXTURE_COORD_INFO.getName(), NEW_UNIFORM);
	}
}
