package com.resourcefulbees.resourcefulbees.init;

import com.google.gson.Gson;
import com.resourcefulbees.resourcefulbees.data.BeeTrait;
import com.resourcefulbees.resourcefulbees.data.JsonBeeTrait;
import com.resourcefulbees.resourcefulbees.registry.BiomeDictionary;
import com.resourcefulbees.resourcefulbees.registry.TraitRegistry;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.potion.Effect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.resourcefulbees.resourcefulbees.ResourcefulBees.LOGGER;

public class TraitSetup {

    public static Path DICTIONARY_PATH;

    public static void buildCustomTraits() {
        addTraits();
    }

    private static void parseType(File file) throws IOException {
        String name = file.getName();
        name = name.substring(0, name.indexOf('.'));

        Reader r = Files.newBufferedReader(file.toPath());

        parseType(r, name);
    }

    private static void parseType(ZipFile zf, ZipEntry zipEntry) throws IOException {
        String name = zipEntry.getName();
        name = name.substring(name.lastIndexOf("/") + 1, name.indexOf('.'));

        InputStream input = zf.getInputStream(zipEntry);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));

        parseType(reader, name);
    }

    private static void parseType(Reader reader, String name) {
        Gson gson = new Gson();
        JsonBeeTrait.jsonTrait jsonTrait = gson.fromJson(reader, JsonBeeTrait.jsonTrait.class);
        BeeTrait.Builder builder = new BeeTrait.Builder();
        if (jsonTrait.damageImmunities != null && jsonTrait.damageImmunities.length > 0){
            for (String damageImmunity : jsonTrait.damageImmunities){
                DamageSource source;
                switch (damageImmunity){
                    case "inFire": source = DamageSource.IN_FIRE; break;
                    case "lightningBolt": source = DamageSource.LIGHTNING_BOLT; break;
                    case "onFire": source = DamageSource.ON_FIRE; break;
                    case "lava": source = DamageSource.LAVA; break;
                    case "hotFloor": source = DamageSource.HOT_FLOOR; break;
                    case "inWall": source = DamageSource.IN_WALL; break;
                    case "cramming": source = DamageSource.CRAMMING; break;
                    case "drown": source = DamageSource.DROWN; break;
                    case "starve": source = DamageSource.STARVE; break;
                    case "cactus": source = DamageSource.CACTUS; break;
                    case "fall": source = DamageSource.FALL; break;
                    case "flyIntoWall": source = DamageSource.FLY_INTO_WALL; break;
                    case "generic": source = DamageSource.GENERIC; break;
                    case "magic": source = DamageSource.MAGIC; break;
                    case "wither": source = DamageSource.WITHER; break;
                    case "anvil": source = DamageSource.ANVIL; break;
                    case "fallingBlock": source = DamageSource.FALLING_BLOCK; break;
                    case "dragonBreath": source = DamageSource.DRAGON_BREATH; break;
                    case "dryout": source = DamageSource.DRYOUT; break;
                    default: throw new IllegalArgumentException("Damage Source supplied not valid.");
                }
                builder.addDamageImmunity(source);
            }
        }
        if (jsonTrait.damageTypes != null && !jsonTrait.damageTypes.isEmpty()){
            jsonTrait.damageTypes.forEach((damageType) -> builder.addDamageType(Pair.of(damageType.damageTypeName, damageType.amplifier)));
        }
        if (jsonTrait.specialAbilities != null && jsonTrait.specialAbilities.length > 0){
            for (String ability : jsonTrait.specialAbilities){
                builder.addSpecialAbility(ability);
            }
        }
        if (jsonTrait.particleName != null && !jsonTrait.particleName.isEmpty()) {
            if (ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation(jsonTrait.particleName)) != null && ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation(jsonTrait.particleName)) instanceof BasicParticleType){
                builder.setParticleEffect((BasicParticleType) ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation(jsonTrait.particleName)));
            }
        }
        if (jsonTrait.potionImmunities != null && jsonTrait.potionImmunities.length > 0){
            for (String immunity : jsonTrait.potionImmunities){
                Effect potion = ForgeRegistries.POTIONS.getValue(new ResourceLocation(immunity));
                if (potion != null)
                    builder.addPotionImmunity(potion);
            }
        }
        if (jsonTrait.potionDamageEffects != null && !jsonTrait.potionDamageEffects.isEmpty()){
            jsonTrait.potionDamageEffects.forEach((traitPotionDamageEffect -> {
                Effect potion = ForgeRegistries.POTIONS.getValue(new ResourceLocation(traitPotionDamageEffect.effectRegistryName));
                if (potion != null)
                    builder.addDamagePotionEffect(Pair.of(potion, MathHelper.clamp(traitPotionDamageEffect.amplifier, 0, 255)));
            }));
        }
        TraitRegistry.getRegistry().register(name, builder.build());
    }

    private static void addTraits() {
        try {
            Files.walk(DICTIONARY_PATH)
                    .filter(f -> f.getFileName().toString().endsWith(".zip"))
                    .forEach(TraitSetup::addZippedType);
            Files.walk(DICTIONARY_PATH)
                    .filter(f -> f.getFileName().toString().endsWith(".json"))
                    .forEach(TraitSetup::addType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addType(Path file) {
        File f = file.toFile();
        try {
            parseType(f);
        } catch (IOException e) {
            LOGGER.error("File not found when parsing biome types");
        }
    }

    private static void addZippedType(Path file) {
        try {
            ZipFile zf = new ZipFile(file.toString());
            zf.stream().forEach(zipEntry -> {
                if (zipEntry.getName().endsWith(".json")) {
                    try {
                        parseType(zf, zipEntry);
                    } catch (IOException e) {
                        String name = zipEntry.getName();
                        name = name.substring(name.lastIndexOf("/") + 1, name.indexOf('.'));
                        LOGGER.error("Could not parse {} biome type from ZipFile", name);
                    }
                }
            });
        } catch (IOException e) {
            LOGGER.warn("Could not read ZipFile! ZipFile: " + file.getFileName());
        }
    }
}
