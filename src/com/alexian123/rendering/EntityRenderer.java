package com.alexian123.rendering;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.alexian123.entity.AnimatedEntity;
import com.alexian123.entity.Entity;
import com.alexian123.game.Camera;
import com.alexian123.lighting.Light;
import com.alexian123.model.ModelMesh;
import com.alexian123.model.TexturedModel;
import com.alexian123.shader.ShaderProgram;
import com.alexian123.texture.ModelTexture;
import com.alexian123.util.Constants;
import com.alexian123.util.enums.AttributeName;
import com.alexian123.util.enums.EntityType;
import com.alexian123.util.enums.UniformName;
import com.alexian123.util.gl.GLControl;
import com.alexian123.util.gl.TextureSampler;
import com.alexian123.util.gl.uniforms.UniformArrayMat4;
import com.alexian123.util.gl.uniforms.UniformArrayVec3;
import com.alexian123.util.gl.uniforms.UniformBoolean;
import com.alexian123.util.gl.uniforms.UniformFloat;
import com.alexian123.util.gl.uniforms.UniformInt;
import com.alexian123.util.gl.uniforms.UniformMat4;
import com.alexian123.util.gl.uniforms.UniformVec2;
import com.alexian123.util.gl.uniforms.UniformVec3;
import com.alexian123.util.gl.uniforms.UniformVec4;
import com.alexian123.util.mathematics.MatrixCreator;

public class EntityRenderer {
	
	private static final String VERTEX_SHADER_FILE = "entity";
	private static final String FRAGMENT_SHADER_FILE = "entity";
	
	private static final int MAX_NUM_TEXTURES = 4;
	
	private final Map<Integer, AttributeName> attributes = new HashMap<>();
	
	private final TextureSampler[] textures = new TextureSampler[MAX_NUM_TEXTURES];
	
	private final ShaderProgram shader;
	private final UniformMat4 projectionMatrix, viewMatrix, transformationMatrix, toShadowMapSpace;
	private final UniformArrayMat4 jointTransforms;
	private final UniformVec4 clipPlane;
	private final UniformVec3 fogColor;
	private final UniformVec2 atlasOffset;
	private final UniformArrayVec3 lightPosition, lightColor, attenuation;
	private final UniformFloat fogDensity, fogGradient, shineDamper, reflectivity, shadowDistance, shadowTransition,
								ambientLight, atlasDimension;
	private final UniformInt shadowMapSize, pcfCount, shadowMap, colorTexture, lightingMap, normalMap;
	private final UniformBoolean useFakeLighting, useLightingMap, useNormalMap, isAnimated;
	
	public EntityRenderer(TextureSampler shadowMapSampler) {
		attributes.put(0, AttributeName.POSITION);
		attributes.put(1, AttributeName.TEXTURE_COORD);
		attributes.put(2, AttributeName.NORMAL);
		attributes.put(3, AttributeName.TANGENT);
		attributes.put(4, AttributeName.JOINT_INDICES);
		attributes.put(5, AttributeName.WEIGHTS);
		
		shader = new ShaderProgram(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE, attributes);
		int id = shader.getProgramID();
		projectionMatrix = new UniformMat4(UniformName.PROJECTION_MATRIX, id);
		viewMatrix = new UniformMat4(UniformName.VIEW_MATRIX, id);
		transformationMatrix = new UniformMat4(UniformName.TRANSFORMATION_MATRIX, id);
		toShadowMapSpace = new UniformMat4(UniformName.TO_SHADOW_MAP_SPACE, id);
		jointTransforms = new UniformArrayMat4(UniformName.JOINT_TRANSFORMS, Constants.MAX_JOINTS, id);
		clipPlane = new UniformVec4(UniformName.CLIP_PLANE, id);
		fogColor = new UniformVec3(UniformName.FOG_COLOR, id);
		atlasOffset = new UniformVec2(UniformName.ATLAS_OFFSET, id);
		lightPosition = new UniformArrayVec3(UniformName.LIGHT_POSITION, Constants.MAX_LIGHTS, id);
		lightColor = new UniformArrayVec3(UniformName.LIGHT_COLOR, Constants.MAX_LIGHTS, id);
		attenuation = new UniformArrayVec3(UniformName.ATTENUATION, Constants.MAX_LIGHTS, id);
		fogDensity = new UniformFloat(UniformName.FOG_DENSITY, id);
		fogGradient = new UniformFloat(UniformName.FOG_GRADIENT, id);
		shineDamper = new UniformFloat(UniformName.SHINE_DAMPER, id);
		reflectivity = new UniformFloat(UniformName.REFLECTIVITY, id);
		shadowDistance = new UniformFloat(UniformName.SHADOW_DISTANCE, id);
		shadowTransition = new UniformFloat(UniformName.SHADOW_TRANSITION, id);
		ambientLight = new UniformFloat(UniformName.AMBIENT_LIGHT, id);
		atlasDimension = new UniformFloat(UniformName.ATLAS_DIMENSION, id);
		shadowMapSize = new UniformInt(UniformName.SHADOW_MAP_SIZE, id);
		pcfCount = new UniformInt(UniformName.PCF_COUNT, id);
		shadowMap = new UniformInt(UniformName.SHADOW_MAP, id);
		colorTexture = new UniformInt(UniformName.COLOR_TEXTURE, id);
		lightingMap = new UniformInt(UniformName.LIGHTING_MAP, id);
		normalMap = new UniformInt(UniformName.NORMAL_MAP, id);
		useFakeLighting = new UniformBoolean(UniformName.USE_FAKE_LIGHTING, id);
		useLightingMap = new UniformBoolean(UniformName.USE_LIGHTING_MAP, id);
		useNormalMap = new UniformBoolean(UniformName.USE_NORMAL_MAP, id);
		isAnimated = new UniformBoolean(UniformName.IS_ANIMATED, id);
		
		shader.start();
		projectionMatrix.load(Constants.PROJECTION_MATRIX);
		shadowDistance.load(Constants.SHADOW_DISTANCE);
		shadowTransition.load(Constants.SHADOW_TRANSITION);
		shadowMapSize.load(Constants.SHADOW_MAP_SIZE);
		ambientLight.load(Constants.AMBIENT_LIGHT);
		shadowMap.load(0);
		colorTexture.load(1);
		lightingMap.load(2);
		normalMap.load(3);
		textures[0] = shadowMapSampler;
		shader.stop();
	}
	
	public void render(Map<TexturedModel, List<Entity>> entities, List<Light> lights, Camera camera, Vector4f clipPlaneVal, Matrix4f toShadowMapSpaceMatrix) {
		shader.start();
		clipPlane.load(clipPlaneVal);
		toShadowMapSpace.load(toShadowMapSpaceMatrix);
		fogColor.load(Constants.FOG_COLOR);
		fogDensity.load(Constants.FOG_DENSITY);
		fogGradient.load(Constants.FOG_GRADIENT);
		
		for (TexturedModel model : entities.keySet()) {
			loadLights(model, lights, camera);
			viewMatrix.load(camera.getViewMatrix());
			prepareTexturedModel(model);
			for (Entity entity : entities.get(model)) {
				prepareEntity(entity);
				GLControl.drawElementsT(model.getMesh().getVertexCount());
			}
			GLControl.enableCulling();
			model.getMesh().getVao().unbind(0, 1, 2, 3, 4, 5);
		}
		shader.stop();
	}
	
	public void cleanup() {
		shader.cleanup();
	}
	
	private void loadLights(TexturedModel model, List<Light> lights, Camera camera) {
		Vector3f[] positions = new Vector3f[Constants.MAX_LIGHTS];
		Vector3f[] colors = new Vector3f[Constants.MAX_LIGHTS];
		Vector3f[] attenuations = new Vector3f[Constants.MAX_LIGHTS];
		if (model.getTexture().hasNormalMap()) {
			useNormalMap.load(true);
			storeLightsData(positions, colors, attenuations, lights, camera.getViewMatrix());
		} else {
			useNormalMap.load(false);
			storeLightsData(positions, colors, attenuations, lights);
		}
		lightPosition.load(positions);
		lightColor.load(colors);
		attenuation.load(attenuations);
	}
	
	private void storeLightsData(Vector3f[] positions, Vector3f[] colors, Vector3f[] attenuations, List<Light> lights) {
		for (int i = 0; i < Constants.MAX_LIGHTS; ++i) {
			Light light = (i < lights.size()) ? lights.get(i) : Light.NO_LIGHT;
			positions[i] = light.getPosition();
			colors[i] = light.getColor();
			attenuations[i] = light.getAttenuation();
		}
	}
	
	private void storeLightsData(Vector3f[] positions, Vector3f[] colors, Vector3f[] attenuations, List<Light> lights, Matrix4f view) {
		for (int i = 0; i < Constants.MAX_LIGHTS; ++i) {
			Light light = (i < lights.size()) ? lights.get(i) : Light.NO_LIGHT;
			positions[i] = getEyeSpaceLightPosition(light, view);
			colors[i] = light.getColor();
			attenuations[i] = light.getAttenuation();
		}
	}
	
	private Vector3f getEyeSpaceLightPosition(Light light, Matrix4f viewMatrix) {
		Vector3f position = light.getPosition();
		Vector4f eyeSpacePos = new Vector4f(position.x,position.y, position.z, 1f);
		Matrix4f.transform(viewMatrix, eyeSpacePos, eyeSpacePos);
		return new Vector3f(eyeSpacePos);
	}
	
	private void prepareTexturedModel(TexturedModel model) {
		ModelMesh rawModel = model.getMesh();
		ModelTexture texture = model.getTexture();
		textures[1] = texture.getColorTexture();
		textures[2] = texture.getLightingMap();
		textures[3] = texture.getNormalMap();
		
		rawModel.getVao().bind(0, 1, 2, 3, 4, 5);
		
		if (texture.isUsingTransparency()) {
			GLControl.disableCulling();
		}
		
		for (int i = 0; i < MAX_NUM_TEXTURES; ++i) {
			if (textures[i] != null) {
				textures[i].bindToUnit(i);
			}
		}
		
		atlasDimension.load((float) texture.getAtlasDimension());
		useFakeLighting.load(texture.isUsingFakeLighting());
		shineDamper.load(texture.getShineDamper());
		reflectivity.load(texture.getReflectivity());
		useLightingMap.load(texture.hasLightingMap());
	}
	
	private void prepareEntity(Entity entity) {
		Matrix4f matrix = MatrixCreator.createTransformationMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
		transformationMatrix.load(matrix);
		atlasOffset.load(new Vector2f(entity.getTextureXOffset(), entity.getTextureYOffset()));
		pcfCount.load(entity.isNoShading() ? -1 : Constants.PCF_COUNT);
		if (entity.getType() == EntityType.ANIMATED) {
			AnimatedEntity animatedEntity = (AnimatedEntity) entity;
			isAnimated.load(true);
			jointTransforms.load(animatedEntity.getModel().getJointTransforms());
		} else {
			isAnimated.load(false);
		}
	}
}
