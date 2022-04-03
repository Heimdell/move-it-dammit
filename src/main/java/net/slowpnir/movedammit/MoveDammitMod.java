package net.slowpnir.movedammit;

import net.fabricmc.api.ModInitializer;
import net.slowpnir.movedammit.block.ModBlocks;
import net.slowpnir.movedammit.item.ModItems;
import net.slowpnir.movedammit.registries.Registries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoveDammitMod implements ModInitializer {
	public static final String MOD_ID = "movedammit";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		ModItems.registerItems();
		ModBlocks.registerBlocks();
		Registries.registerVarious();

		LOGGER.info("Hello Fabric world!");
	}

}
