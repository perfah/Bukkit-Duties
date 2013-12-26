package me.th3pf.plugins.duties;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public class Configuration 
{
	public class Main
	{
		private YamlConfiguration config;
		private HashMap<String, Object> configDefaults = new LinkedHashMap<String, Object>();
		
		public LinkedHashMap<String, Object> initializeConfigDefaults()
		{	
			LinkedHashMap<String, Object> output = new LinkedHashMap<String, Object>();
			
			output.put("Enabled", true);
			output.put("KeepStateOffline", false);
			
			output.put("Actions.onEnable.Order", Arrays.asList(new String[]{"MemoryImport","TemporaryGroups", "TemporaryPermissions","Cleanups","CommandsByConsole","Commands","Messages","Broadcast"}));
			output.put("Actions.TemporaryPermissions", Arrays.asList(new String[]{}));
			output.put("Actions.TemporaryGroups", Arrays.asList(new String[]{}));
			output.put("Actions.onEnable.Cleanups", Arrays.asList(new String[]{"Vehicle","Velocity","Inventory","Armor","Exhaustion","Saturation","FoodLevel","Health","Experience","RemaingAir","FallDistance","FireTicks","PotionEffects","TicksLived"}));
			output.put("Actions.onEnable.Messages", new ArrayList<String>());
			output.put("Actions.onEnable.Commands", new ArrayList<String>());
			output.put("Actions.onEnable.CommandsByConsole", Arrays.asList(new String[]{"gamemode 1 %PLAYER_NAME%"}));
			
			output.put("Actions.onDisable.Order", Arrays.asList(new String[]{"MemoryExport","CommandsByConsole","Commands","TemporaryPermissions","TemporaryGroups","DataRemoval","Messages","Broadcast"}));
			output.put("Actions.onDisable.Messages", new ArrayList<String>());
			output.put("Actions.onDisable.Commands", new ArrayList<String>());
			output.put("Actions.onDisable.CommandsByConsole", new ArrayList<String>());
			
			output.put("Actions.DisableDeathDrops", true);
			output.put("Actions.DisableKillDrops", false);
			output.put("Actions.DenyDesiredDrops", true);
			output.put("Actions.DenyChestInteracts", true);
			output.put("Actions.RemindPlayers", true);
			output.put("Actions.Requirements.Dependencies", Arrays.asList(new String[]{"Vault", "TagAPI"}));
			output.put("Actions.NameTagPrefix", "&4!&f");
			output.put("Actions.NameTagSuffix", "&4!&f");
			
			output.put("Vault.Permissions", true);
			output.put("Vault.NameFormatting", false);
			output.put("Vault.Economy", false);

			output.put("PreventTeleportCollision", false);
			output.put("Broadcast-duty-changes", true);
			
			output.put("ReminderCooldown", 2400);
			
			//output.put("Easter-eggs",true);
			
			return output; 
		}
		
		public Main(File configFile)
		{
			this.config = new YamlConfiguration();
			this.configDefaults = initializeConfigDefaults();
			
			if(!configFile.exists())
			{
				for (String key : this.configDefaults.keySet())
				{
					this.config.set(key, this.configDefaults.get(key));
				}
				
				try {
					this.config.save(configFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else
			{
				try {
					this.config.load(configFile);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		public void Reload()
		{
			try 
			{
				this.config.load(new File(Duties.GetInstance().getDataFolder().getAbsolutePath() + File.separator + "config.yml"));
			} 
			catch (Exception e) 
			{
				return;
			} 
		}
		
		public boolean GetBoolean(String key)
		{
			if(!this.configDefaults.containsKey(key)){return false;}
			try
			{
				return this.config.getBoolean(key, (Boolean)this.configDefaults.get(key));
			}
			catch(Exception exception)
			{
				return false;
			}
		}
		public void SetBoolean(String key,Boolean value)
		{
			if(!this.configDefaults.containsKey(key)){return;}
			try
			{
				this.config.set(key, value);
			}
			catch(Exception exception)
			{
				return;
			}
		}
		
		public String GetString(String key)
		{
			if(!this.configDefaults.containsKey(key)){return "";}
			try
			{
				return this.config.getString(key).replaceAll("&", String.valueOf(ChatColor.COLOR_CHAR));
			}
			catch(Exception exception)
			{
				return "";
			}
		}
		public void SetString(String key,String value)
		{
			if(!this.configDefaults.containsKey(key)){return;}
			try
			{
				this.config.set(key, value);
			}
			catch(Exception exception)
			{
				return;
			}
		}
		
		public Integer GetInteger(String key)
		{
			if(!this.configDefaults.containsKey(key)){return 0;}
			try
			{
				return this.config.getInt(key);
			}
			catch(Exception exception)
			{
				return 0;
			}
		}
		public void SetInteger(String key,Integer value)
		{
			if(!this.configDefaults.containsKey(key)){return;}
			try
			{
				this.config.set(key, value);
			}
			catch(Exception exception)
			{
				return;
			}
		}
		
		public double GetDouble(String key)
		{
			if(!this.configDefaults.containsKey(key)){return 0;}
			try
			{
				return this.config.getDouble(key);
			}
			catch(Exception exception)
			{
				return 0;
			}
		}
		public void SetDouble(String key,double value)
		{
			if(!this.configDefaults.containsKey(key)){return;}
			try
			{
				this.config.set(key, value);
			}
			catch(Exception exception)
			{
				return;
			}
		}

		public List<String> GetStringList(String key)
		{
			if(!this.configDefaults.containsKey(key)){return null;}
			try
			{
				List<String>output = new ArrayList<String>();
				
				for(String object : this.config.getStringList(key))
				{
					output.add(object.replaceAll("&", String.valueOf(ChatColor.COLOR_CHAR)));
				}
				
				return output;
			}
			catch(Exception exception)
			{
				return null;
			}
		}
		public void SetStringList(String key,List<String> value)
		{
			if(!this.configDefaults.containsKey(key)){return;}
			try
			{
				this.config.set(key, value);
			}
			catch(Exception exception)
			{
				return;
			}
		}
		
		public YamlConfiguration GetHandle()
		{
			try
			{
				return this.config;
			}
			catch(Exception exception)
			{
				return null;
			}
		}
	 }
	
	public class Messages
	{
		private YamlConfiguration config;
		private HashMap<String, Object> configDefaults = new LinkedHashMap<String, Object>();
		
		public LinkedHashMap<String, Object> initializeConfigDefaults()
		{
			LinkedHashMap<String, Object> output = new LinkedHashMap<String, Object>();
			
			output.put("Client.Tag", "&6" + "[" + "&e&o" + "Duties" + "&6" + "]" + "&f" + " ");
			
			output.put("Client.Enabled", "&a" + "Dutymode enabled! Use /dutymode to disable.");
			output.put("Client.Disabled", "&c" + "Dutymode disabled! Use /dutymode to enable.");
			output.put("Client.Fail.Enable", "&4" + "Dutymode activation failed or interrupted!");
			output.put("Client.Fail.Disable","&4" + "Dutymode deactivation failed or interrupted!");
			output.put("Client.EnabledForOtherPlayer", "&a" + "Dutymode enabled for the requested player!");
			output.put("Client.DisabledForOtherPlayer", "&c" + "Dutymode disabled for the requested player!");
			output.put("Client.Fail.EnableForOtherPlayer", "&4" + "Dutymode activation failed or interrupted for the requested player!");
			output.put("Client.Fail.DisableForOtherPlayer", "&4" + "Dutymode deactivation failed or interrupted for the requested player!");
			output.put("Client.PlayerNotOnline", "&4" + "The requested player is not online!");
			output.put("Client.Purged",  "&c" + "Dutymode statuses purged!");
			output.put("Client.MissingPermission", "&4" + "You don't have permissions to do that!");
			output.put("Client.CommandExtensionNotFound", "&4" + "Couldn't find command extension.");
			output.put("Client.Broadcast.Enabled", "%PLAYER_NAME%" + "&a" + " went on duty.");
			output.put("Client.Broadcast.Disabled","%PLAYER_NAME%" + "&c" + " went off duty.");
			output.put("Client.Reminder.Login", "&9" + "Notice that you joined on duty!");
			output.put("Client.Reminder.ChestOpen", "&9" + "Remember that you have dutymode on!");
			output.put("Client.Reminder.ChestOpenCancelled", "&4" + "You are not allowed to open chests in dutymode!");
			output.put("Client.Reminder.ItemDrop", "&9" + "Remember that you have dutymode on!");
			output.put("Client.Reminder.ItemDropCancelled", "&4" + "You are not allowed to drop items in dutymode!");
			output.put("Client.AlreadyOn","&4" + "Dutymode is already on.");
			output.put("Client.AlreadyOff","&4" + "Dutymode is already off.");
			output.put("Client.ErrorOccured", "&4" + "An error occured while enabling dutymode.");
			output.put("Client.List","&a" + "Staff on duty: " + "&f");
			output.put("Client.ListAll","&a" + "Players on duty: " + "&f");
			output.put("Client.NoStaffOnDuty","&c" + "There is currently no staff on duty.");
			output.put("Client.NoPlayersOnDuty","&c" + "There is currently no players on duty.");
			output.put("Client.BroadcastsShown","&e" + "Now broadcasting status updates!");
			output.put("Client.BroadcastsHidden","&9" + "No longer broadcasting status updates!.");
			output.put("Client.BroadcastsShownForPlayer","&e" + "The requested player is now broadcasting status updates.");
			output.put("Client.BroadcastsHiddenForPlayer","&9" + "The requsted player is no longer broadcasting status updates.");
			output.put("Client.BroadcastsAlreadyShown","&4" + "You are already broadcasting status updates.");
			output.put("Client.BroadcastsAlreadyHidden","&4" + "You are already not broadcasting status updates.");
			output.put("Client.BroadcastsAlreadyShownForPlayer","&4" + "The requested player is already broadcasting status updates.");
			output.put("Client.BroadcastsAlreadyHiddenForPlayer","&4" + "The requested player is already not broadcasting status updates.");
			output.put("Server.Enabled","Dutymode enabled for player %PLAYER_NAME%.");
			output.put("Server.Disabled","Dutymode disabled for player %PLAYER_NAME%.");
			output.put("Server.Fail.Enable","Failed to enable dutymode for player %PLAYER_NAME%.");
			output.put("Server.Fail.Disable","Failed to disable dutymode for player %PLAYER_NAME%.");
			output.put("Server.IngamePlayersOnly","This command is only available for in-game player.");
			
			return output;
		}
		
		public Messages(File configFile)
		{
			this.config = new YamlConfiguration();
			this.configDefaults = initializeConfigDefaults();
			
			if(!configFile.exists())
			{
				for (String key : this.configDefaults.keySet())
				{
					this.config.set(key, this.configDefaults.get(key));
				}
				
				try {
					this.config.save(configFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else
			{
				try {
					this.config.load(configFile);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		public void Reload()
		{
			try 
			{
				this.config.load(new File(Duties.GetInstance().getDataFolder().getAbsolutePath() + File.separator + "messages.yml"));
			} 
			catch (Exception e) 
			{
				return;
			} 
		}
		
		public boolean GetBoolean(String key)
		{
			if(!this.configDefaults.containsKey(key)){return false;}
			try
			{
				return this.config.getBoolean(key, (Boolean)this.configDefaults.get(key));
			}
			catch(Exception exception)
			{
				return false;
			}
		}
		public void SetBoolean(String key,Boolean value)
		{
			if(!this.configDefaults.containsKey(key)){return;}
			try
			{
				this.config.set(key, value);
			}
			catch(Exception exception)
			{
				return;
			}
		}
		
		public String GetString(String key)
		{
			if(!this.configDefaults.containsKey(key)){return "";}
			try
			{
				if(this.config.getString(key).equals(""))
					Duties.GetInstance().LogMessage("Couldn't find the '" + key + "' message. Removing the 'messages.yml' file will fix this.");
				
				return this.config.getString(key).replaceAll("&", String.valueOf(ChatColor.COLOR_CHAR));
			}
			catch(Exception exception)
			{
				return "";
			}
		}
		public void SetBoolean(String key,String value)
		{
			if(!this.configDefaults.containsKey(key)){return;}
			try
			{
				this.config.set(key, value);
			}
			catch(Exception exception)
			{
				return;
			}
		}

		public List<String> GetStringList(String key)
		{
			if(!this.configDefaults.containsKey(key)){return null;}
			try
			{
				return this.config.getStringList(key);
			}
			catch(Exception exception)
			{
				return null;
			}
		}
		public void SetStringList(String key,List<String> value)
		{
			if(!this.configDefaults.containsKey(key)){return;}
			try
			{
				this.config.set(key, value); 
			}
			catch(Exception exception)
			{
				return;
			}	
		}
		
		public YamlConfiguration GetHandle()
		{
			try
			{
				return this.config;
			}
			catch(Exception exception)
			{
				return null;
			}
		}
	}
}
