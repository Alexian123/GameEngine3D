package com.alexian123.shader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public abstract class ShaderProgram {
	
	protected static final int NEW_UNIFORM = -1;
	
	private static FloatBuffer matrix4fBuffer = BufferUtils.createFloatBuffer(16);
	
	protected final Map<String, Integer> attributes = new HashMap<>();
	protected final Map<String, Integer> uniforms = new HashMap<>();
	protected final Map<String, List<Integer>> uniformArrays = new HashMap<>();
	
	private final int programID;
	private final int vertexShaderID;
	private final int fragmentShaderID;
	
	private final int numAttributes;
	
	private boolean isRunning = false;
	
	protected ShaderProgram(String vertexShader, String fragmentShader) {
		vertexShaderID = loadShader(vertexShader, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentShader, GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		numAttributes = setAttributes();
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		setUniforms();
		getAllUniformLocations();
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
	
	public int getNumAttributes() {
		return numAttributes;
	}

	protected abstract int setAttributes();
	
	protected abstract void setUniforms();
	
	protected void loadFloat(int location, float val) {
		GL20.glUniform1f(location, val);
	}
	
	protected void loadInt(int location, int val) {
		GL20.glUniform1i(location, val);
	}
	
	protected void loadBoolean(int location, boolean val) {
		GL20.glUniform1f(location, val ? 1.0f : 0.0f);
	}
	
	protected void loadVector(int location, Vector2f vec) {
		GL20.glUniform2f(location, vec.x, vec.y);
	}
	
	protected void loadVector(int location, Vector3f vec) {
		GL20.glUniform3f(location, vec.x, vec.y, vec.z);
	}
	
	protected void loadVector(int location, Vector4f vec) {
		GL20.glUniform4f(location, vec.x, vec.y, vec.z, vec.w);
	}
	
	protected void loadMatrix(int location, Matrix4f mat) {
		mat.store(matrix4fBuffer);
		matrix4fBuffer.flip();
		GL20.glUniformMatrix4(location, false, matrix4fBuffer);
	}
	
	private void bindAttributes() {
		for (String attribName : attributes.keySet()) {
			GL20.glBindAttribLocation(programID, attributes.get(attribName), attribName);
		}
	}
	
	private void getAllUniformLocations() {
		for (String uniformName : uniforms.keySet()) {
			uniforms.replace(uniformName, GL20.glGetUniformLocation(programID, uniformName));
		}
		for (String uniformArrayName : uniformArrays.keySet()) {
			List<Integer> uniformArray = uniformArrays.get(uniformArrayName);
			for (int i = 0; i < uniformArray.size(); ++i) {
				uniformArray.set(i, GL20.glGetUniformLocation(programID, uniformArrayName + "[" + i + "]"));
			}
		}
	}
	
	protected static List<Integer> createNewUniformArray(int size) {
		return new ArrayList<>(Collections.nCopies(size, NEW_UNIFORM));
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
		} catch (IOException e) {
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
