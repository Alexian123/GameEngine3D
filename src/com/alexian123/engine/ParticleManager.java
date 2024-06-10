package com.alexian123.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alexian123.entity.Camera;
import com.alexian123.loader.Loader;
import com.alexian123.particle.Particle;
import com.alexian123.particle.ParticleSorter;
import com.alexian123.particle.ParticleSystem;
import com.alexian123.rendering.ParticleRenderer;

public class ParticleManager {

	private static List<ParticleSystem> systemsOrder = new ArrayList<>();
	private static Map<ParticleSystem, List<Particle>> particles = new HashMap<>();
	
	private static ParticleRenderer renderer;
	
	private static boolean isInitialized = false;
	
	public static void init(Loader loader) {
		if (!isInitialized) {
			renderer = new ParticleRenderer(loader);
			isInitialized = true;
		}
	}
	
	public static void renderParticles(Camera camera) {
		updateParticles(camera);
		renderer.render(systemsOrder, particles, camera);
	}
	
	public static void cleanup() {
		renderer.cleanup();
		particles.clear();
	}
	
	public static void addParticle(Particle particle) {
		ParticleSystem system = particle.getParentSystem();
		List<Particle> batch = particles.get(system);
		if (batch == null) {
			batch = new ArrayList<>();
			particles.put(system,  batch);
		}
		batch.add(particle);
	}
	
	private static void updateParticles(Camera camera) {
		systemsOrder.clear();
		Iterator<Entry<ParticleSystem, List<Particle>>> mapIter = particles.entrySet().iterator();
		while (mapIter.hasNext()) {
			Entry<ParticleSystem, List<Particle>> entry = mapIter.next();
			ParticleSystem system = entry.getKey();
			systemsOrder.add(system);
			List<Particle> batch = entry.getValue();
			Iterator<Particle> iter = batch.iterator();
			float sumDistanceToCamera = 0f;
			while (iter.hasNext()) {
				Particle p = iter.next();
				boolean alive = p.update(camera);
				if (alive) {
					sumDistanceToCamera += p.getDistanceToCamera();
				} else {
					iter.remove();
					if (batch.isEmpty()) {
						mapIter.remove();
						systemsOrder.remove(system);
					}
				}
			}
			system.setAverageDistanceToCamera(sumDistanceToCamera / batch.size());
			if (!system.getTexture().isAdditiveBlending()) {
				ParticleSorter.sortBatchHighToLow(batch);
			}
		}
		ParticleSorter.sortSystemsHighToLow(systemsOrder);
	}
	
}
