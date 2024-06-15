package com.alexian123.shader;

public enum Uniform {
	MODEL_TEXTURE("modelTexture"),
	TRANSFORMATION_MATRIX("transformationMatrix"),
	PROJECTION_MATRIX("projectionMatrix"),
	VIEW_MATRIX("viewMatrix"),
	LIGHT_POSITION("lightPosition"),
	LIGHT_COLOR("lightColor"),
	ATTENUATION("attenuation"),
	SHINE_DAMPER("shineDamper"),
	REFLECTIVITY("reflectivity"),
	USE_FAKE_LIGHTING("useFakeLighting"),
	FOG_DENSITY("fogDensity"),
	FOG_GRADIENT("fogGradient"),
	FOG_COLOR("fogColor"),
	ATLAS_DIMENSION("atlasDimension"),
	ATLAS_OFFSET("atlasOffset"),
	CLIP_PLANE("clipPlane"),
	NORMAL_MAP("normalMap"),
	BG_TEXTURE("bgTexture"),
	R_TEXTURE("rTexture"),
	G_TEXTURE("gTexture"),
	B_TEXTURE("bTexture"),
	BLEND_MAP("blendMap"),
	CUBE_MAP_0("cubeMap0"),
	CUBE_MAP_1("cubeMap1"),
	BLEND_FACTOR("blendFactor"),
	MODEL_MATRIX("modelMatrix"),
	REFLECTION_TEXTURE("reflectionTexture"),
	REFRACTION_TEXTURE("refractionTexture"),
	DUDV_MAP("dudvMap"),
	DEPTH_MAP("depthMap"),
	NEAR_PLANE("nearPlane"),
	FAR_PLANE("farPlane"),
	MOVE_FACTOR("moveFactor"),
	WAVE_STRENGTH("waveStrength"),
	CAMERA_POSITION("cameraPosition"),
	TILING_FACTOR("tilingFactor"),
	TRANSLATION("translation"),
	COLOR("color"),
	OUTLINE_COLOR("outlineColor"),
	OFFSET("offset"),
	CHARACTER_WIDTH("characterWidth"),
	CHARACTER_EDGE("characterEdige"),
	BORDER_WIDTH("borderWidth"),
	BORDER_EDGE("borderEdge"),
	LOWER_LIMIT("lowerLimit"),
	UPPER_LIMIT("upperLimit"),
	MVP_MATRIX("mvpMatrix"),
	TO_SHADOW_MAP_SPACE("toShadowMapSpace"),
	SHADOW_MAP("shadowMap"),
	SHADOW_DISTANCE("shadowDistance"),
	SHADOW_TRANSITION("shadowTransition"),
	SHADOW_MAP_SIZE("shadowMapSize"),
	PCF_COUNT("pcfCount"),
	CONTRAST("contrast"),
	;

	private final String name;
	
	private Uniform(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}