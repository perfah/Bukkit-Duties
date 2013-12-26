public package me.th3pf.plugins.duties.events;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ReloadedEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
    
    public HandlerList getHandlers() {
        return handlers;
    }
 
    public static HandlerList getHandlerList() {
        return handlers;
    }
}