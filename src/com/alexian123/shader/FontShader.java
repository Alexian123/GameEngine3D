package com.alexian123.shader;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.alexian123.util.enums.Attribute;
import com.alexian123.util.enums.Uniform;

public class FontShader extends ShaderProgram {

	private static final String VERTEX_SHADER_FILE = "/com/alexian123/shader/glsl/vertex/font.vert";
	private static final String FRAGMENT_SHADER_FILE = "/com/alexian123/shader/glsl/fragment/font.frag";
	
	public FontShader() {
		super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
	}
	
	public void loadTranslation(Vector2f translation) {
		loadVector(uniforms.get(Uniform.TRANSLATION), translation);
	}
	
	public void loadColor(Vector3f color) {
		loadVector(uniforms.get(Uniform.COLOR), color);
	}
	
	public void loadOutlineColor(Vector3f color) {
		loadVector(uniforms.get(Uniform.OUTLINE_COLOR), color);
	}
	
	public void loadOffset(Vector2f offset) {
		loadVector(uniforms.get(Uniform.OFFSET), offset);
	}
	
	public void loadCharacterDimensions(float width, float edge) {
		loadFloat(uniforms.get(Uniform.CHARACTER_WIDTH), width);
		loadFloat(uniforms.get(Uniform.CHARACTER_EDGE), edge);
	}
	
	public void loadBorderDimensions(float width, float edge) {
		loadFloat(uniforms.get(Uniform.BORDER_WIDTH), width);
		loadFloat(uniforms.get(Uniform.BORDER_EDGE), edge);
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
		uniforms.put(Uniform.TRANSLATION, NEW_UNIFORM);
		uniforms.put(Uniform.COLOR, NEW_UNIFORM);
		uniforms.put(Uniform.OUTLINE_COLOR, NEW_UNIFORM);
		uniforms.put(Uniform.OFFSET, NEW_UNIFORM);
		uniforms.put(Uniform.CHARACTER_WIDTH, NEW_UNIFORM);
		uniforms.put(Uniform.CHARACTER_EDGE, NEW_UNIFORM);
		uniforms.put(Uniform.BORDER_WIDTH, NEW_UNIFORM);
		uniforms.put(Uniform.BORDER_EDGE, NEW_UNIFORM);
	}
}