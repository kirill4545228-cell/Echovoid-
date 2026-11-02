package com.echovoid.registry;

import com.echovoid.EchoVoidMod;
import com.echovoid.item.EchoFlintItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, EchoVoidMod.MODID);

    public static final RegistryObject<Item> ECHO_SHARD =
            ITEMS.register("echo_shard", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ECHO_INGOT =
            ITEMS.register("echo_ingot", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CRYSTAL_CORE =
            ITEMS.register("crystal_core", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));

    public static final RegistryObject<Item> ECHO_FLINT =
            ITEMS.register("echo_flint", () -> new EchoFlintItem(new Item.Properties().stacksTo(1).durability(64)));
}
