package me.duncanruns.reversecrafingtable;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

@Environment(EnvType.CLIENT)
public class ReverseCraftingTableCMI implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(ReverseCraftingTable.SCREEN_HANDLER, ReverseCraftingTableScreen::new);
    }
}
