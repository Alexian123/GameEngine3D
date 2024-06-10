package com.alexian123.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alexian123.font.FontType;
import com.alexian123.font.GUIText;
import com.alexian123.font.TextMeshData;
import com.alexian123.loader.Loader;
import com.alexian123.rendering.FontRenderer;

public class TextManager {

	private static Map<FontType, List<GUIText>> texts = new HashMap<>();;
	
	private static FontRenderer renderer = new FontRenderer();
	
	private static Loader loader;
	
	private static boolean isInitialized = false;
	
	public static void init(Loader loader) {
		if (!isInitialized) {
			TextManager.loader = loader;
			isInitialized = true;
		}
	}
	
	public static void renderText() {
		renderer.render(texts);
	}
	
	public static void cleanup() {
		renderer.cleanup();
		texts.clear();
	}
	
	public static void loadText(GUIText text) {
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		int vao = loader.loadToVao(data.getVertexPositions(), data.getTextureCoords());
		text.setMeshInfo(vao, data.getVertexCount());
		List<GUIText> batch = texts.get(font);
		if (batch == null) {
			batch = new ArrayList<>();
			texts.put(font, batch);
		}
		batch.add(text);
	}
	
	public static void removeText(GUIText text) {
		List<GUIText> batch = texts.get(text.getFont());
		if (batch != null) {
			batch.remove(text);
			if (batch.isEmpty()) {
				loader.deleteVao(text.getMesh());	// delete VAO and associated VBO's from memory 
				texts.remove(text.getFont());
			}
		}
	}
}
