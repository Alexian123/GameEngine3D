package com.alexian123.loader.collada;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.alexian123.animation.Animation;
import com.alexian123.animation.JointTransform;
import com.alexian123.animation.KeyFrame;
import com.alexian123.loader.data.AnimationData;
import com.alexian123.loader.data.JointTransformData;
import com.alexian123.loader.data.KeyFrameData;
import com.alexian123.util.mathematics.Quaternion;

/**
 * This class loads up an animation collada file, gets the information from it,
 * and then creates and returns an {@link Animation} from the extracted data.
 * 
 * @author Karl
 *
 */
public class AnimationFileLoader {

	/**
	 * Loads up a collada animation file, and returns and animation created from
	 * the extracted animation data from the file.
	 * 
	 * @param colladaFile
	 *            - the collada file containing data about the desired
	 *            animation.
	 * @return The animation made from the data in the file.
	 */
	public static Animation loadAnimation(String fileName) {
		AnimationData animationData = ColladaLoader.loadColladaAnimation(fileName);
		KeyFrame[] frames = new KeyFrame[animationData.keyFrames.length];
		for (int i = 0; i < frames.length; i++) {
			frames[i] = createKeyFrame(animationData.keyFrames[i]);
		}
		return new Animation(animationData.lengthSeconds, frames);
	}

	/**
	 * Creates a keyframe from the data extracted from the collada file.
	 * 
	 * @param data
	 *            - the data about the keyframe that was extracted from the
	 *            collada file.
	 * @return The keyframe.
	 */
	private static KeyFrame createKeyFrame(KeyFrameData data) {
		Map<String, JointTransform> map = new HashMap<String, JointTransform>();
		for (JointTransformData jointData : data.jointTransforms) {
			JointTransform jointTransform = createTransform(jointData);
			map.put(jointData.jointNameId, jointTransform);
		}
		return new KeyFrame(data.time, map);
	}

	/**
	 * Creates a joint transform from the data extracted from the collada file.
	 * 
	 * @param data
	 *            - the data from the collada file.
	 * @return The joint transform.
	 */
	private static JointTransform createTransform(JointTransformData data) {
		Matrix4f mat = data.jointLocalTransform;
		Vector3f translation = new Vector3f(mat.m30, mat.m31, mat.m32);
		Quaternion rotation = Quaternion.fromMatrix(mat);
		return new JointTransform(translation, rotation);
	}

}