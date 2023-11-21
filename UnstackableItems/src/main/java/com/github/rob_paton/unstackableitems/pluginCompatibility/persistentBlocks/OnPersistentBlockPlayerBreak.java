package com.github.rob_paton.unstackableitems.pluginCompatibility.persistentBlocks;

import com.github.rob_paton.unstackableitems.Logic;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class OnPersistentBlockPlayerBreak implements Listener {
    public Logic logic;
    public OnPersistentBlockPlayerBreak(Logic logic) {
        this.logic = logic;
    }

    // To work with PersistentBlocks: this makes block item unstackable before it's added to player inventory
    @EventHandler(priority = EventPriority.LOW)
    public void onPersistentBlockPlayerBreak(BlockDropItemEvent event) {
        // Ignore Event if Block drops nothing
        List<Item> items = event.getItems();
        if (items.isEmpty()) {
            return;
        }

        ItemStack blockItemStack = items.get(items.size() - 1).getItemStack();
        if (blockItemStack.getAmount() == 1) {
            logic.giveUnstackableId(blockItemStack);
        }
    }
}
