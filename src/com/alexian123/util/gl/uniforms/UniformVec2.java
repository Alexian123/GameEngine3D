package com.alexian123.util.gl.uniforms;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector2f;

import com.alexian123.util.enums.UniformName;

public class UniformVec2 extends Uniform<Vector2f> {

	public UniformVec2(UniformName name, int programID) {
		super(name, programID);
	}

	@Override
	public void load(Vector2f value) {
		GL20.glUniform2f(location, value.x, value.y);
	}
}
