package me.duncanruns.reversecrafingtable;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReverseCraftingTable implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("reverse-crafting-table");
    public static final String MOD_ID = "reverse-crafting-table";
    private static final Identifier TABLE_ID = new Identifier(MOD_ID, "table");

    public static final Block BLOCK = Registry.register(Registry.BLOCK, TABLE_ID, new ReverseCraftingTableBlock(FabricBlockSettings.copyOf(Blocks.CRAFTING_TABLE)));
    public static final BlockItem BLOCK_ITEM = Registry.register(Registry.ITEM, TABLE_ID, new BlockItem(BLOCK, new Item.Settings().group(ItemGroup.MISC)));

    @Override
    public void onInitialize() {

    }

    public static final ScreenHandlerType<ReverseCraftingTableScreenHandler> SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(TABLE_ID, ReverseCraftingTableScreenHandler::new);
    public static final BlockEntityType<ReverseCraftingTableBlockEntity> BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, TABLE_ID, BlockEntityType.Builder.create(ReverseCraftingTableBlockEntity::new, BLOCK).build(null));
}