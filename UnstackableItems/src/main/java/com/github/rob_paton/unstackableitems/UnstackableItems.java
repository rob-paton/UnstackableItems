package com.github.rob_paton.unstackableitems;

import com.github.rob_paton.unstackableitems.pluginCompatibility.persistentBlocks.OnPersistentBlockPlayerBreak;
import com.github.rob_paton.unstackableitems.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


/*
TODO
    - make crafting result unstackable
    - allow stacking materials in enchantment table and anvil
    - could remove nbt tag on click when in those stations
 */

public final class UnstackableItems extends JavaPlugin {
    public Logic logic;

    @Override
    public void onEnable() {
        this.logic = new Logic(this);
        Bukkit.getPluginManager().registerEvents(new OnItemSpawn(logic), this);
        Bukkit.getPluginManager().registerEvents(new OnCraftItem(logic), this);
        Bukkit.getPluginManager().registerEvents(new OnFurnaceSmelt(logic), this);

        Bukkit.getPluginManager().registerEvents(new OnPersistentBlockPlayerBreak(logic), this);
    }

    @Override
    public void onDisable() {
    }


//    @EventHandler
//    public void onEntityDropItem(EntityDropItemEvent event) {
//        ItemStack itemStack = event.getItemDrop().getItemStack();
//        makeItemUnstackable(itemStack);
//    }

//    @EventHandler
//    public void onBlockDropItem(BlockDropItemEvent event) {
//        List<Item> items = event.getItems();
//
//        int initialSize = items.size();
//        for (int i = 0; i < initialSize; i++) {
//            Item item = items.get(i);
//            ItemStack itemStack = item.getItemStack();
//            while (itemStack.getAmount() > 1) {
//                ItemStack itemStackClone = new ItemStack(itemStack);
//                itemStackClone.setAmount(1);
//
//                World world = item.getWorld();
//                Location location = item.getLocation();
//                items.add(world.dropItemNaturally(location, itemStackClone));
//                itemStack.setAmount(itemStack.getAmount() - 1);
//            }
//        }
//
//        for (Item item : items) {
//            makeItemUnstackable(item.getItemStack());
//        }
//    }


}
