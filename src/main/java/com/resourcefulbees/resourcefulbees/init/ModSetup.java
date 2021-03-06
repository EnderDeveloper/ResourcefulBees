package com.resourcefulbees.resourcefulbees.init;

import com.resourcefulbees.resourcefulbees.ResourcefulBees;
import com.resourcefulbees.resourcefulbees.item.dispenser.ScraperDispenserBehavior;
import com.resourcefulbees.resourcefulbees.item.dispenser.ShearsDispenserBehavior;
import com.resourcefulbees.resourcefulbees.mixin.DispenserBlockInvoker;
import com.resourcefulbees.resourcefulbees.registry.ModItems;
import com.resourcefulbees.resourcefulbees.utils.color.RainbowColor;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.resources.FolderPack;
import net.minecraft.resources.IPackNameDecorator;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.resourcefulbees.resourcefulbees.ResourcefulBees.LOGGER;

public class ModSetup {

    private ModSetup() {
        throw new IllegalStateException("Utility Class");
    }

    public static void initialize(){
        setupPaths();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ModSetup::loadResources);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> RainbowColor::init);
    }

    private static void setupPaths(){
        Path configPath = FMLPaths.CONFIGDIR.get();
        Path rbBeesPath = Paths.get(configPath.toAbsolutePath().toString(), ResourcefulBees.MOD_ID, "bees");
        Path rbBiomePath = Paths.get(configPath.toAbsolutePath().toString(), ResourcefulBees.MOD_ID, "biome_dictionary");
        Path rbTraitPath = Paths.get(configPath.toAbsolutePath().toString(), ResourcefulBees.MOD_ID, "bee_traits");
        Path rbAssetsPath = Paths.get(configPath.toAbsolutePath().toString(),ResourcefulBees.MOD_ID, "resources");
        Path rbHoneyPath = Paths.get(configPath.toAbsolutePath().toString(), ResourcefulBees.MOD_ID, "honey");
        BeeSetup.setBeePath(rbBeesPath);
        BeeSetup.setResourcePath(rbAssetsPath);
        BeeSetup.setHoneyPath(rbHoneyPath);
        BiomeDictionarySetup.DICTIONARY_PATH = rbBiomePath;
        TraitSetup.DICTIONARY_PATH = rbTraitPath;

        try { Files.createDirectories(rbBeesPath);
        } catch (FileAlreadyExistsException ignored) { //ignored
        } catch (IOException e) { LOGGER.error("failed to create \"bees\" directory");}

        try { Files.createDirectories(rbBiomePath);
        } catch (FileAlreadyExistsException ignored) { //ignored
        } catch (IOException e) { LOGGER.error("failed to create \"biome_dictionary\" directory");}

        try { Files.createDirectories(rbTraitPath);
        } catch (FileAlreadyExistsException ignored) { //ignored
        } catch (IOException e) { LOGGER.error("failed to create \"bee_traits\" directory");}

        try { Files.createDirectory(rbAssetsPath);
        } catch (FileAlreadyExistsException ignored) { //ignored
        } catch (IOException e) { LOGGER.error("Failed to create \"assets\" directory");}

        try { Files.createDirectories(rbHoneyPath);
        } catch (FileAlreadyExistsException ignored) { //ignored
        } catch (IOException e) { LOGGER.error("failed to create \"honey\" directory");}

        try (FileWriter file = new FileWriter(Paths.get(rbAssetsPath.toAbsolutePath().toString(), "pack.mcmeta").toFile())) {
            String mcMetaContent = "{\"pack\":{\"pack_format\":6,\"description\":\"Resourceful Bees resource pack used for lang purposes for the user to add lang for bee/items.\"}}";
            file.write(mcMetaContent);
        } catch (FileAlreadyExistsException ignored) { //ignored
        } catch (IOException e) { LOGGER.error("Failed to create pack.mcmeta file for resource loading");}
    }

    public static void registerDispenserBehaviors() {
        ShearsDispenserBehavior.DEFAULT_SHEARS_DISPENSE_BEHAVIOR =
                ((DispenserBlockInvoker) Blocks.DISPENSER).invokeGetBehaviorForItem(new ItemStack(Items.SHEARS));

        DispenserBlock.registerDispenseBehavior(net.minecraft.item.Items.SHEARS.asItem(), new ShearsDispenserBehavior());

        DispenserBlock.registerDispenseBehavior(ModItems.SCRAPER.get().asItem(), new ScraperDispenserBehavior());
    }

    public static void loadResources() {
        Minecraft.getInstance().getResourcePackList().addPackFinder((consumer, factory) -> {
            final ResourcePackInfo packInfo = ResourcePackInfo.createResourcePack(
                    ResourcefulBees.MOD_ID,
                    true,
                    () -> new FolderPack(BeeSetup.getResourcePath().toFile()),
                    factory,
                    ResourcePackInfo.Priority.TOP,
                    IPackNameDecorator.method_29485()
            );
            if (packInfo == null) {
                LOGGER.error("Failed to load resource pack, some things may not work.");
                return;
            }
            consumer.accept(packInfo);
        });
    }
}
