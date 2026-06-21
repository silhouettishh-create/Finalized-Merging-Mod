package com.example.commandmerge;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class CommandMergeMod implements ModInitializer {

    public static final String MOD_ID = "commandmerge";

    @Override
    public void onInitialize() {
        ServerTickEvents.END_SERVER_TICK.register(TickScheduler::onEndTick);
    }
}
