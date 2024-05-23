package com.alexian123.shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public abstract class ShaderProgram {
	
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	public ShaderProgram(String vertexShader, String fragmentShader) {
		vertexShaderID = loadShader(vertexShader, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentShader, GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
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
