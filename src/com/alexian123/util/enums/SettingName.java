package com.alexian123.util.enums;

public enum SettingName {
    SCREEN_WIDTH("screenWidth"),
    SCREEN_HEIGHT("screenHeight"),
    WINDOW_TITLE("windowTitle"),
    FPS_CAP("fpsCap"),
    GRAVITY("gravity"),
    PLAYER_RUN_SPEED("playerRunSpeed"),
    PLAYER_TURN_SPEED("playerTurnSpeed"),
    PLAYER_JUMP_POWER("playerJumpPower"),
    MAX_PARTICLES("maxParticles"),
    NEAR_PLANE("nearPlane"),
    FAR_PLANE("farPlane"),
    FOV("fov"),
    AMBIENT_LIGHT("ambientLight"),
    BLOOM_FACTOR("bloomFactor"),
    CONTRAST("contrast"),
    BLUR_LEVEL("blurLevel"),
    FOG_COLOR_R("fogColorR"),
    FOG_COLOR_G("fogColorG"),
    FOG_COLOR_B("fogColorB"),
    FOG_DENSITY("fogDensity"),
    FOG_GRADIENT("fogGradient"),
    WATER_REFLECTION_WIDTH("waterReflectionWidth"),
    WATER_REFLECTION_HEIGHT("waterReflectionHeight"),
    WATER_REFRACTION_WIDTH("waterRefractionWidth"),
    WATER_REFRACTION_HEIGHT("waterRefractionHeight"),
    WATER_WAVE_SPEED("waterWaveSpeed"),
    WATER_SHINE_DAMPER("waterShineDamper"),
    WATER_REFLECTIVITY("waterReflectivity"),
    WATER_TILING_FACTOR("waterTilingFactor"),
    WATER_WAVE_STRENGTH("waterWaveStrength"),
    WATER_TILE_SIZE("waterTileSize"),
    TERRAIN_SEED("terrainSeed"),
    TERRAIN_AMPLITUDE("terrainAmplitude"),
    TERRAIN_ROUGHNESS("terrainRoughness"),
    TERRAIN_OCTAVES("terrainOctaves"),
    TERRAIN_TILE_SIZE("terrainTileSize"),
    SHADOW_MAP_SIZE("shadowMapSize"),
    PCF_COUNT("pcfCount"),
    SHADOW_DISTANCE("shadowDistance"),
    SHADOW_TRANSITION("shadowTransition"),
    SHADOWBOX_OFFSET("shadowboxOffset"),
    NUM_MULTISAMPLES("numMultisamples"),
    DEFAULT_CHARACTER_WIDTH("defaultCharacterWidth"),
    DEFAULT_CHARACTER_EDGE("defaultCharacterEdge"),
    DEFAULT_BORDER_WIDTH("defaultBorderWidth"),
    DEFAULT_BORDER_EDGE("defaultBorderEdge"),
    DESIRED_PADDING("desiredPadding"),
    LINE_HEIGHT("lineHeight"),
    MOUSE_PICKER_RECURSION_COUNT("mousePickerRecursionCount"),
    MOUSE_PICKER_RAY_RANGE("mousePickerRayRange"),
    ;

    private final String value;

    SettingName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
	
}
