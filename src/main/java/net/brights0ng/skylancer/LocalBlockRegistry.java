package net.brights0ng.skylancer;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class LocalBlockRegistry {

    public static final Block LOCAL_BLOCK = Registry.register(
            Registries.BLOCK,
            Identifier.tryParse("your_mod_id:my_custom_block"),
            new LocalBlock(AbstractBlock.Settings.create().strength(4.0f).burnable())
    );

    public static void register() {
        System.out.println("Registering Blocks for Skylancer");
    }
}
