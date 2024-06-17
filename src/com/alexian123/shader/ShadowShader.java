package com.alexian123.shader;

import org.lwjgl.util.vector.Matrix4f;

import com.alexian123.util.enums.Attribute;
import com.alexian123.util.enums.Uniform;

public class ShadowShader extends ShaderProgram {
	
	private static final String VERTEX_SHADER_FILE = "/com/alexian123/shader/glsl/vertex/shadow.vert";
	private static final String FRAGMENT_SHADER_FILE = "/com/alexian123/shader/glsl/fragment/shadow.frag";

	public ShadowShader() {
		super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
	}
	
	public void loadMvpMatrix(Matrix4f mvpMatrix){
		loadMatrix(uniforms.get(Uniform.MVP_MATRIX), mvpMatrix);
	}

	@Override
	protected int setAttributes() {
		int attribNo = 0;
		attributes.put(Attribute.POSITION, attribNo++);
		attributes.put(Attribute.TEXTURE_COORD, attribNo++);
		return attribNo;
		
	}

	@Override
	protected void setUniforms() {
		uniforms.put(Uniform.MVP_MATRIX, NEW_UNIFORM);
	}

}