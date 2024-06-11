package com.alexian123.particle;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.alexian123.engine.GameManager;
import com.alexian123.engine.ParticleManager;
import com.alexian123.texture.ParticleTexture;
import com.alexian123.util.Constants;

public class ParticleSystem {
	
	private final Queue<Particle> deadParticles = new LinkedList<>();
	
	private final ParticleTexture texture;

	private final float pps;
	private final float averageSpeed;
	private final float gravityComplient;
	private final float averageLifeLength;
	private final float averageScale;
	private Vector3f center;

	private float speedError, lifeError, scaleError = 0;
	private boolean randomRotation = false;
	private Vector3f direction;
	private float directionDeviation = 0;

	private Random random = new Random();
	
	private float averageDistanceToCamera;
	

	public ParticleSystem(ParticleTexture texture, float pps, float speed, float gravityComplient, float lifeLength, float scale) {
		this(texture, pps, speed, gravityComplient, lifeLength, scale, new Vector3f());
	}
	
	public ParticleSystem(ParticleTexture texture, float pps, float speed, float gravityComplient, float lifeLength, float scale, Vector3f center) {
		this.texture = texture;
		this.pps = pps;
		this.averageSpeed = speed;
		this.gravityComplient = gravityComplient;
		this.averageLifeLength = lifeLength;
		this.averageScale = scale;
		this.center = center;
		
		// create available particles
		for (int i = 0; i < Constants.MAX_PARTICLES; ++i) {
			new Particle(this);
		}
	}

	/**
	 * @param direction - The average direction in which particles are emitted.
	 * @param deviation - A value between 0 and 1 indicating how far from the chosen direction particles can deviate.
	 */
	public void setDirection(Vector3f direction, float deviation) {
		this.direction = new Vector3f(direction);
		this.directionDeviation = (float) (deviation * Math.PI);
	}

	public void randomizeRotation() {
		randomRotation = true;
	}

	/**
	 * @param error
	 *            - A number between 0 and 1, where 0 means no error margin.
	 */
	public void setSpeedError(float error) {
		this.speedError = error * averageSpeed;
	}

	/**
	 * @param error
	 *            - A number between 0 and 1, where 0 means no error margin.
	 */
	public void setLifeError(float error) {
		this.lifeError = error * averageLifeLength;
	}

	/**
	 * @param error
	 *            - A number between 0 and 1, where 0 means no error margin.
	 */
	public void setScaleError(float error) {
		this.scaleError = error * averageScale;
	}
	
	/**
	 * @param center
	 *            - The center of the particle system
	 */
	public void setCenter(Vector3f center) {
		this.center = center;
	}

	/**
	 * Start generating particles from the specified center
	 * 
	 * @param center
	 */
	public void generateParticles() {
		float delta = GameManager.getFrameTimeSeconds();
		float particlesToCreate = pps * delta;
		int count = (int) Math.floor(particlesToCreate);
		float partialParticle = particlesToCreate % 1;
		for (int i = 0; i < count; i++) {
			emitParticle();
		}
		if (Math.random() < partialParticle) {
			emitParticle();
		}
	}
	
	/**
	 * @return The Particle texture
	 */
	public ParticleTexture getTexture() {
		return texture;
	}
	
	/**
	 * @param averageDistanceToCamera
	 * 			- the average distance from all the particles to the camera
	 */
	public void setAverageDistanceToCamera(float averageDistanceToCamera) {
		this.averageDistanceToCamera = averageDistanceToCamera;
	}
	
	/**
	 * @return The average distance from the particles to the camera
	 */
	public float getAverageDistanceToCamera() {
		return averageDistanceToCamera;
	}
	
	/**
	 * Adds a particle to the queue of dead particles
	 * 
	 * @param particle
	 */
	public void addDeadParticle(Particle particle) {
		deadParticles.add(particle);
	}

	private void emitParticle() {
		Vector3f velocity = null;
		if(direction!=null){
			velocity = generateRandomUnitVectorWithinCone(direction, directionDeviation);
		}else{
			velocity = generateRandomUnitVector();
		}
		velocity.normalise();
		velocity.scale(generateValue(averageSpeed, speedError));
		float scale = generateValue(averageScale, scaleError);
		float lifeLength = generateValue(averageLifeLength, lifeError);
		Particle p = deadParticles.poll();
		if (p != null) {
			p.revive(new Vector3f(center), velocity, gravityComplient, lifeLength, generateRotation(), scale);
			ParticleManager.addParticle(p);
		}
	}

	private float generateValue(float average, float errorMargin) {
		float offset = (random.nextFloat() - 0.5f) * 2f * errorMargin;
		return average + offset;
	}

	private float generateRotation() {
		if (randomRotation) {
			return random.nextFloat() * 360f;
		} else {
			return 0;
		}
	}

	private static Vector3f generateRandomUnitVectorWithinCone(Vector3f coneDirection, float angle) {
		float cosAngle = (float) Math.cos(angle);
		Random random = new Random();
		float theta = (float) (random.nextFloat() * 2f * Math.PI);
		float z = cosAngle + (random.nextFloat() * (1 - cosAngle));
		float rootOneMinusZSquared = (float) Math.sqrt(1 - z * z);
		float x = (float) (rootOneMinusZSquared * Math.cos(theta));
		float y = (float) (rootOneMinusZSquared * Math.sin(theta));

		Vector4f direction = new Vector4f(x, y, z, 1);
		if (coneDirection.x != 0 || coneDirection.y != 0 || (coneDirection.z != 1 && coneDirection.z != -1)) {
			Vector3f rotateAxis = Vector3f.cross(coneDirection, new Vector3f(0, 0, 1), null);
			rotateAxis.normalise();
			float rotateAngle = (float) Math.acos(Vector3f.dot(coneDirection, new Vector3f(0, 0, 1)));
			Matrix4f rotationMatrix = new Matrix4f();
			rotationMatrix.rotate(-rotateAngle, rotateAxis);
			Matrix4f.transform(rotationMatrix, direction, direction);
		} else if (coneDirection.z == -1) {
			direction.z *= -1;
		}
		return new Vector3f(direction);
	}
	
	private Vector3f generateRandomUnitVector() {
		float theta = (float) (random.nextFloat() * 2f * Math.PI);
		float z = (random.nextFloat() * 2) - 1;
		float rootOneMinusZSquared = (float) Math.sqrt(1 - z * z);
		float x = (float) (rootOneMinusZSquared * Math.cos(theta));
		float y = (float) (rootOneMinusZSquared * Math.sin(theta));
		return new Vector3f(x, y, z);
	}

}