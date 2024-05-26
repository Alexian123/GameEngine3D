package com.alexian123.shader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public abstract class ShaderProgram {
	
	private static FloatBuffer matrix4fBuffer = BufferUtils.createFloatBuffer(16);
	
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	public ShaderProgram(String vertexShader, String fragmentShader) {
		vertexShaderID = loadShader(vertexShader, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentShader, GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		getAllUniformLocations();
	}
	
	public void start() {
		GL20.glUseProgram(programID);
	}
	
	public void stop() {
		GL20.glUseProgram(0);
	}
	
	public void cleanup() {
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}
	
	protected abstract void bindAttributes();
	
	protected void bindAttrib(int attrib, String varName) {
		GL20.glBindAttribLocation(programID, attrib, varName);
	}
	
	protected abstract void getAllUniformLocations();
	
	protected int getUniformLocation(String uniformName) {
		return GL20.glGetUniformLocation(programID, uniformName);
	}
	
	protected void loadFLoat(int location, float val) {
		GL20.glUniform1f(location, val);
	}
	
	protected void loadBoolean(int location, boolean val) {
		GL20.glUniform1f(location, val ? 1.0f : 0.0f);
	}
	
	protected void loadVector3f(int location, Vector3f vec) {
		GL20.glUniform3f(location, vec.x, vec.y, vec.z);
	}
	
	protected void loadMatrix4f(int location, Matrix4f mat) {
		mat.store(matrix4fBuffer);
		matrix4fBuffer.flip();
		GL20.glUniformMatrix4(location, false, matrix4fBuffer);
	}

	@SuppressWarnings("deprecation")
	private static int loadShader(String fileName, int type) {
		final String SHADER_TYPE_NAME = type == GL20.GL_VERTEX_SHADER ? "vertex" : "fragment";
		StringBuilder shaderCode = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line;
			while ((line = reader.readLine()) != null) {
				shaderCode.append(line).append("\n");
			}
			reader.close();
		} catch (IOException e) {
			System.err.println("Error loading " + SHADER_TYPE_NAME + " shader");
			e.printStackTrace();
			System.exit(-1);
		}
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderCode);
		GL20.glCompileShader(shaderID);
		if (GL20.glGetShader(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.println("Error compiling " + SHADER_TYPE_NAME + " shader");
			System.exit(-1);
		}
		return shaderID;
	}
}
