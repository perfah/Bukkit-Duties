package me.th3pf.plugins.duties.listeners;

import me.th3pf.plugins.duties.Duties;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kitteh.tag.PlayerReceiveNameTagEvent;

public class TagAPIListener implements Listener{
	
	@EventHandler(ignoreCancelled = true)
    public void onNameTag(PlayerReceiveNameTagEvent event)
    {
        if(Duties.Memories.containsKey( event.getNamedPlayer().getName() ))
        {
            event.setTag(
            		  Duties.Config.GetString("Actions.NameTagPrefix") 
            		+ event.getNamedPlayer().getName() 
            		+ Duties.Config.GetString("Actions.NameTagSuffix"));
        }
    }
}
