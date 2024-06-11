package com.alexian123.particle;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.alexian123.engine.GameManager;
import com.alexian123.entity.Camera;
import com.alexian123.texture.ParticleTexture;
import com.alexian123.util.Constants;

public class Particle {
	
	private Vector3f position;
	private Vector3f velocity;
	
	private float gravityComplient;
	private float lifeLength;
	private float rotation;
	private float scale;
	
	private ParticleSystem parentSystem;
	
	private Vector2f currentAtlasOffset = new Vector2f();
	private Vector2f nextAtlasOffset = new Vector2f();
	private float blendFactor = 0;
	private float elapsedTime = 0;
	private float distanceToCamera = 0;

	private final Vector3f change = new Vector3f();
	
	public Particle(ParticleSystem parentSystem) {
		this.parentSystem = parentSystem;
		parentSystem.addDeadParticle(this);
	}
	
	public void revive(Vector3f position, Vector3f velocity, float gravityComplient, float lifeLength, float rotation,
			float scale) {
		this.position = position;
		this.velocity = velocity;
		this.gravityComplient = gravityComplient;
		this.lifeLength = lifeLength;
		this.rotation = rotation;
		this.scale = scale;
		this.elapsedTime = 0;
		this.blendFactor = 0;
		this.distanceToCamera = 0;
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

	public ParticleSystem getParentSystem() {
		return parentSystem;
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
		velocity.y += Constants.GRAVITY * gravityComplient * GameManager.getFrameTimeSeconds();
		change.set(velocity);
		change.scale(GameManager.getFrameTimeSeconds());
		Vector3f.add(change, position, position);
		distanceToCamera = Vector3f.sub(camera.getPosition(), position, null).lengthSquared();
		updateAtlasInfo();
		elapsedTime += GameManager.getFrameTimeSeconds();
		boolean alive = elapsedTime < lifeLength;
		if (!alive) {
			parentSystem.addDeadParticle(this);
		}
		return alive;
	}
	
	private void updateAtlasInfo() {
		ParticleTexture texture = parentSystem.getTexture();
		float lifeFactor = elapsedTime / lifeLength;
		int numStages = texture.getAtlasDimension() * texture.getAtlasDimension();
		float atlasProgress = lifeFactor * numStages;
		int currentStageIndex = (int) Math.floor(atlasProgress);
		int nextStageIndex = (currentStageIndex < numStages - 1) ? currentStageIndex + 1 : currentStageIndex;
		blendFactor = atlasProgress % 1;
		setAtlasOffset(currentAtlasOffset, currentStageIndex, texture);
		setAtlasOffset(nextAtlasOffset, nextStageIndex, texture);
	}
	
	private void setAtlasOffset(Vector2f offset, int index, ParticleTexture texture) {
		int column = index % texture.getAtlasDimension();
		int row = index / texture.getAtlasDimension();
		offset.x = (float) column / texture.getAtlasDimension();
		offset.y = (float) row / texture.getAtlasDimension();
	}
}
