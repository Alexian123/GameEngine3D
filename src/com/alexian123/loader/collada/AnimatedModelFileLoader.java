package com.alexian123.loader.collada;

import com.alexian123.loader.Loader;
import com.alexian123.loader.data.AnimatedModelData;
import com.alexian123.loader.data.JointData;
import com.alexian123.loader.data.SkeletonData;
import com.alexian123.model.RawModel;
import com.alexian123.model.TexturedModel;
import com.alexian123.model.animated.AnimatedModel;
import com.alexian123.model.animated.Joint;
import com.alexian123.texture.ModelTexture;
import com.alexian123.util.Constants;

public class AnimatedModelFileLoader {

	/**
	 * Creates an AnimatedEntity from the data in an entity file. It loads up
	 * the collada model data, stores the extracted data in a VAO, sets up the
	 * joint heirarchy, and loads up the entity's texture.
	 * 
	 * @param entityFile
	 *            - the file containing the data for the entity.
	 * @return The animated entity (no animation applied though)
	 */
	public static AnimatedModel loadEntity(Loader loader, String modelFile, String textureFile) {
		AnimatedModelData entityData = ColladaLoader.loadColladaModel(modelFile, Constants.MAX_WEIGHTS);
		RawModel model = loader.loadToVao(entityData.getMeshData());
		ModelTexture texture = new ModelTexture(loader.loadTexture(textureFile));
		TexturedModel texturedModel = new TexturedModel(model, texture);
		SkeletonData skeletonData = entityData.getJointsData();
		Joint headJoint = createJoints(skeletonData.headJoint);
		return new AnimatedModel(texturedModel, headJoint, skeletonData.jointCount);
	}

	/**
	 * Constructs the joint-hierarchy skeleton from the data extracted from the
	 * collada file.
	 * 
	 * @param data
	 *            - the joints data from the collada file for the head joint.
	 * @return The created joint, with all its descendants added.
	 */
	private static Joint createJoints(JointData data) {
		Joint joint = new Joint(data.index, data.nameId, data.bindLocalTransform);
		for (JointData child : data.children) {
			joint.addChild(createJoints(child));
		}
		return joint;
	}
	
}
