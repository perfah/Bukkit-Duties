package me.th3pf.plugins.duties;

import me.th3pf.plugins.duties.adapters.VaultAdapter;
import me.th3pf.plugins.duties.commandexecutors.DutiesCommandExecutor;
import me.th3pf.plugins.duties.commandexecutors.DutymodeCommandExecutor;
import me.th3pf.plugins.duties.listeners.EntityDeathListener;
import me.th3pf.plugins.duties.listeners.PlayerDropItemListener;
import me.th3pf.plugins.duties.listeners.PlayerInteractListener;
import me.th3pf.plugins.duties.listeners.PlayerJoinListener;
import me.th3pf.plugins.duties.listeners.PlayerQuitListener;
import me.th3pf.plugins.duties.listeners.RemindListener;
import me.th3pf.plugins.duties.listeners.TagAPIListener;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class Duties extends JavaPlugin 
{
	//Server readpoint
	private static Duties Instance;
	
	//Public source points
	public PluginManager pluginManager;
	public PluginDescriptionFile PDFile;
	public static Configuration.Main Config;
	public static Configuration.Messages Messages;
	public static HashMap<String, Memory> Memories = new HashMap<String, Memory>();
	public static List<Player> Hidden = new ArrayList<Player>();
	public static HashMap<String, Long> LastChestReminderTime = new HashMap<String, Long>();
	public static HashMap<String, Long> LastDropReminderTime = new HashMap<String, Long>();
	public static HashMap<Plugin,String> Addons = new HashMap<Plugin,String>();
	public static VaultAdapter VaultAdapter;
	public static boolean latestEventCancelled = false;
	
	public Duties()
	{
		Instance = this;
	}
	
	@Override
	public void onEnable() 
	{
		pluginManager = this.getServer().getPluginManager();
		PDFile = this.getDescription();
		
		Config = (new Configuration().new Main(new File(Duties.GetInstance().getDataFolder().getAbsolutePath() + File.separator + "config.yml")));
		Messages = (new Configuration().new Messages(new File(Duties.GetInstance().getDataFolder().getAbsolutePath() + File.separator + "messages.yml")));
		
		if(!Config.GetBoolean("Enabled")){pluginManager.disablePlugin(this);}
		
		//Initialize Vault
		if(pluginManager.isPluginEnabled("Vault"))
			VaultAdapter = new VaultAdapter();
		
		getCommand("duties").setExecutor(new DutiesCommandExecutor());
		getCommand("dutymode").setExecutor(new DutymodeCommandExecutor());
		
		pluginManager.registerEvents(new PlayerDropItemListener(), this);
		pluginManager.registerEvents(new PlayerInteractListener(), this);
		pluginManager.registerEvents(new EntityDeathListener(), this);
		pluginManager.registerEvents(new RemindListener(), this);
		
		if(pluginManager.isPluginEnabled("TagAPI")){
			pluginManager.registerEvents(new TagAPIListener(), this);
			Duties.GetInstance().LogMessage("TagAPI hooked.");
		}
		
		if(Config.GetBoolean("KeepStateOffline"))
		{pluginManager.registerEvents(new PlayerJoinListener(), this);}
		else
		{pluginManager.registerEvents(new PlayerQuitListener(), this);}
		
		LogMessage("by " + PDFile.getAuthors().get(0) + " was successfully enabled!");
	}
	@Override
	public void onDisable() 
	{	
		this.getServer().savePlayers();
		
		ArrayList<String>keySet = new ArrayList<String>();
		keySet.addAll(Memories.keySet());
		
		if((Config.GetBoolean("KeepStateOffline")))
		{
			for(String playerName : keySet)
			{
				if(Duties.GetInstance().getServer().getOfflinePlayer(playerName).isOnline())
				{
					if(!new ModeSwitcher(Duties.GetInstance().getServer().getPlayerExact(playerName)).DisableDutyMode())
					{
						LogMessage("Couldn't disable duty mode for " + playerName + ".");
					}
				}
				else
				{
					
					Player player = Memories.get(playerName).Player;
					
					player.loadData();
					if(!new ModeSwitcher(player).DisableDutyMode())
					{
						LogMessage("Dutymode inactivation for " + playerName + " couldn't complete. Sorry for the inconvience.");
					}
					player.saveData();
				}
			}
		}
		else
		{
			for(String playerName : keySet)
			{
				if(!new ModeSwitcher(Duties.GetInstance().getServer().getPlayerExact(playerName)).DisableDutyMode())
				{
					LogMessage("Dutymode inactivation for " + playerName + " couldn't complete. Sorry for the inconvience.");
				}
			}
		}
		
		LogMessage("by " + PDFile.getAuthors().get(0) + " was successfully disabled!");
	}
	
	public static Duties GetInstance()
	{
		return Instance;
	}
	
	public static API GetAPI()
	{
		return new API();
	}
	
	public void LogMessage(String Message)
	{
		System.out.println("[" + PDFile.getName()+ " " + PDFile.getVersion() + "] " + Message);
	}

}
