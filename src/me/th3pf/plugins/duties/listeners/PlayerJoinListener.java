private package me.th3pf.plugins.duties.listeners;

import me.th3pf.plugins.duties.Duties;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener
{
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) 
	{
		if(!Duties.Memories.containsKey(event.getPlayer())){return;}
		if(!Duties.Config.GetBoolean("Actions.RemindPlayers")){return;}
		if(!event.getPlayer().hasPermission("duties.getreminder.onlogin") && !(Duties.Config.GetBoolean("Vault.Permissions") && Duties.VaultAdapter.permission.has(event.getPlayer(), "duties.getreminder.onlogin"))){return;}
			
		event.getPlayer().sendMessage(Duties.Messages.GetString("Client.Tag") + Duties.Messages.GetString("Client.Reminder.Login"));
	}
}
