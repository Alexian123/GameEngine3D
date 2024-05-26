package com.alexian123.renderEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.alexian123.models.RawModel;

public class OBJLoader {
	
	public static RawModel loadObjModel(String fileName, Loader loader) {
		FileReader fr = null;
		try {
			fr = new FileReader(new File("res/models/" + fileName + ".obj"));
		} catch (FileNotFoundException e) {
			System.err.println("Error reading obj file: " + fileName);
			e.printStackTrace();
		}
		List<Vector3f> vertices = new ArrayList<>();
		List<Vector2f> textureCoords = new ArrayList<>();
		List<Vector3f> normals = new ArrayList<>();
		List<Integer> indices = new ArrayList<>();
		float[] verticesArr = null;
		float[] textureCoordsArr = null;
		float[] normalsArr = null;
		int[] indicesArr = null;
		
		try {
			BufferedReader br = new BufferedReader(fr);
			String line = "";
			boolean reachedFacesSection = false;
			
			while (!reachedFacesSection) {
				line = br.readLine();
				String[] tokens = line.split(" ");
				
				switch (tokens[0]) {
					case "v":
						Vector3f vertex = new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));
						vertices.add(vertex);
						break;
					case "vt":
						Vector2f texCoord = new Vector2f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
						textureCoords.add(texCoord);
						break;
					case "vn":
						Vector3f normal = new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));
						normals.add(normal);
						break;
					case "f":
						reachedFacesSection = true;
						textureCoordsArr = new float[vertices.size() * 2];
						normalsArr = new float[vertices.size() * 3];
						break;
					default: // skip line
						break;
				}
			}
			
			while (line != null) {
				if (!line.startsWith("f ")) {
					line = br.readLine();
					continue;
				}
				String[] tokens = line.split(" ");
				for (int i = 1; i < tokens.length; ++i) {
					processVertex(tokens[i].split("/"), indices, textureCoords, normals, textureCoordsArr, normalsArr);
				}
				line = br.readLine();
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		verticesArr = new float[vertices.size() * 3];
		indicesArr = new int[indices.size()];
		int vertexPtr = 0;
		for (Vector3f vertex : vertices) {
			verticesArr[vertexPtr++] = vertex.x;
			verticesArr[vertexPtr++] = vertex.y;
			verticesArr[vertexPtr++] = vertex.z;
		}
		for (int i = 0; i < indices.size(); ++i) {
			indicesArr[i] = indices.get(i);
		}
		
		return loader.loadToVao(verticesArr, textureCoordsArr, normalsArr, indicesArr);
	}
	
	private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textureCoords, 
			List<Vector3f> normals, float[] textureCoordsArr, float[] normalsArr) {
		int currentVertexPtr = Integer.parseInt(vertexData[0]) - 1;
		indices.add(currentVertexPtr);
		Vector2f currentTextureCoord = textureCoords.get(Integer.parseInt(vertexData[1]) - 1);
		textureCoordsArr[currentVertexPtr * 2] = currentTextureCoord.x;
		textureCoordsArr[currentVertexPtr * 2 + 1] = 1 - currentTextureCoord.y;
		Vector3f currentNormal = normals.get(Integer.parseInt(vertexData[2]) - 1);
		normalsArr[currentVertexPtr * 3] = currentNormal.x;
		normalsArr[currentVertexPtr * 3 + 1] = currentNormal.y;
		normalsArr[currentVertexPtr * 3 + 2] = currentNormal.z;
	}
	
}
