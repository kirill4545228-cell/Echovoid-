package com.echovoid;

import com.echovoid.registry.ModBlocks;
import com.echovoid.registry.ModCreativeTabs;
import com.echovoid.registry.ModItems;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(EchoVoidMod.MODID)
public class EchoVoidMod {
    public static final String MODID = "echovoid";

    public EchoVoidMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(bus);
        ModItems.ITEMS.register(bus);
        ModCreativeTabs.TABS.register(bus);
    }
}
