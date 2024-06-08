package com.alexian123.shader;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class FontShader extends ShaderProgram {

	private static final String VERTEX_SHADER_FILE = "src/com/alexian123/shader/glsl/font_shader.vert";
	private static final String FRAGMENT_SHADER_FILE = "src/com/alexian123/shader/glsl/font_shader.frag";
	
	private int translationLocation;
	private int colorLocation;
	private int outlineColorLocation;
	private int offsetLocation;
	private int characterWidthLocation;
	private int characterEdgeLocation;
	private int borderWidthLocation;
	private int borderEdgeLocation;
	
	public FontShader() {
		super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
	}
	
	public void loadTranslation(Vector2f translation) {
		super.loadVector(translationLocation, translation);
	}
	
	public void loadColor(Vector3f color) {
		super.loadVector(colorLocation, color);
	}
	
	public void loadOutlineColor(Vector3f color) {
		super.loadVector(outlineColorLocation, color);
	}
	
	public void loadOffset(Vector2f offset) {
		super.loadVector(offsetLocation, offset);
	}
	
	public void loadCharacterDimensions(float width, float edge) {
		super.loadFLoat(characterWidthLocation, width);
		super.loadFLoat(characterEdgeLocation, edge);
	}
	
	public void loadBorderDimensions(float width, float edge) {
		super.loadFLoat(borderWidthLocation, width);
		super.loadFLoat(borderEdgeLocation, edge);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttrib(0, "position");
		super.bindAttrib(1, "textureCoord");
	}
	
	@Override
	protected void getAllUniformLocations() {
		translationLocation = super.getUniformLocation("translation");
		colorLocation = super.getUniformLocation("color");
		outlineColorLocation = super.getUniformLocation("outlineColor");
		offsetLocation = super.getUniformLocation("offset");
		characterWidthLocation = super.getUniformLocation("characterWidth");
		characterEdgeLocation = super.getUniformLocation("characterEdge");
		borderWidthLocation = super.getUniformLocation("borderWidth");
		borderEdgeLocation = super.getUniformLocation("borderEdge");

	}
}