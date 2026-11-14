package com.example.lanoffline;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.network.NetworkConstants;

@Mod(OfflineLan.MODID)
public class OfflineLan {
    public static final String MODID = "lanoffline";

    public OfflineLan() {
        ModLoadingContext ctx = ModLoadingContext.get();
        // Host-side only: clients without this mod can still connect.
        ctx.registerExtensionPoint(IExtensionPoint.DisplayTest.class,
                () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (remote, isServer) -> true));
        ctx.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        MinecraftForge.EVENT_BUS.register(this);
    }

    // The integrated (singleplayer/LAN) server switches authentication on during startup.
    // Switch it back off once startup is done so offline-mode players can log in.
    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        MinecraftServer server = event.getServer();
        if (!server.isDedicatedServer() && Config.allowOffline()) {
            server.setUsesAuthentication(false);
        }
    }
}
