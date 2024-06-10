package com.alexian123.shader;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class FontShader extends ShaderProgram {

	private static final String VERTEX_SHADER_FILE = "src/com/alexian123/shader/glsl/font_shader.vert";
	private static final String FRAGMENT_SHADER_FILE = "src/com/alexian123/shader/glsl/font_shader.frag";
	
	public FontShader() {
		super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
	}
	
	public void loadTranslation(Vector2f translation) {
		loadVector(uniforms.get(Uniform.TRANSLATION.getName()), translation);
	}
	
	public void loadColor(Vector3f color) {
		loadVector(uniforms.get(Uniform.COLOR.getName()), color);
	}
	
	public void loadOutlineColor(Vector3f color) {
		loadVector(uniforms.get(Uniform.OUTLINE_COLOR.getName()), color);
	}
	
	public void loadOffset(Vector2f offset) {
		loadVector(uniforms.get(Uniform.OFFSET.getName()), offset);
	}
	
	public void loadCharacterDimensions(float width, float edge) {
		loadFloat(uniforms.get(Uniform.CHARACTER_WIDTH.getName()), width);
		loadFloat(uniforms.get(Uniform.CHARACTER_EDGE.getName()), edge);
	}
	
	public void loadBorderDimensions(float width, float edge) {
		loadFloat(uniforms.get(Uniform.BORDER_WIDTH.getName()), width);
		loadFloat(uniforms.get(Uniform.BORDER_EDGE.getName()), edge);
	}

	@Override
	protected int setAttributes() {
		int attribNo = 0;
		attributes.put(Attribute.POSITION.getName(), attribNo++);
		attributes.put(Attribute.TEXTURE_COORD.getName(), attribNo++);
		return attribNo;
	}
	
	@Override
	protected void setUniforms() {
		uniforms.put(Uniform.TRANSLATION.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.COLOR.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.OUTLINE_COLOR.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.OFFSET.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.CHARACTER_WIDTH.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.CHARACTER_EDGE.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.BORDER_WIDTH.getName(), NEW_UNIFORM);
		uniforms.put(Uniform.BORDER_EDGE.getName(), NEW_UNIFORM);
	}
}