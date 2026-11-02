package com.echovoid.registry;

import com.echovoid.EchoVoidMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, EchoVoidMod.MODID);

    public static final RegistryObject<CreativeModeTab> ECHO_TAB = TABS.register("echo_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.echovoid"))
                    .icon(() -> new ItemStack(ModItems.ECHO_SHARD.get()))
                    .displayItems((params, output) ->
                            // Every registered item (including block items) shows up automatically.
                            ModItems.ITEMS.getEntries().forEach(item -> output.accept(item.get())))
                    .build());
}
