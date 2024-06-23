package com.alexian123.util.gl.uniforms;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

import com.alexian123.util.enums.UniformName;

public class UniformMat4 extends Uniform<Matrix4f> {
	
	private final FloatBuffer buffer = BufferUtils.createFloatBuffer(16);

	public UniformMat4(UniformName name, int programID) {
		super(name, programID);
	}

	@Override
	public void load(Matrix4f value) {
		value.store(buffer);
		buffer.flip();
		GL20.glUniformMatrix4(location, false, buffer);
	}

}
