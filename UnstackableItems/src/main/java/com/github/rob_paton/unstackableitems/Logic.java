package com.github.rob_paton.unstackableitems;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class Logic {
    public Plugin plugin;
    public NamespacedKey unstackableKey;

    public static final List<Material> stackableItems = Arrays.asList(
            Material.ARROW,
            Material.SPECTRAL_ARROW,
            Material.TIPPED_ARROW
    );

    public Logic(Plugin plugin) {
        this.plugin = plugin;

        // Custom NBT tag key
        unstackableKey = new NamespacedKey(plugin, "UUID");
    }

    // Return true if item type is allowed to be stacked
    public boolean isStackable(Material material) {
        return stackableItems.contains(material);
    }

    // Gives itemStack a unique NBT tag
    public void giveUnstackableId(ItemStack itemStack) {
        // Generate random UUID
        String uuid = UUID.randomUUID().toString();

        // Set UUID string as custom NBT tag's value
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getPersistentDataContainer().set(unstackableKey, PersistentDataType.STRING, uuid);
        itemStack.setItemMeta(itemMeta);
    }

    public boolean hasUnstackableId(ItemStack itemStack) {
        return itemStack.getItemMeta().getPersistentDataContainer().has(unstackableKey);
    }

    // For each itemStack of amount more than 1, make single itemStackClones of it and add to itemStacks list
//    public void unstackItemsList(List<ItemStack> itemsList) {
//        for (int i = 0; i < itemsList.size(); i++) {
//            ItemStack itemStack = itemsList.get(i);
//            int amount = itemStack.getAmount();
//
//            while (amount > 1) {
//                ItemStack itemStackClone = new ItemStack(itemStack);
//                itemStackClone.setAmount(1);
//                giveUnstackableId(itemStackClone);
//                itemsList.add(i + 1, itemStackClone);
//                amount--;
//                i++;
//            }
//            if (!hasUnstackableId(itemStack)){
//                giveUnstackableId(itemStack);
//            }
//        }
//    }

    /***
     * If item entity has amount over 1, split into single item entities
     * @param item
     * @return true - item was unstacked into single entities; therefore should cancel event to avoid duplication
     * <br>false - item was already single, so just been given an Unstackable ID
     */
    public boolean unstackItemEntity(Item item) {
        ItemStack itemStack = item.getItemStack();
        // Ignore if item is allowed to be stacked, or already unstackable
        if (isStackable(itemStack.getType()) || hasUnstackableId(itemStack)) {
            return false;
        }

        if (itemStack.getAmount() == 1) {
            giveUnstackableId(itemStack);
            return false;
        }

        Location location = item.getLocation();

        List<ItemStack> singleItemStacks = unstackItemStack(itemStack);
        for (ItemStack singleItemStack : singleItemStacks) {
            // Spawn new item entity in same location with the copied itemStack
            location.getWorld().dropItemNaturally(location, singleItemStack);
        }
        return true;
    }

    public List<ItemStack> unstackItemStack(ItemStack itemStack) {
        int amount = itemStack.getAmount();
        List<ItemStack> singleItemStacks = new LinkedList<ItemStack>();

        for (int i = 0; i < amount; i++) {
            // Create copy of item's itemStack, set amount to 1, and make unstackable
            ItemStack itemStackCopy = itemStack.asOne();
            giveUnstackableId(itemStackCopy);

            singleItemStacks.add(itemStackCopy);
        }

        return singleItemStacks;
    }
}
