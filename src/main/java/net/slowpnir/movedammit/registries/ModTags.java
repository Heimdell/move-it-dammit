package net.slowpnir.movedammit.registries;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.slowpnir.movedammit.MoveDammitMod.MOD_ID;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> VALUABLE_ORES = create("valuable_ores");
        public static final TagKey<Block> FRAMES = createCommon("carrying_frames");

        public static TagKey<Block> create(String name) {
            return TagKey.of(Registry.BLOCK_KEY, new Identifier(MOD_ID, name));
        }

        public static TagKey<Block> createCommon(String name) {
            return TagKey.of(Registry.BLOCK_KEY, new Identifier("c", name));
        }
    }

    public static class Items {
        public static TagKey<Item> create(String name) {
            return TagKey.of(Registry.ITEM_KEY, new Identifier(MOD_ID, name));
        }

        public static TagKey<Item> createCommon(String name) {
            return TagKey.of(Registry.ITEM_KEY, new Identifier("c", name));
        }

    }
}
