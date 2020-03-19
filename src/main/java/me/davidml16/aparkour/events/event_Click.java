package me.davidml16.aparkour.events;

import me.davidml16.aparkour.api.events.ParkourReturnEvent;
import me.davidml16.aparkour.data.Parkour;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.davidml16.aparkour.Main;
import me.davidml16.aparkour.utils.ItemUtil;
import me.davidml16.aparkour.utils.SoundUtil;

public class event_Click implements Listener {

	@EventHandler
	public void onClicker(PlayerInteractEvent e) {
		Player p = e.getPlayer();

		if (Main.getInstance().getTimerManager().hasPlayerTimer(p)) {

			ItemStack item = p.getItemInHand();

			String Return = Main.getInstance().getLanguageHandler().getMessage("MESSAGES_RETURN", false);

			if (item != null && item.equals(ItemUtil.getRestartItem())) {
				if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
					e.setCancelled(true);

					Parkour parkour = Main.getInstance().getParkourHandler().getParkourByPlayer(p);
					parkour.getPlayers().remove(p.getUniqueId());

					Main.getInstance().getTimerManager().cancelTimer(p);

					p.sendMessage(Return);

					p.setFlying(false);
					p.teleport(parkour.getSpawn());

					if (Main.getInstance().getConfig().getBoolean("RestartItem.Enabled")) {
						Main.getInstance().getPlayerDataHandler().restorePlayerInventory(p);
					}

					SoundUtil.playReturn(p);

                    Bukkit.getPluginManager().callEvent(new ParkourReturnEvent(p, parkour));

					p.setNoDamageTicks(20);
					return;
				}
				return;
			}
			return;
		}
	}
}