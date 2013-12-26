package me.th3pf.plugins.duties.commandexecutors;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

import me.th3pf.plugins.duties.Configuration;
import me.th3pf.plugins.duties.Duties;
import me.th3pf.plugins.duties.adapters.VaultAdapter;
import me.th3pf.plugins.duties.events.ReloadedEvent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class DutiesCommandExecutor implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) 
	{
		if(args.length == 0)
		{
			if(!sender.hasPermission("duties.help") && !(Duties.Config.GetBoolean("Vault.Permissions") && Duties.VaultAdapter.permission.has(sender, "duties.help"))){return true;}
			
			sender.sendMessage(ChatColor.BLUE + "----------------------" + ChatColor.GOLD + "[" + ChatColor.YELLOW + "Duties" + ChatColor.GOLD + "]" + ChatColor.BLUE +  "-----------" + ChatColor.YELLOW + "[Page: " + "1" +"]" + ChatColor.BLUE + "------");
		    
			sender.sendMessage(ChatColor.GREEN + "/dutymode");
		    sender.sendMessage("    Toggles the duty mode for yourself");
		    sender.sendMessage(ChatColor.GREEN + "/dutymode toggle [Player]");
		    sender.sendMessage("    Toggles the duty mode for yourself [or other player]");
		    sender.sendMessage(ChatColor.GREEN + "/dutymode enable [Player]");
		    sender.sendMessage("    Enables the duty mode for yourself [or other player]");
		    sender.sendMessage(ChatColor.GREEN + "/dutymode disable [Player]");
		    sender.sendMessage("    Disables the duty mode for yourself [or other player]");
		    sender.sendMessage(ChatColor.GREEN + "/dutymode list");
		    sender.sendMessage("    Shows a list of the staff players that have duty mode on");
		    sender.sendMessage(ChatColor.GREEN + "/dutymode listall");
		    sender.sendMessage("    Shows a list of all the players that have duty mode on");
		    sender.sendMessage(ChatColor.YELLOW + "/duties help");
		    sender.sendMessage("    Shows the help for the plugin");
		    sender.sendMessage(ChatColor.RED + "/duties reload");
		    sender.sendMessage("    Reloads the plugin");
			sender.sendMessage(ChatColor.RED + "/duties disable");
		    sender.sendMessage("    Disables the plugin");
		    
		    sender.sendMessage(ChatColor.BLUE + "-----------------------------------------------------");
		    
		    return true;
		}
		else if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?"))
		{
			if(!sender.hasPermission("duties.help") && !(Duties.Config.GetBoolean("Vault.Permissions") && Duties.VaultAdapter.permission.has(sender, "duties.help"))){return true;}
			
			if(args.length == 1)
			{sender.sendMessage(ChatColor.BLUE + "----------------------" + ChatColor.GOLD + "[" + ChatColor.YELLOW + "Duties" + ChatColor.GOLD + "]" + ChatColor.BLUE +  "-----------" + ChatColor.YELLOW + "[Page: " + "1" +"]" + ChatColor.BLUE + "------");}
			else
			{sender.sendMessage(ChatColor.BLUE + "----------------------" + ChatColor.GOLD + "[" + ChatColor.YELLOW + "Duties" + ChatColor.GOLD + "]" + ChatColor.BLUE +  "-----------" + ChatColor.YELLOW + "[Page: " + args[1] +"]" + ChatColor.BLUE + "------");}
			
			if(args.length == 1 || args[1].equalsIgnoreCase("0") || args[1].equalsIgnoreCase("1"))
			{
				sender.sendMessage(ChatColor.GREEN + "/dutymode");
			    sender.sendMessage("    Toggles the duty mode for yourself");
			    sender.sendMessage(ChatColor.GREEN + "/dutymode toggle [Player]");
			    sender.sendMessage("    Toggles the duty mode for yourself [or other player]");
			    sender.sendMessage(ChatColor.GREEN + "/dutymode enable [Player]");
			    sender.sendMessage("    Enables the duty mode for yourself [or other player]");
			    sender.sendMessage(ChatColor.GREEN + "/dutymode disable [Player]");
			    sender.sendMessage("    Disables the duty mode for yourself [or other player]");
			    sender.sendMessage(ChatColor.GREEN + "/dutymode list");
			    sender.sendMessage("    Shows a list of the staff players that have duty mode on");
			    sender.sendMessage(ChatColor.GREEN + "/dutymode listall");
			    sender.sendMessage("    Shows a list of all the players that have duty mode on");
			    sender.sendMessage(ChatColor.YELLOW + "/duties help");
			    sender.sendMessage("    Shows the help for the plugin");
			    sender.sendMessage(ChatColor.RED + "/duties reload");
			    sender.sendMessage("    Reloads the plugin");
				sender.sendMessage(ChatColor.RED + "/duties disable");
			    sender.sendMessage("    Disables the plugin");
			}
			else if(args[1].equalsIgnoreCase("2"))
			{
			    sender.sendMessage(ChatColor.RED + "/duties purge");
			    sender.sendMessage("    Forces every player off duty mode");
				sender.sendMessage(ChatColor.GREEN + "/hidebroadcast [Player]");
				sender.sendMessage("    Disables duty mode changes broadcasting");
				sender.sendMessage(ChatColor.GREEN + "/hidebroadcast [Player]");
				sender.sendMessage("    Disables duty mode changes broadcasting");
			    sender.sendMessage(ChatColor.YELLOW + "/duties updateconfig");
			    sender.sendMessage("    Updates the config file to include all config options");
			}

			sender.sendMessage(ChatColor.BLUE + "-----------------------------------------------------");
		
		    return true;
		}
		else if(args[0].equalsIgnoreCase("reload"))
		{
			if(!sender.hasPermission("duties.reload") && !(Duties.Config.GetBoolean("Vault.Permissions") && Duties.VaultAdapter.permission.has(sender, "duties.reload"))){TellSender(sender,updates.MissingPermission,false); return true;}
			
			Duties.GetInstance().LogMessage("The 'KeepStateOffline' setting requires server restart to be changed.");
			
			if(!new File(Duties.GetInstance().getDataFolder().getAbsolutePath() + File.separator + "config.yml").exists())
			{
				Configuration.Main config = (new Configuration().new Main(new File(Duties.GetInstance().getDataFolder().getAbsolutePath() + File.separator + "config.yml")));
				config.Reload();
			}
			if(!new File(Duties.GetInstance().getDataFolder().getAbsolutePath() + File.separator + "messages.yml").exists())
			{
				Configuration.Messages messages = (new Configuration().new Messages(new File(Duties.GetInstance().getDataFolder().getAbsolutePath() + File.separator + "messages.yml")));
				messages.Reload();
			}
			
			Duties.GetInstance().reloadConfig();
			Duties.Config.Reload();
			Duties.Messages.Reload();
			
			Duties.VaultAdapter = new VaultAdapter();
			
			Bukkit.getServer().getPluginManager().callEvent(new ReloadedEvent());
			
			if (sender instanceof Player) 
				TellSender(sender,"Configuration reloaded!");
			
			Duties.GetInstance().LogMessage("Configuration reloaded!");
			
			return true;
		}
		else if(args[0].equalsIgnoreCase("disable"))
		{
			if(!sender.hasPermission("duties.disable") && !(Duties.Config.GetBoolean("Vault.Permissions") && Duties.VaultAdapter.permission.has(sender, "duties.disable"))){TellSender(sender,updates.MissingPermission,false); return true;}
			
			Duties.GetInstance().pluginManager.disablePlugin(Duties.GetInstance());
			
			return true;
		}
		else if(args[0].equalsIgnoreCase("updateconfig"))
		{
			if(!sender.hasPermission("duties.updateconfig") && !(Duties.Config.GetBoolean("Vault.Permissions") && Duties.VaultAdapter.permission.has(sender, "duties.updateconfig"))){TellSender(sender,updates.MissingPermission,false); return true;}
			
			Duties.Config.Reload();
			Duties.Messages.Reload();
			
			LinkedHashMap<String, Object> configDefaults = Duties.Config.initializeConfigDefaults();
			
			for(String key : configDefaults.keySet())
			{
				if(!Duties.Config.GetHandle().contains(key))
				{	
					Duties.GetInstance().LogMessage("Adding: '" + key + "' to 'config.yml'");
					Duties.Config.GetHandle().set(key, configDefaults.get(key));
				}
			}
			configDefaults.clear();
			configDefaults = Duties.Messages.initializeConfigDefaults();
			
			for(String key : configDefaults.keySet())
			{
				if(!Duties.Messages.GetHandle().contains(key))
				{ 	
					Duties.GetInstance().LogMessage("Adding: '" + key + "' to 'messages.yml'");
					Duties.Messages.GetHandle().set(key, configDefaults.get(key));
				}
			}
			
			try {
				Duties.Config.GetHandle().save(new File(Duties.GetInstance().getDataFolder().getAbsolutePath() + File.separator + "config.yml"));
				Duties.Messages.GetHandle().save(new File(Duties.GetInstance().getDataFolder().getAbsolutePath() + File.separator + "messages.yml"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Duties.GetInstance().LogMessage("Configuration reloaded & updated!");
			return true;
		}
		else
		{
			TellSender(sender,updates.CommandExtensionNotFound,false);
			return true;
		}
	}
	
	public enum updates{MissingPermission,CommandExtensionNotFound}
	private void TellSender(CommandSender sender, updates update, boolean success)
	{
		if(update == updates.MissingPermission)
		{
			sender.sendMessage(Duties.Messages.GetString("Client.Tag") + Duties.Messages.GetString("Client.MissingPermission"));
		}
		if(update == updates.CommandExtensionNotFound)
		{
			sender.sendMessage(Duties.Messages.GetString("Client.Tag") + Duties.Messages.GetString("Client.CommandExtensionNotFound"));
		}
	}
	private void TellSender(CommandSender sender, String message)
	{
		sender.sendMessage(Duties.Messages.GetString("Client.Tag") + message);
	}
}
