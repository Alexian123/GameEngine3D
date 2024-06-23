package com.alexian123.loader.collada;

import com.alexian123.loader.Loader;
import com.alexian123.loader.data.AnimatedModelData;
import com.alexian123.loader.data.JointData;
import com.alexian123.loader.data.SkeletonData;
import com.alexian123.model.ModelMesh;
import com.alexian123.model.animated.AnimatedModel;
import com.alexian123.model.animated.Joint;
import com.alexian123.texture.ModelTexture;
import com.alexian123.util.Constants;

public class AnimatedModelFileLoader {

	public static AnimatedModel loadEntity(Loader loader, String modelFile, String textureFile) {
		AnimatedModelData entityData = ColladaLoader.loadColladaModel(modelFile, Constants.MAX_WEIGHTS);
		ModelMesh mesh = loader.loadToVao(entityData.getMeshData());
		ModelTexture texture = new ModelTexture(loader.loadTexture(textureFile));
		SkeletonData skeletonData = entityData.getJointsData();
		Joint headJoint = createJoints(skeletonData.headJoint);
		return new AnimatedModel(mesh, texture, headJoint, skeletonData.jointCount);
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
