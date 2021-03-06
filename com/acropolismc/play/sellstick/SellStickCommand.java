package com.acropolismc.play.sellstick;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.acropolismc.play.sellstick.Configs.StickConfig;

public class SellStickCommand implements CommandExecutor {

	private SellStick plugin;

	// Pass instance of main class to 'plugin'
	public SellStickCommand(SellStick plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// Args length is 0
		if (args.length == 0) {
			// For players, show them nothing.
			sender.sendMessage(
					StickConfig.instance.prefix + ChatColor.GRAY + "" + ChatColor.ITALIC + "Sell Stick by shmkane");
			// If the sender has perms to give a stick, show the help message
			if (sender.hasPermission("sellstick.give")) {
				sender.sendMessage(StickConfig.instance.prefix + ChatColor.GREEN + "/SellStick give <player> <amount>");
			}
			return true;

			// Args length is 1
		} else if (args.length == 1) {
			// If the sender has permission to reload
			if (args[0].equalsIgnoreCase("reload") && sender.hasPermission("sellstick.reload")) {
				plugin.getServer().getPluginManager().disablePlugin(plugin);
				sender.sendMessage(ChatColor.RED + "Disabled Plugin");
				plugin.getServer().getPluginManager().enablePlugin(plugin);
				sender.sendMessage(ChatColor.GREEN + "Enabled Plugin");
				return true;
			} else {
				sender.sendMessage(
						StickConfig.instance.prefix + ChatColor.GRAY + "" + ChatColor.ITALIC + "Sell Stick by shmkane");
				return true;
			}
			// Args length is 3
		} else if (args.length == 3) {
			// If they're trying to give a stick...
			if (args[0].equalsIgnoreCase("give")) {
				// And has permission...
				if (sender.hasPermission("sellstick.give")) {
					// Get the intended target
					Player target = plugin.getServer().getPlayer(args[1]);
					// and make sure the target is online/exists
					if (target != null && target.isOnline()) {
						// Give them X number of sticks.
						for (int i = 0; i < Integer.parseInt(args[2]); i++) {
							/**
							 * This assigns a random string to the item meta so
							 * that the item cannot be stacked
							 */
							RandomString random = new RandomString(10);
							String UUID = random.nextString();

							ItemStack is = new ItemStack(Material.getMaterial(StickConfig.instance.item));
							ItemMeta im = is.getItemMeta();
							List<String> lores = new ArrayList<String>();

							im.setDisplayName(StickConfig.instance.itemName);
							lores.add(StickConfig.instance.itemLore);
							// Ignore the uses if "Infinite Uses" is enabled
							if (StickConfig.instance.infiniteUses) {
								lores.add("Infinite Uses");
							} else {
								lores.add(StickConfig.instance.uses + " uses remaining");
							}
							lores.add(ChatColor.MAGIC + UUID);
							im.setLore(lores);
							is.setItemMeta(im);
							// Give the item
							target.getInventory().addItem(is);
						}
						// Send target and sender a message.
						target.sendMessage(
								StickConfig.instance.prefix + "You've been given " + Integer.parseInt(args[2])
										+ " sell " + ((Integer.parseInt(args[2]) > 1) ? "sticks!" : "stick!"));
						sender.sendMessage(StickConfig.instance.prefix + "Given " + target.getName() + " "
								+ Integer.parseInt(args[2]) + " sell "
								+ ((Integer.parseInt(args[2]) > 1) ? "sticks!" : "stick!"));

						return true;

						// If the target wasn't found
					} else {
						sender.sendMessage(ChatColor.RED + "Player not found");
					}
					// If the sender didn't have permission
				} else {
					sender.sendMessage(StickConfig.instance.prefix + StickConfig.instance.noPermission);
				}
			}
			// Invalid number of args. > 3
		} else {
			sender.sendMessage(ChatColor.RED + "Invalid command. Type /Sellstick for help");
		}
		return false;
	}
}