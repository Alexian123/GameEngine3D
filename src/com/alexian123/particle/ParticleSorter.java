package com.alexian123.particle;

import java.util.List;

public class ParticleSorter {
	
	public static void sortSystemsHighToLow(List<ParticleSystem> list) {
		for (int i = 1; i < list.size(); ++i) {
			ParticleSystem item = list.get(i);
			if (item.getAverageDistanceToCamera() > list.get(i - 1).getAverageDistanceToCamera()) {
				sortSystemsUpHighToLow(list, i);
			}
		}
	}
	
	public static void sortBatchHighToLow(List<Particle> batch) {
		for (int i = 1; i < batch.size(); ++i) {
			Particle item = batch.get(i);
			if (item.getDistanceToCamera() > batch.get(i - 1).getDistanceToCamera()) {
				sortBatchUpHighToLow(batch, i);
			}
		}
	}
	
	private static void sortSystemsUpHighToLow(List<ParticleSystem> list, int i) {
		ParticleSystem item = list.get(i);
		int attemptPos = i - 1;
		while (attemptPos != 0 && list.get(attemptPos - 1).getAverageDistanceToCamera() < item.getAverageDistanceToCamera()) {
			--attemptPos;
		}
		list.remove(i);
		list.add(attemptPos, item);
	}

	private static void sortBatchUpHighToLow(List<Particle> batch, int i) {
		Particle item = batch.get(i);
		int attemptPos = i - 1;
		while (attemptPos != 0 && batch.get(attemptPos - 1).getDistanceToCamera() < item.getDistanceToCamera()) {
			--attemptPos;
		}
		batch.remove(i);
		batch.add(attemptPos, item);
	}
}
