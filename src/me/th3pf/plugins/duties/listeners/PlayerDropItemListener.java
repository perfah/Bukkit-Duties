package me.th3pf.plugins.duties.listeners;

import me.th3pf.plugins.duties.Duties;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItemListener implements Listener{

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerDropItem(PlayerDropItemEvent event) 
	{				
		if(!Duties.Config.GetBoolean("Actions.DenyDesiredDrops"))
			return;
				
		if(!Duties.Memories.containsKey(event.getPlayer().getName())) 
			return;
		
		if(	(event.getPlayer().hasPermission("duties.bypass.dropitems") 
				|| (Duties.Config.GetBoolean("Vault.Permissions") && Duties.VaultAdapter.permission.has(event.getPlayer(), "duties.bypass.dropitems"))) )
			return;
			
		event.setCancelled(true);
	}
	
}
