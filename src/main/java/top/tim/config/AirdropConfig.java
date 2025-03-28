package top.tim.config;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tomlj.Toml;
import org.tomlj.TomlArray;
import org.tomlj.TomlParseResult;
import org.tomlj.TomlTable;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * IntelliJ IDEA
 * Tim_Random_Things AirdropConfig
 *
 * @author albus
 * @since 2025/3/28 15:57
 */
public class AirdropConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger("AirdropConfig");
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "airdrop_items.toml");
    public static List<ConfigItem> ITEMS = new ArrayList<>();

    public static class ConfigItem {
        Identifier itemId;
        int weight;
        int minCount;
        int maxCount;
        String displayName;
        List<Pair<String, Integer>> enchants;
    }


}
