package com.alexian123.terrain;

import java.util.Random;

public class HeightGenerator {
	
	private static final float AMPLITUDE = 70f;
	private static final float ROUGHNESS = 0.1f;
	private static final int OCTAVES = 4;
	
	private static final int X_NOISE_COEF = 49632;
	private static final int Z_NOISE_COEF = 325176;
	
	private final Random random = new Random();
	private final int seed;
	private final int xOffset;
	private final int zOffset;
	
	public HeightGenerator() {
		this.seed = random.nextInt(Integer.MAX_VALUE);
		this.xOffset = 0;
		this.zOffset = 0;
	}
	
	//only works with POSITIVE gridX and gridZ values!
	public HeightGenerator(int gridX, int gridZ, int vertexCount, int seed) {
		this.seed = Math.max(0, seed);
		xOffset = gridX * (vertexCount - 1);
		zOffset = gridZ * (vertexCount - 1);
	}
	
	public float generateHeight(int x, int z) {
		float total = 0;
		float d = (float) Math.pow(2, OCTAVES - 1);
		for(int i = 0; i < OCTAVES; ++i){
			float freq = (float) (Math.pow(2, i) / d);
			float amp = (float) Math.pow(ROUGHNESS, i) * AMPLITUDE;
			total += getInterpolatedNoise((x + xOffset) * freq, (z + zOffset) * freq) * amp;
		}
		return total;
	}
	
	private float getInterpolatedNoise(float x, float z) {
		int intX = (int) x;
		int intZ = (int) z;
		float fractX = x - intX;
		float fractZ = z - intZ;
		float v1 = getSmoothNoise(intX, intZ);
		float v2 = getSmoothNoise(intX + 1, intZ);
		float v3 = getSmoothNoise(intX, intZ + 1);
		float v4 = getSmoothNoise(intX + 1, intZ + 1);
		float i1 = cosInterpolation(v1, v2, fractX);
		float i2 = cosInterpolation(v3, v4, fractX);
		return cosInterpolation(i1, i2, fractZ);
	}
	
	private float cosInterpolation(float a, float b, float blend) {
		double theta = blend * Math.PI;
		float f = (float) (1f - Math.cos(theta)) * 0.5f;
		return a * (1f - f) + b * f;
	}
	
	private float getSmoothNoise(int x, int z) {
		float cornersNoise = (getNoise(x - 1, z - 1) + getNoise(x - 1, z + 1) + getNoise(x + 1, z + 1) + getNoise(x + 1, z - 1)) / 16f;
		float sidesNoise = (getNoise(x - 1, z) + getNoise(x + 1, z) + getNoise(x, z - 1) + getNoise(x, z + 1)) / 8f;
		float centerNoise = getNoise(x, z) / 4f;
		return cornersNoise + sidesNoise + centerNoise;
	}
	
	private float getNoise(int x, int z) {
		random.setSeed(x * X_NOISE_COEF + z * Z_NOISE_COEF + seed);
		return random.nextFloat() * 2f - 1f;
	}
}
