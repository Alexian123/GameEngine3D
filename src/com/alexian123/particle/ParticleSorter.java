package com.alexian123.particle;

import java.util.List;

public class ParticleSorter {
	
	public static void sortHighToLow(List<Particle> list) {
		for (int i = 1; i < list.size(); i++) {
			Particle item = list.get(i);
			if (item.getDistanceToCamera() > list.get(i - 1).getDistanceToCamera()) {
				sortUpHighToLow(list, i);
			}
		}
	}

	private static void sortUpHighToLow(List<Particle> list, int i) {
		Particle item = list.get(i);
		int attemptPos = i - 1;
		while (attemptPos != 0 && list.get(attemptPos - 1).getDistanceToCamera() < item.getDistanceToCamera()) {
			--attemptPos;
		}
		list.remove(i);
		list.add(attemptPos, item);
	}

}
