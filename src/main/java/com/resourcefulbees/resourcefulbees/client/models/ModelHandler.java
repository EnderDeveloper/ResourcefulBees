package com.resourcefulbees.resourcefulbees.client.models;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.resourcefulbees.resourcefulbees.ResourcefulBees;
import com.resourcefulbees.resourcefulbees.lib.BeeConstants;
import com.resourcefulbees.resourcefulbees.registry.BeeRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.item.Item;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;

import java.util.Map;

public class ModelHandler {

    private static final Multimap<ResourceLocation, ResourceLocation> MODEL_MAP = LinkedHashMultimap.create();

    public static void registerModels(ModelRegistryEvent event) {
        IResourceManager resourceManager = Minecraft.getInstance().getResourceManager();

        BeeRegistry.getRegistry().getBees().forEach((string, customBee) -> {
            if (customBee.shouldResourcefulBeesDoForgeRegistration) {
                Block honeycombBlock = customBee.getCombBlockRegistryObject() != null ? customBee.getCombBlockRegistryObject().get() : null;
                Item honeycombBlockItem = customBee.getCombBlockItemRegistryObject() != null ? customBee.getCombBlockItemRegistryObject().get() : null;
                Item honeycomb = customBee.getCombRegistryObject() != null ? customBee.getCombRegistryObject().get() : null;
                Item spawnEgg = customBee.getSpawnEggItemRegistryObject() != null ? customBee.getSpawnEggItemRegistryObject().get() : null;

                if (customBee.hasHoneycomb() && !customBee.hasCustomDrop()) {
                    if (honeycombBlock != null && honeycombBlock.getRegistryName() != null && !resourceManager.hasResource(new ResourceLocation(ResourcefulBees.MOD_ID, "blockstates/" + honeycombBlock.getRegistryName().getPath() + ".json"))) {
                        honeycombBlock.getStateContainer().getValidStates().forEach(state -> {
                            String propertyMapString = BlockModelShapes.getPropertyMapString(state.getValues());
                            ModelResourceLocation defaultModelLocation = new ModelResourceLocation(
                                    ResourcefulBees.MOD_ID + ":honeycomb_block", propertyMapString);
                            ModelLoader.addSpecialModel(defaultModelLocation);
                            MODEL_MAP.put(defaultModelLocation, new ModelResourceLocation(honeycombBlock.getRegistryName(), propertyMapString));
                        });
                    }
                    if (honeycombBlockItem != null && honeycombBlockItem.getRegistryName() != null && !resourceManager.hasResource(new ResourceLocation(ResourcefulBees.MOD_ID, "item/models/" + honeycombBlockItem.getRegistryName().getPath() + ".json"))) {
                        ModelResourceLocation defaultModelLocation = new ModelResourceLocation(
                                ResourcefulBees.MOD_ID + ":honeycomb_block", "inventory");
                        ModelLoader.addSpecialModel(defaultModelLocation);
                        MODEL_MAP.put(defaultModelLocation, new ModelResourceLocation(honeycombBlockItem.getRegistryName(), "inventory"));
                    }
                    if (honeycomb != null && honeycomb.getRegistryName() != null && !resourceManager.hasResource(new ResourceLocation(ResourcefulBees.MOD_ID, "item/models/" + honeycomb.getRegistryName().getPath() + ".json"))) {
                        ModelResourceLocation defaultModelLocation = new ModelResourceLocation(
                                ResourcefulBees.MOD_ID + ":honeycomb", "inventory");
                        ModelLoader.addSpecialModel(defaultModelLocation);
                        MODEL_MAP.put(defaultModelLocation, new ModelResourceLocation(honeycomb.getRegistryName(), "inventory"));
                    }
                }
                if (spawnEgg != null && spawnEgg.getRegistryName() != null && !resourceManager.hasResource(new ResourceLocation(ResourcefulBees.MOD_ID, "item/models/" + spawnEgg.getRegistryName().getPath() + ".json"))) {
                    ModelResourceLocation defaultModelLocation = new ModelResourceLocation(
                            "minecraft:template_spawn_egg", "inventory");
                    ModelLoader.addSpecialModel(defaultModelLocation);
                    MODEL_MAP.put(defaultModelLocation, new ModelResourceLocation(spawnEgg.getRegistryName(), "inventory"));
                }
            }
        });

        BeeRegistry.getRegistry().getHoneyBottles().forEach((string, honeyData) -> {
            if (honeyData.shouldResourcefulBeesDoForgeRegistration) {
                Item honeyBottleItem = honeyData.getHoneyBottleRegistryObject() != null ? honeyData.getHoneyBottleRegistryObject().get() : null;
                Item honeyBlockItem = honeyData.getHoneyBlockItemRegistryObject() != null ? honeyData.getHoneyBlockItemRegistryObject().get() : null;
                Item honeyBucketItem = honeyData.getHoneyBucketItemRegistryObject() != null ? honeyData.getHoneyBucketItemRegistryObject().get() : null;
                Block honeyBlock = honeyData.getHoneyBlockRegistryObject() != null ? honeyData.getHoneyBlockRegistryObject().get() : null;
                FlowingFluidBlock honeyFluidBlock = honeyData.getHoneyFluidBlockRegistryObject() != null ? honeyData.getHoneyFluidBlockRegistryObject().get() : null;
                FlowingFluid honeyStillFluid = honeyData.getHoneyStillFluidRegistryObject() != null ? honeyData.getHoneyStillFluidRegistryObject().get() : null;
                FlowingFluid honeyFlowingFluid = honeyData.getHoneyFlowingFluidRegistryObject() != null ? honeyData.getHoneyFlowingFluidRegistryObject().get() : null;

                if (honeyStillFluid != null) {
                    RenderTypeLookup.setRenderLayer(honeyStillFluid, RenderType.getTranslucent());
                }
                if (honeyFlowingFluid != null) {
                    RenderTypeLookup.setRenderLayer(honeyFlowingFluid, RenderType.getTranslucent());
                }
                if (honeyFluidBlock != null) {
                    RenderTypeLookup.setRenderLayer(honeyFluidBlock, RenderType.getTranslucent());
                }
                if (honeyBlock != null) {
                    RenderTypeLookup.setRenderLayer(honeyBlock, RenderType.getTranslucent());
                }


                // Items
                if (honeyBottleItem != null && honeyBottleItem.getRegistryName() != null && !resourceManager.hasResource(new ResourceLocation(ResourcefulBees.MOD_ID, "item/models/" + honeyBottleItem.getRegistryName().getPath() + ".json"))) {
                    ModelResourceLocation defaultModelLocation = new ModelResourceLocation(
                            ResourcefulBees.MOD_ID + ":honey_bottle", "inventory");
                    ModelLoader.addSpecialModel(defaultModelLocation);
                    MODEL_MAP.put(defaultModelLocation, new ModelResourceLocation(honeyBottleItem.getRegistryName(), "inventory"));
                }
                if (honeyBlockItem != null && honeyBlockItem.getRegistryName() != null && !resourceManager.hasResource(new ResourceLocation(ResourcefulBees.MOD_ID, "item/models/" + honeyBlockItem.getRegistryName().getPath() + ".json"))) {
                    ModelResourceLocation defaultModelLocation = new ModelResourceLocation(
                            ResourcefulBees.MOD_ID + ":honey_block", "inventory");
                    ModelLoader.addSpecialModel(defaultModelLocation);
                    MODEL_MAP.put(defaultModelLocation, new ModelResourceLocation(honeyBlockItem.getRegistryName(), "inventory"));
                }
                if (honeyBucketItem != null && honeyBucketItem.getRegistryName() != null && !resourceManager.hasResource(new ResourceLocation(ResourcefulBees.MOD_ID, "item/models/" + honeyBucketItem.getRegistryName().getPath() + ".json"))) {
                    ModelResourceLocation defaultModelLocation = new ModelResourceLocation(
                            ResourcefulBees.MOD_ID + ":custom_honey_fluid_bucket", "inventory");
                    ModelLoader.addSpecialModel(defaultModelLocation);
                    MODEL_MAP.put(defaultModelLocation, new ModelResourceLocation(honeyBucketItem.getRegistryName(), "inventory"));
                }

                // Blocks
                if (honeyBlock != null && honeyBlock.getRegistryName() != null && !resourceManager.hasResource(new ResourceLocation(ResourcefulBees.MOD_ID, "blockstates/" + honeyBlock.getRegistryName().getPath() + ".json"))) {
                    honeyBlock.getStateContainer().getValidStates().forEach(state -> {
                        String propertyMapString = BlockModelShapes.getPropertyMapString(state.getValues());
                        ModelResourceLocation defaultModelLocation = new ModelResourceLocation(
                                ResourcefulBees.MOD_ID + ":honey_block", propertyMapString);
                        ModelLoader.addSpecialModel(defaultModelLocation);
                        MODEL_MAP.put(defaultModelLocation, new ModelResourceLocation(honeyBlock.getRegistryName(), propertyMapString));
                    });
                }
            }
        });
    }

    public static void onModelBake(ModelBakeEvent event) {
        Map<ResourceLocation, IBakedModel> modelRegistry = event.getModelRegistry();
        IBakedModel missingModel = modelRegistry.get(ModelLoader.MODEL_MISSING);
        MODEL_MAP.asMap().forEach(((resourceLocation, resourceLocations) -> {
            IBakedModel defaultModel = modelRegistry.getOrDefault(resourceLocation, missingModel);
            resourceLocations.forEach(modelLocation -> modelRegistry.put(modelLocation, defaultModel));
        }));
    }
}