package com.alexian123.font;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.alexian123.engine.GameManager;
import com.alexian123.engine.TextManager;
import com.alexian123.util.gl.Vao;

/**
 * Represents a piece of text in the game.
 * 
 * @author Karl
 *
 */
public class GUIText {
	


	private String textString;
	private float fontSize;

	private Vao textMeshVao;
	private int vertexCount;
	private Vector3f color = new Vector3f(0f, 0f, 0f);
	private Vector3f outlineColor = new Vector3f(0f, 0f, 0f);

	private Vector2f position;
	private float lineMaxSize;
	private int numberOfLines;

	private FontType font;

	private boolean centerText = false;
	
	private Vector2f offset = new Vector2f(0f, 0f);
	private float characterWidth = GameManager.SETTINGS.defaultCharacterWidth;
	private float characterEdge = GameManager.SETTINGS.defaultCharacterEdge;
	private float borderWidth = GameManager.SETTINGS.defaultBorderWidth;
	private float borderEdge = GameManager.SETTINGS.defaultBorderEdge;
	
	private boolean visible = false;

	/**
	 * Creates a new text, loads the text's quads into a VAO, and adds the text
	 * to the screen.
	 * 
	 * @param text
	 *            - the text.
	 * @param fontSize
	 *            - the font size of the text, where a font size of 1 is the
	 *            default size.
	 * @param font
	 *            - the font that this text should use.
	 * @param position
	 *            - the position on the screen where the top left corner of the
	 *            text should be rendered. The top left corner of the screen is
	 *            (0, 0) and the bottom right is (1, 1).
	 * @param maxLineLength
	 *            - basically the width of the virtual page in terms of screen
	 *            width (1 is full screen width, 0.5 is half the width of the
	 *            screen, etc.) Text cannot go off the edge of the page, so if
	 *            the text is longer than this length it will go onto the next
	 *            line. When text is centered it is centered into the middle of
	 *            the line, based on this line length value.
	 * @param centered
	 *            - whether the text should be centered or not.
	 */
	public GUIText(String text, float fontSize, FontType font, Vector2f position, float maxLineLength,
			boolean centered) {
		this.textString = text;
		this.fontSize = fontSize;
		this.font = font;
		this.position = position;
		this.lineMaxSize = maxLineLength;
		this.centerText = centered;
	}
	
	/**
	 * Show the text on the screen
	 */
	public void show() {
		if (!visible) {
			TextManager.loadText(this);
			visible = true;
		}
	}
	
	/**
	 * Hide the text from the screen
	 */
	public void hide() {
		if (visible) {
			TextManager.removeText(this);
			visible = false;
		}
	}
	
	/**
	 * @return true if the text is visible on the screen
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @return The font used by this text.
	 */
	public FontType getFont() {
		return font;
	}

	/**
	 * Set the color of the text.
	 * 
	 * @param r
	 *            - red value, between 0 and 1.
	 * @param g
	 *            - green value, between 0 and 1.
	 * @param b
	 *            - blue value, between 0 and 1.
	 */
	public void setColor(float r, float g, float b) {
		color.set(r, g, b);
	}

	/**
	 * @return the color of the text.
	 */
	public Vector3f getColor() {
		return color;
	}
	
	/**
	 * Set the outline color of the text.
	 * 
	 * @param r
	 *            - red value, between 0 and 1.
	 * @param g
	 *            - green value, between 0 and 1.
	 * @param b
	 *            - blue value, between 0 and 1.
	 */
	public void setOutlineColor(float r, float g, float b) {
		outlineColor.set(r, g, b);
	}

	/**
	 * @return the outline color of the text.
	 */
	public Vector3f getOutlineColor() {
		return outlineColor;
	}

	/**
	 * @return The number of lines of text. This is determined when the text is
	 *         loaded, based on the length of the text and the max line length
	 *         that is set.
	 */
	public int getNumberOfLines() {
		return numberOfLines;
	}

	/**
	 * @return The position of the top-left corner of the text in screen-space.
	 *         (0, 0) is the top left corner of the screen, (1, 1) is the bottom
	 *         right.
	 */
	public Vector2f getPosition() {
		return position;
	}

	/**
	 * @return the text's VAO, which contains all the vertex data for
	 *         the quads on which the text will be rendered.
	 */
	public Vao getMesh() {
		return textMeshVao;
	}

	/**
	 * Set the VAO and vertex count for this text.
	 * 
	 * @param vao
	 *            - the VAO containing all the vertex data for the quads on
	 *            which the text will be rendered.
	 * @param verticesCount
	 *            - the total number of vertices in all of the quads.
	 */
	public void setMeshInfo(Vao vao, int verticesCount) {
		this.textMeshVao = vao;
		this.vertexCount = verticesCount;
	}
	
	/**
	 * @return The text offset (for drop shadow effect).
	 */
	public Vector2f getOffset() {
		return offset;
	}

	/**
	 * Sets the text offset (for drop shadow effect)
	 * 
	 * @param offset
	 */
	public void setOffset(Vector2f offset) {
		this.offset = offset;
	}

	/**
	 * @return The character width.
	 */
	public float getCharacterWidth() {
		return characterWidth;
	}

	/**
	 * Sets the width of a character
	 * 
	 * @param characterWidth
	 */
	public void setCharacterWidth(float characterWidth) {
		this.characterWidth = characterWidth;
	}

	/**
	 * @return The character edge transition.
	 */
	public float getCharacterEdge() {
		return characterEdge;
	}

	/**
	 * Sets the edge transition of a character
	 * 
	 * @param characterEdge
	 */
	public void setCharacterEdge(float characterEdge) {
		this.characterEdge = characterEdge;
	}

	/**
	 * @return The border width.
	 */
	public float getBorderWidth() {
		return borderWidth;
	}

	/**
	 * Sets the width for a character's border
	 * 
	 * @param borderWidth
	 */
	public void setBorderWidth(float borderWidth) {
		this.borderWidth = borderWidth;
	}

	/**
	 * @return The border edge distance.
	 */
	public float getBorderEdge() {
		return borderEdge;
	}

	/**
	 * Sets the edge distance for a character's border
	 * 
	 * @param borderEdge
	 */
	public void setBorderEdge(float borderEdge) {
		this.borderEdge = borderEdge;
	}

	/**
	 * @return The total number of vertices of all the text's quads.
	 */
	public int getVertexCount() {
		return this.vertexCount;
	}

	/**
	 * @return the font size of the text (a font size of 1 is normal).
	 */
	protected float getFontSize() {
		return fontSize;
	}

	/**
	 * Sets the number of lines that this text covers (method used only in
	 * loading).
	 * 
	 * @param number
	 */
	protected void setNumberOfLines(int number) {
		this.numberOfLines = number;
	}

	/**
	 * @return {@code true} if the text should be centered.
	 */
	protected boolean isCentered() {
		return centerText;
	}

	/**
	 * @return The maximum length of a line of this text.
	 */
	protected float getMaxLineSize() {
		return lineMaxSize;
	}

	/**
	 * @return The string of text.
	 */
	protected String getTextString() {
		return textString;
	}
}
