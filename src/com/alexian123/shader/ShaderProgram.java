package com.alexian123.shader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.alexian123.util.Constants;

public class ShaderProgram {
	
	private final int programID;
	private final int vertexShaderID;
	private final int fragmentShaderID;
	
	private boolean isRunning = false;
	
	public ShaderProgram(String vertexShader, String fragmentShader) {
		vertexShaderID = loadShader(Constants.VERTEX_SHADERS_DIR + vertexShader + Constants.VERTEX_SHADER_SUFFIX, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(Constants.FRAGMENT_SHADERS_DIR + fragmentShader + Constants.FRAGMENT_SHADER_SUFFIX, GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
	}
	
	public void start() {
		if (!isRunning) {
			GL20.glUseProgram(programID);
			isRunning = true;
		}
	}
	
	public void stop() {
		if (isRunning) {
			GL20.glUseProgram(0);
			isRunning = false;
		}
	}
	
	public void cleanup() {
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}
	
	public int getProgramID() {
		return programID;
	}
	
	private static int loadShader(String fileName, int type) {
		final String SHADER_TYPE_NAME = type == GL20.GL_VERTEX_SHADER ? "vertex" : "fragment";
		StringBuilder shaderCode = new StringBuilder();
		
		try (InputStream in = ShaderProgram.class.getResourceAsStream(fileName);
		    BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
		    String line;
		    while ((line = reader.readLine()) != null) {
		        shaderCode.append(line).append(System.lineSeparator());
		    }
		} catch (Exception e) {
		    System.err.println("Error loading " + SHADER_TYPE_NAME + " shader: " + fileName);
		    e.printStackTrace();
		    System.exit(-1);
		}
		
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderCode);
		GL20.glCompileShader(shaderID);
		
		if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
		    System.err.println("Error compiling " + SHADER_TYPE_NAME + " shader: " + fileName);
		    System.err.println(GL20.glGetShaderInfoLog(shaderID, 500));
		    System.exit(-1);
		}
		
		return shaderID;
	 }
}
