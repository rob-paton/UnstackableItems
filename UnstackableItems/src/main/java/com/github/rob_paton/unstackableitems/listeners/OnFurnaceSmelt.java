package com.github.rob_paton.unstackableitems.listeners;

import com.github.rob_paton.unstackableitems.Logic;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;

public class OnFurnaceSmelt implements Listener {
    public Logic logic;

    public OnFurnaceSmelt(Logic logic) {
        this.logic = logic;
    }

    // TODO - furnace continues to use up fuel
    @EventHandler
    public void onCraft(FurnaceSmeltEvent event) {
        ItemStack result = event.getResult();
        logic.giveUnstackableId(result);
    }
}
