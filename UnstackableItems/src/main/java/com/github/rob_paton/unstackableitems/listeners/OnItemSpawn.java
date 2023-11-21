package com.github.rob_paton.unstackableitems.listeners;

import com.github.rob_paton.unstackableitems.Logic;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

public class OnItemSpawn implements Listener {
    public Logic logic;

    public OnItemSpawn(Logic logic) {
        this.logic = logic;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onItemSpawn(ItemSpawnEvent event) {
        Item item = event.getEntity();
        ItemStack itemStack = item.getItemStack();

        // Split item into single item entities
        if (logic.unstackItemEntity(item)) {
            // This event is replaced by the single item spawns
            event.setCancelled(true);
        }
    }
}
