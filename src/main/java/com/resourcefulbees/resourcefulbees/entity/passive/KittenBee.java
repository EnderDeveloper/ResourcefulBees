package com.resourcefulbees.resourcefulbees.entity.passive;

import com.resourcefulbees.resourcefulbees.api.beedata.*;
import com.resourcefulbees.resourcefulbees.lib.BaseModelTypes;
import com.resourcefulbees.resourcefulbees.lib.BeeConstants;
import com.resourcefulbees.resourcefulbees.lib.LightLevels;
import com.resourcefulbees.resourcefulbees.registry.BeeRegistry;
import com.resourcefulbees.resourcefulbees.registry.ModBlocks;
import com.resourcefulbees.resourcefulbees.registry.ModFluids;
import com.resourcefulbees.resourcefulbees.registry.ModItems;
import net.minecraft.potion.Effects;

public class KittenBee {

    public static void register() {
        BeeRegistry.getRegistry().registerBee(BeeConstants.KITTEN_BEE, getKittenBeeData());
    }

    private static CustomBeeData getKittenBeeData() {
        CustomBeeData data = new CustomBeeData.Builder(BeeConstants.KITTEN_BEE, "tag:minecraft:beds", true,
                MutationData.createDefault(),
                new ColorData.Builder(false)
                        .setPrimaryColor("#EAA939")
                        .setSecondaryColor("#4C483B")
                        .createColorData(),
                new CombatData.Builder(false)
                        .setAttackDamage(0f)
                        .setRemoveStingerOnAttack(false)
                        .create(),
                new CentrifugeData.Builder(true, "minecraft:cat_spawn_egg")
                        .setBottleOutput("resourcefulbees:catnip_honey_bottle")
                        .setBottleOutputWeight(0.1f)
                        .setMainOutputWeight(0.005f)
                        .createCentrifugeData(),
                BreedData.createDefault(),
                new SpawnData.Builder(true)
                        .setSpawnWeight(3)
                        .setBiomeWhitelist("tag:rare")
                        .setLightLevel(LightLevels.DAY)
                        .setMinGroupSize(1)
                        .setMaxGroupSize(1)
                        .createSpawnData(),
                new TraitData(true))
                .setEasterEggBee(true)
                .setBaseLayerTexture("/kitten/bee")
                .setMaxTimeInHive(500)
                .setBaseModelType(BaseModelTypes.KITTEN)
                .setSizeModifier(0.75f)
                .setTraits(new String[]{BeeConstants.KITTEN_BEE})
                .createCustomBee();

        data.shouldResourcefulBeesDoForgeRegistration = true;
        data.setCombRegistryObject(ModItems.CATNIP_HONEYCOMB);
        data.setCombBlockItemRegistryObject(ModItems.CATNIP_HONEYCOMB_BLOCK_ITEM);
        data.setCombBlockRegistryObject(ModBlocks.CATNIP_HONEY_BLOCK);

        return data;
    }

    private static HoneyBottleData honeyBottleData = null;

    public static HoneyBottleData getHoneyBottleData() {
        if (honeyBottleData == null) {
            HoneyBottleData.Builder builder = new HoneyBottleData.Builder("catnip", 8, 1.6f, "#BD5331");
            HoneyEffect speed = new HoneyEffect(Effects.SPEED.getRegistryName().toString(), 2400, 2, 1);
            HoneyEffect nightVistion = new HoneyEffect(Effects.NIGHT_VISION.getRegistryName().toString(), 2400, 0, 1);
            HoneyEffect jump = new HoneyEffect(Effects.JUMP_BOOST.getRegistryName().toString(), 2400, 1, 1);
            builder.addEffect(speed).addEffect(nightVistion).addEffect(jump);
            honeyBottleData = builder.build();
            honeyBottleData.setHoneyBlockRegistryObject(ModBlocks.CATNIP_HONEY_BLOCK);
            honeyBottleData.setHoneyStillFluidRegistryObject(ModFluids.CATNIP_HONEY_STILL);
            honeyBottleData.setHoneyFlowingFluidRegistryObject(ModFluids.CATNIP_HONEY_FLOWING);
            honeyBottleData.setHoneyBlockItemRegistryObject(ModItems.CATNIP_HONEY_BLOCK_ITEM);
            honeyBottleData.setHoneyBucketItemRegistryObject(ModItems.CATNIP_HONEY_FLUID_BUCKET);
            honeyBottleData.setHoneyBottleRegistryObject(ModItems.CATNIP_HONEY_BOTTLE);
        }
        return honeyBottleData;
    }
}
