package de.timeout.lobby.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackAPI {
	
	private ItemStackAPI() {}

	public static ItemStack createItemStack(Material material, short subid, int amount, String name) {
		ItemStack item = new ItemStack(material, amount, subid);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}
	
	public static void enchantItem(ItemStack item, Enchantment ench, int level, boolean removeEnchantment) {
		ItemMeta meta = item.getItemMeta();
		meta.addEnchant(ench, level, false);
		if(removeEnchantment)meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
	}
}
