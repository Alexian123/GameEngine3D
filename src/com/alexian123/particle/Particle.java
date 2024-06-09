package com.alexian123.particle;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.alexian123.entity.Camera;
import com.alexian123.rendering.DisplayManager;
import com.alexian123.texture.ParticleTexture;
import com.alexian123.util.Constants;

public class Particle {
	
	private final Vector3f position;
	private final Vector3f velocity;
	
	private final float gravityComplient;
	private final float lifeLength;
	private final float rotation;
	private final float scale;
	
	private final ParticleTexture texture;
	
	private Vector2f currentAtlasOffset = new Vector2f();
	private Vector2f nextAtlasOffset = new Vector2f();
	private float blendFactor = 0;
	private float elapsedTime = 0;
	private float distanceToCamera = 0;

	public Particle(Vector3f position, Vector3f velocity, float gravityComplient, float lifeLength, float rotation,
			float scale, ParticleTexture texture) {
		this.position = position;
		this.velocity = velocity;
		this.gravityComplient = gravityComplient;
		this.lifeLength = lifeLength;
		this.rotation = rotation;
		this.scale = scale;
		this.texture = texture;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getRotation() {
		return rotation;
	}

	public float getScale() {
		return scale;
	}

	public ParticleTexture getTexture() {
		return texture;
	}

	public Vector2f getCurrentAtlasOffset() {
		return currentAtlasOffset;
	}

	public Vector2f getNextAtlasOffset() {
		return nextAtlasOffset;
	}

	public float getBlendFactor() {
		return blendFactor;
	}

	public float getDistanceToCamera() {
		return distanceToCamera;
	}

	public boolean update(Camera camera) {
		velocity.y += Constants.GRAVITY * gravityComplient * DisplayManager.getFrameTimeSeconds();
		Vector3f change = new Vector3f(velocity);
		change.scale(DisplayManager.getFrameTimeSeconds());
		Vector3f.add(change, position, position);
		distanceToCamera = Vector3f.sub(camera.getPosition(), position, null).lengthSquared();
		updateAtlasInfo();
		elapsedTime += DisplayManager.getFrameTimeSeconds();
		return elapsedTime < lifeLength;
	}
	
	private void updateAtlasInfo() {
		float lifeFactor = elapsedTime / lifeLength;
		int numStages = texture.getAtlasDimension() * texture.getAtlasDimension();
		float atlasProgress = lifeFactor * numStages;
		int currentStageIndex = (int) Math.floor(atlasProgress);
		int nextStageIndex = (currentStageIndex < numStages - 1) ? currentStageIndex + 1 : currentStageIndex;
		this.blendFactor = atlasProgress % 1;
		setAtlasOffset(this.currentAtlasOffset, currentStageIndex);
		setAtlasOffset(this.nextAtlasOffset, nextStageIndex);
	}
	
	private void setAtlasOffset(Vector2f offset, int index) {
		int column = index % texture.getAtlasDimension();
		int row = index / texture.getAtlasDimension();
		offset.x = (float) column / texture.getAtlasDimension();
		offset.y = (float) row / texture.getAtlasDimension();
	}
}
