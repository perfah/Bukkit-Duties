package me.th3pf.plugins.duties.listeners;

import me.th3pf.plugins.duties.Duties;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener{

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerInteract(PlayerInteractEvent event) {
		
		if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR)
			return;
		
		if( ! ( (event.getClickedBlock().getType() == Material.CHEST) 
			 || (event.getClickedBlock().getType() == Material.TRAPPED_CHEST) 
			 || (event.getClickedBlock().getType() == Material.ENDER_CHEST)
			  )
		  )return;
		
		if(!Duties.Config.GetBoolean("Actions.DenyChestInteracts"))
			return;
		
		if(!Duties.Memories.containsKey(event.getPlayer().getName())) 
			return;
		
		if( (event.getPlayer().hasPermission("duties.bypass.chestinteracts") 
				|| (Duties.Config.GetBoolean("Vault.Permissions") && Duties.VaultAdapter.permission.has(event.getPlayer(), "duties.bypass.chestinteracts"))) )
			return;
			
		
		event.setCancelled(true);
		Duties.latestEventCancelled = true;
	}
	
}
