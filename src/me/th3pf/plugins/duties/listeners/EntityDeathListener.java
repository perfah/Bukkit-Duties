private package me.th3pf.plugins.duties.listeners;

import me.th3pf.plugins.duties.Duties;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeathListener implements Listener
{
	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityDeath(EntityDeathEvent event) 
	{
		if(Duties.Config.GetBoolean("Actions.DisableDeathDrops")
				&& (event.getEntity() instanceof Player)
				&& (Duties.Memories.containsKey( ((Player)event.getEntity()).getName()) ))
		{	
			event.getDrops().clear();
			event.setDroppedExp(0);
		}
		
		if(Duties.Config.GetBoolean("Actions.DisableKillDrops")
				&& (event.getEntity().getKiller() instanceof Player)
				&& (Duties.Memories.containsKey( event.getEntity().getKiller().getName() )))
		{
			event.getDrops().clear();
			event.setDroppedExp(0);
		}
	}
}