package com.alexian123.util.gl.uniforms;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

import com.alexian123.util.enums.UniformName;

public class UniformArrayMat4 extends UniformArray<Matrix4f> {
	
	private final FloatBuffer buffer = BufferUtils.createFloatBuffer(16);

	public UniformArrayMat4(UniformName name, int size, int programID) {
		super(name, size, programID);
	}

	@Override
	public void load(Matrix4f[] values) {
		for (int i = 0; i < values.length; ++i) {
			values[i].store(buffer);
			buffer.flip();
			GL20.glUniformMatrix4(locations[i], false, buffer);
		}
	}

}
