package com.alexian123.loader.collada;

import com.alexian123.loader.data.AnimatedModelData;
import com.alexian123.loader.data.AnimationData;
import com.alexian123.loader.data.MeshData;
import com.alexian123.loader.data.SkeletonData;
import com.alexian123.loader.data.SkinningData;
import com.alexian123.util.Constants;
import com.alexian123.util.xml.XmlNode;
import com.alexian123.util.xml.XmlParser;

public class ColladaLoader {

	public static AnimatedModelData loadColladaModel(String fileName, int maxWeights) {
		XmlNode node = XmlParser.loadXmlFile(Constants.ANIMATED_MODELS_DIR + fileName + Constants.ANIMATED_MODEL_FILE_EXTENSION);

		SkinLoader skinLoader = new SkinLoader(node.getChild("library_controllers"), maxWeights);
		SkinningData skinningData = skinLoader.extractSkinData();

		SkeletonLoader jointsLoader = new SkeletonLoader(node.getChild("library_visual_scenes"), skinningData.jointOrder);
		SkeletonData jointsData = jointsLoader.extractBoneData();

		GeometryLoader g = new GeometryLoader(node.getChild("library_geometries"), skinningData.verticesSkinData);
		MeshData meshData = g.extractModelData();

		return new AnimatedModelData(meshData, jointsData);
	}

	public static AnimationData loadColladaAnimation(String fileName) {
		XmlNode node = XmlParser.loadXmlFile(Constants.ANIMATIONS_DIR + fileName + Constants.ANIMATION_FILE_EXTENSION);
		XmlNode animNode = node.getChild("library_animations");
		XmlNode jointsNode = node.getChild("library_visual_scenes");
		AnimationLoader loader = new AnimationLoader(animNode, jointsNode);
		AnimationData animData = loader.extractAnimation();
		return animData;
	}
}