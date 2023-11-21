package com.github.rob_paton.unstackableitems.listeners;

import com.github.rob_paton.unstackableitems.Logic;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class OnCraftItem implements Listener {
    public Logic logic;

    public OnCraftItem(Logic logic) {
        this.logic = logic;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onCraftItem(CraftItemEvent event) {
        if (event.isCancelled()) {
            return;
        }

        CraftingInventory craftingInventory = event.getInventory();
        ItemStack result = craftingInventory.getResult();

        if (logic.isStackable(result.getType())) {
            return;
        }


        Player player = (Player) event.getWhoClicked();
        Inventory playerInventory = player.getInventory();
        int amount = result.getAmount();

        // If result amount is 1, then it just needs an UnstackableId
        if (amount == 1) {
            logic.giveUnstackableId(result);
            return;
        }

        // Get number of empty slots in inventory
        ItemStack[] contents = playerInventory.getStorageContents();
        int emptySlots = 0;
        for (ItemStack itemStack : contents) {
            if (itemStack == null) {
                emptySlots++;
            }
        }

        // Using these booleans to rule out a drop click
        boolean normalClick = false;
        boolean shiftClick = false;

        ClickType click = event.getClick();
        // If click and player cursor is not empty, then craft will not proceed
        if (click == ClickType.LEFT || click == ClickType.RIGHT) {
            if (!event.getCursor().isEmpty()) {
                return;
            }
            normalClick = true;
        }
        // If shift click and player inventory is full, then craft will not proceed
        else if (click == ClickType.SHIFT_LEFT || click == ClickType.SHIFT_RIGHT) {
            if (emptySlots == 0) {
                return;
            }
            shiftClick = true;
        }

        result.setAmount(1);
        logic.giveUnstackableId(result);
        // Decrement amount for the result item
        amount--;

        // Fill player inventory, then drop rest
        while (amount > 0) {
            ItemStack singleItemStack = result.asOne();
            logic.giveUnstackableId(singleItemStack);

            // Add item to inventory until full, or one empty slot if craft was a shift click
            if ((shiftClick && emptySlots > 1) || (normalClick && emptySlots > 0)) {
                playerInventory.addItem(singleItemStack);
                emptySlots--;
            }
            // Drop remaining items
            else {
                Item singleItem = player.getWorld().dropItemNaturally(player.getEyeLocation(), singleItemStack);
                // singleItem.setVelocity(player.getEyeLocation().getDirection().normalize().multiply(0.3));
            }
            amount--;
        }
    }
}
