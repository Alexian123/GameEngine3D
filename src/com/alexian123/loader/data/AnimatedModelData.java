package com.alexian123.loader.data;

/**
 * Contains the extracted data for an animated model, which includes the mesh data, and skeleton (joints heirarchy) data.
 * @author Karl
 *
 */
public class AnimatedModelData {

	private final SkeletonData joints;
	private final AnimatedMeshData mesh;
	
	public AnimatedModelData(AnimatedMeshData mesh, SkeletonData joints) {
		this.joints = joints;
		this.mesh = mesh;
	}
	
	public SkeletonData getJointsData() {
		return joints;
	}
	
	public AnimatedMeshData getMeshData() {
		return mesh;
	}
	
}