package net.brights0ng.skylancer.item;

import net.brights0ng.skylancer.Skylancer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item GRID_TOOL = registerItem("grid_tool", new GridToolItem(new Item.Settings()));
    public static final Item BLOCK_TOOL = registerItem("block_tool", new BlockToolItem(new Item.Settings()));
    public static final Item SWITCH_TOOL = registerItem("switch_tool", new SwitchToolItem(new Item.Settings()));
    public static final ItemGroup SKYLANCER_TOOLS = Registry.register(Registries.ITEM_GROUP, Identifier.of(Skylancer.MOD_ID, "skylancer_tools"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.skylancer_tools"))
                    .icon(()-> new ItemStack(ModItems.GRID_TOOL)).entries((displayContext,entries) -> {
                        entries.add(ModItems.GRID_TOOL);
                        entries.add(ModItems.BLOCK_TOOL);
                        entries.add(ModItems.SWITCH_TOOL);
                    }).build());

    public static void addItemsToItemGroup(FabricItemGroupEntries group) {
        group.add(GRID_TOOL);
        group.add(BLOCK_TOOL);
        group.add(SWITCH_TOOL);
    }

    public static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, Identifier.of(Skylancer.MOD_ID, name) , item);
    }

    public static void registerItems() {
        Skylancer.LOGGER.info("Registering in game tools...");
    }
}
