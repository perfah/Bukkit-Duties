private package me.th3pf.plugins.duties;

import java.util.ArrayList;

import me.th3pf.plugins.duties.events.DutyModeDisabledEvent;
import me.th3pf.plugins.duties.events.DutyModeEnabledEvent;
import me.th3pf.plugins.duties.events.DutyModePreDisabledEvent;
import me.th3pf.plugins.duties.events.DutyModePreEnabledEvent;
//import me.th3pf.plugins.duties.temporaryfixes.PotionEffectRemoval;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;
import org.kitteh.tag.TagAPI;

public class ModeSwitcher
{
	private Player player;
	
	public ModeSwitcher(Player player)
	{
		this.player = player;
	}
	
	public boolean EnableDutyMode()
	{
		try
		{
			DutyModePreEnabledEvent event = new DutyModePreEnabledEvent(this.player);
			Bukkit.getServer().getPluginManager().callEvent(event);
			if(event.getCancelled())return false;
			
			for(String plugin : Duties.Config.GetStringList("Actions.Requirements.Dependencies"))
			{
				try
				{
					if(Duties.GetInstance().pluginManager.getPlugin(plugin).isEnabled())continue;
					Duties.GetInstance().pluginManager.enablePlugin(Duties.GetInstance().pluginManager.getPlugin(plugin));
				}
				catch(Exception exception){}
			}
			
			boolean fail = false;
			
			Modules.onEnable modules = new Modules().new onEnable();
			
			for(String module : Duties.Config.GetStringList("Actions.onEnable.Order"))
			{
				if(module.equalsIgnoreCase("MemoryImport")){if(!modules.MemoryImport()){fail = true;};}
				else if(module.equalsIgnoreCase("TemporaryPermissions")){if(!modules.TemporaryPermissions()){fail = true;};}
				else if(module.equalsIgnoreCase("TemporaryGroups")){if(!modules.TemporaryGroups()){fail = true;};}
				else if(module.equalsIgnoreCase("Cleanups")){if(!modules.Cleanups()){fail = true;};}
				else if(module.equalsIgnoreCase("CommandsByConsole")){if(!modules.CommandsByConsole()){fail = true;};}
				else if(module.equalsIgnoreCase("Commands")){if(!modules.Commands()){fail = true;};}
				else if(module.equalsIgnoreCase("Messages")){if(!modules.Messages()){fail = true;};}
				else if(module.equalsIgnoreCase("Broadcast")){if(!modules.Broadcast()){fail = true;};}
			}

			if(Duties.GetInstance().pluginManager.isPluginEnabled("TagAPI") && this.player.isOnline())
				TagAPI.refreshPlayer(this.player);
			
			//Returns that duty mode activation failed
			if(fail == true)
			{return false;}
			else
			{
				Duties.GetInstance().LogMessage(Duties.Messages.GetString("Server.Enabled").replaceAll("%PLAYER_NAME%",player.getName()));
				
				Bukkit.getServer().getPluginManager().callEvent(new DutyModeEnabledEvent(this.player));
				return true;
			}
		}
		catch(Exception exception)
		{
			Duties.GetInstance().LogMessage(Duties.Messages.GetString("Server.Fail.Enable").replaceAll("%PLAYER_NAME%",player.getName().replaceAll("%REASON%",exception.getMessage())));
			return false;
		}
	}

	public boolean DisableDutyMode()
	{
		try
		{
			DutyModePreDisabledEvent event = new DutyModePreDisabledEvent(this.player);
			Bukkit.getServer().getPluginManager().callEvent(event);
			if(event.getCancelled())return false;
			
			for(String plugin : Duties.Config.GetStringList("Actions.Requirements.Dependencies"))
			{
				try
				{
					if(Duties.GetInstance().pluginManager.getPlugin(plugin).isEnabled())continue;
					Duties.GetInstance().pluginManager.enablePlugin(Duties.GetInstance().pluginManager.getPlugin(plugin));
				}
				catch(Exception exception){}
			}
			
			boolean fail = false;
			
			Modules.onDisable modules = new Modules().new onDisable();
			for(String module : Duties.Config.GetStringList("Actions.onDisable.Order"))
			{
				if(module.equalsIgnoreCase("MemoryExport")){if(!modules.MemoryExport()){fail = true;};}
				else if(module.equalsIgnoreCase("CommandsByConsole")){if(!modules.CommandsByConsole()){fail = true;};}
				else if(module.equalsIgnoreCase("Commands")){if(!modules.Commands()){fail = true;};}
				else if(module.equalsIgnoreCase("TemporaryGroups")){if(!modules.TemporaryGroups()){fail = true;};}
				else if(module.equalsIgnoreCase("TemporaryPermissions")){if(!modules.TemporaryPermissions()){fail = true;};}
				else if(module.equalsIgnoreCase("DataRemoval")){if(!modules.DataRemoval()){fail = true;};}
				else if(module.equalsIgnoreCase("Messages")){if(!modules.Messages()){fail = true;};}
				else if(module.equalsIgnoreCase("Broadcast")){if(!modules.Broadcast()){fail = true;};}
			}
			
			if(Duties.GetInstance().pluginManager.isPluginEnabled("TagAPI") && this.player.isOnline())
				TagAPI.refreshPlayer(this.player);
			
			//Returns that duty mode inactivation failed
			if(fail == true)
			{return false;}
			else
			{
				Duties.GetInstance().LogMessage(Duties.Messages.GetString("Server.Disabled").replaceAll("%PLAYER_NAME%",player.getName()));
				
				Bukkit.getServer().getPluginManager().callEvent(new DutyModeDisabledEvent(this.player));
				return true;
			}
		}
		catch(Exception exception)
		{
			Duties.GetInstance().LogMessage(Duties.Messages.GetString("Server.Fail.Disable").replaceAll("%PLAYER_NAME%",player.getName().replaceAll("%REASON%",exception.getMessage())));
			return false;
		}
	}

	public class Modules
	{
		public class onEnable
		{	
			public boolean Broadcast() 
			{
				//Broadcasts duty change
				try
				{
					if(Duties.Config.GetBoolean("Broadcast-duty-changes") && ModeSwitcher.this.player.hasPermission("duties.broadcast") && !Duties.Hidden.contains(ModeSwitcher.this.player))
					{
						String FormattedName = ModeSwitcher.this.player.getName();
						
						if(Duties.Config.GetBoolean("Vault.NameFormatting") && Duties.GetInstance().getServer().getPluginManager().isPluginEnabled("Vault"))
						{
							FormattedName = (Duties.VaultAdapter.chat.getPlayerPrefix(ModeSwitcher.this.player) + FormattedName + Duties.VaultAdapter.chat.getPlayerSuffix(ModeSwitcher.this.player));
						}
							
						for(Player player : Duties.GetInstance().getServer().getOnlinePlayers())
						{
							if(player != ModeSwitcher.this.player && player.hasPermission("duties.getbroadcasts"))
							player.sendMessage(Duties.Messages.GetString("Client.Broadcast.Enabled").replaceAll("%PLAYER_NAME%", FormattedName));
						}
					}
					
					return true;
				}
				catch(Exception exception)
				{
					Duties.GetInstance().LogMessage("Failed while broadcasting duty mode change: " + exception.getMessage());
					return false;
				}
			}

			public boolean Messages() 
			{
				//Chats onEnable messages
				if(Duties.Config.GetStringList("Actions.onEnable.Messages") == null)return true;
				try
				{
					for(String message : Duties.Config.GetStringList("Actions.onEnable.Messages"))
					{	
						String parsedMessage = ("/".equals(message.charAt(0)) ? message.substring(1) : message)
								.replaceAll("%PLAYER_NAME%", player.getName())
								.replaceAll("%PLAYER_GAMEMODE%", player.getGameMode().toString());
						ModeSwitcher.this.player.sendMessage(parsedMessage);
					}
					
					return true;
				}
				catch(Exception exception)
				{
					Duties.GetInstance().LogMessage("Failed while chatting onEnable messages: " + exception.getMessage());
					return false;
				}
			}

			public boolean Commands() 
			{
				//Performs onEnable commands
				if(Duties.Config.GetStringList("Actions.onEnable.Commands") == null)return true;
				try
				{
					for(String command : Duties.Config.GetStringList("Actions.onEnable.Commands"))
					{	
						String parsedCommand = ("/".equals(command.charAt(0)) ? command.substring(1) : command)
								.replaceAll("%PLAYER_NAME%", player.getName())
								.replaceAll("%PLAYER_GAMEMODE%", player.getGameMode().toString());
						player.performCommand(parsedCommand);
						));
					}
					
					return true;
				}
				catch(Exception exception)
				{
					Duties.GetInstance().LogMessage("Failed while performing onEnable commands: " + exception.getMessage());
					return false;
				}
			}

			public boolean CommandsByConsole() 
			{
				//Performs onEnable console commands
				if(Duties.Config.GetStringList("Actions.onEnable.CommandsByConsole") == null)return true;
				try
				{
					for(String command : Duties.Config.GetStringList("Actions.onEnable.CommandsByConsole"))
					{	
						String parsedCommand = ("/".equals(command.charAt(0)) ? command.substring(1) : command)
								.replaceAll("%PLAYER_NAME%", player.getName())
								.replaceAll("%PLAYER_GAMEMODE%", player.getGameMode().toString());
						Duties.GetInstance().getServer().dispatchCommand(Bukkit.getConsoleSender(), parsedCommand);
					}
					
					return true;
				}
				catch(Exception exception)
				{
					Duties.GetInstance().LogMessage("Failed while performing onEnable console commands: " + exception.getMessage());
					return false;
				}
			}

			public boolean Cleanups() 
			{
				//Cleanups
				if(Duties.Config.GetStringList("Actions.onEnable.Cleanups") == null)return true;
				try
				{
					for(String task : Duties.Config.GetStringList("Actions.onEnable.Cleanups"))
					{
						if(task.equalsIgnoreCase("Location"))
							ModeSwitcher.this.player.teleport(player.getWorld().getSpawnLocation());
						else if(task.equalsIgnoreCase("Vehicle"))
						{
							if(ModeSwitcher.this.player.isInsideVehicle())
							{
								ModeSwitcher.this.player.getVehicle().eject();
							}
						}
						else if(task.equalsIgnoreCase("Velocity"))
							ModeSwitcher.this.player.setVelocity(new Vector(0,0,0));
						else if(task.equalsIgnoreCase("Inventory"))
							ModeSwitcher.this.player.getInventory().clear();
						else if(task.equalsIgnoreCase("Armor"))
						{
							ModeSwitcher.this.player.getInventory().setHelmet(null);
							ModeSwitcher.this.player.getInventory().setChestplate(null);
							ModeSwitcher.this.player.getInventory().setLeggings(null);
							ModeSwitcher.this.player.getInventory().setBoots(null);
						}
						else if(task.equalsIgnoreCase("Saturation"))
							ModeSwitcher.this.player.setSaturation(0);
						else if(task.equalsIgnoreCase("Exhaustion"))
							ModeSwitcher.this.player.setExhaustion(0);
						else if(task.equalsIgnoreCase("FoodLevel"))
							ModeSwitcher.this.player.setFoodLevel(20);
						else if(task.equalsIgnoreCase("Health"))
							ModeSwitcher.this.player.setHealth(20.0);
						else if(task.equalsIgnoreCase("Experience"))
						{
							ModeSwitcher.this.player.setLevel(0);
							ModeSwitcher.this.player.setExp((float)0.0);
						}
						else if(task.equalsIgnoreCase("RemainingAir"))
							ModeSwitcher.this.player.setRemainingAir(20);
						else if(task.equalsIgnoreCase("FallDistance"))
							ModeSwitcher.this.player.setFallDistance(0);
						else if(task.equalsIgnoreCase("FireTicks"))
							ModeSwitcher.this.player.setFireTicks(0);
						else if(task.equalsIgnoreCase("PotionEffects"))
						{
							for(PotionEffect potionEffect : ModeSwitcher.this.player.getActivePotionEffects())
							{
								ModeSwitcher.this.player.removePotionEffect(potionEffect.getType()); 
								
								//Temporary fix for Potion Effect Removal
								//PotionEffectRemoval.removeMobEffect(ModeSwitcher.this.player, potionEffect.getType().getId());
							}
						}
						else if(task.equalsIgnoreCase("BedSpawnLocation"))
							ModeSwitcher.this.player.setBedSpawnLocation(ModeSwitcher.this.player.getWorld().getSpawnLocation());
						else if(task.equalsIgnoreCase("TicksLived"))
							ModeSwitcher.this.player.setTicksLived(1);
					}	
					return true;
				}
				catch(Exception exception)
				{
					Duties.GetInstance().LogMessage("Failed while reading cleanup tasks: " + exception.getMessage());
					return false;
				}
			}

			public boolean TemporaryGroups()
			{
				if(!Duties.Config.GetBoolean("Vault.Permissions") || !Duties.GetInstance().getServer().getPluginManager().isPluginEnabled("Vault"))return true;
				
				try
				{	
					if(Duties.Config.GetStringList("Actions.TemporaryGroups") == null)return true;
					for(String group : Duties.Config.GetStringList("Actions.TemporaryGroups"))
					{
						try
						{
							//Adding group
							if(Duties.GetInstance().getServer().getPluginManager().isPluginEnabled("bPermissions"))
							{
								for(World world : Duties.GetInstance().getServer().getWorlds())
									Duties.VaultAdapter.permission.playerAddGroup(world, ModeSwitcher.this.player.getName(), group.replaceAll("%PLAYER_NAME%", ModeSwitcher.this.player.getName()));
							}
							else if(!Duties.VaultAdapter.permission.playerInGroup((String)null,ModeSwitcher.this.player.getName(), group.replaceAll("%PLAYER_NAME%", ModeSwitcher.this.player.getName())))
								Duties.VaultAdapter.permission.playerAddGroup((String)null, ModeSwitcher.this.player.getName(), group.replaceAll("%PLAYER_NAME%", ModeSwitcher.this.player.getName()));
							else
							{
								//Already in group
								
							}
						}
						catch(Exception exception)
						{
							Duties.GetInstance().LogMessage("Failed while enabling temporary groups: Not a valid group: " + group.replaceAll("%PLAYER_NAME%", ModeSwitcher.this.player.getName()));
							Duties.GetInstance().LogMessage("Error occured: " +  exception.getMessage() + ". Ignoring it!");
							
						}
					}
					
					return true;
				}
				catch(Exception exception)
				{
					Duties.GetInstance().LogMessage("Failed while enabling temporary groups: " + exception.getMessage());
					return false;
				}
			}
			
			public boolean TemporaryPermissions() 
			{
				//Adds temporary permissions
				if(Duties.Config.GetStringList("Actions.TemporaryPermissions") == null)return true;
				
				try
				{	
					Duties.Memories.get(ModeSwitcher.this.player.getName()).TemporaryPermissions = new ArrayList<PermissionAttachment>();		
					
					for(String node : Duties.Config.GetStringList("Actions.TemporaryPermissions"))
					{
						try
						{
							if(Duties.Config.GetBoolean("Vault.Permissions") && Duties.GetInstance().getServer().getPluginManager().isPluginEnabled("Vault"))
							{
								if(node.contains(": "))
								{
									if(node.split(": ")[1].equalsIgnoreCase("true"))
									{
										Duties.VaultAdapter.permission.playerAddTransient(ModeSwitcher.this.player, node.split(": ")[0].replaceAll("%PLAYER_NAME%", ModeSwitcher.this.player.getName()));
									}
									else if(node.split(": ")[1].equalsIgnoreCase("false"))
									{
										Duties.VaultAdapter.permission.playerRemoveTransient(ModeSwitcher.this.player, node.split(": ")[0].replaceAll("%PLAYER_NAME%", ModeSwitcher.this.player.getName()));
									}
									else
									{
										Duties.GetInstance().LogMessage("Failed while enabling temporary permissions: '" + (node.split(": ")[1]) + "' is not a valid value for node: " + node.split((": "))[0] + ". Ignoring it!");
										continue;
									}
								}
								else
								{
									Duties.VaultAdapter.permission.playerAddTransient(ModeSwitcher.this.player, node.replaceAll("%PLAYER_NAME%", ModeSwitcher.this.player.getName()));
								}
							}
							else
							{
								if(!player.isOnline()){return true;}
								else
								{
									Duties.Memories.get(ModeSwitcher.this.player).TemporaryPermissions.add(ModeSwitcher.this.player.addAttachment(Duties.GetInstance(),node.split(": ")[0].replaceAll("%PLAYER_NAME%", ModeSwitcher.this.player.getName()), Boolean.parseBoolean(node.split(": ")[1])));
								}
							}
						}
						catch(Exception exception)
						{
							Duties.GetInstance().LogMessage("Failed while enabling temporary permissions: Not a valid permission node: '" + node.replaceAll("%PLAYER_NAME%", ModeSwitcher.this.player.getName()));
							Duties.GetInstance().LogMessage("Error occured: " +  exception.getMessage() + ". Ignoring it!");
						}
					}
					
					return true;
				}
				catch(Exception exception)
				{
					Duties.GetInstance().LogMessage("Failed while enabling temporary permissions: " + exception.getMessage());
					return false;
				}
			}

			public boolean MemoryImport() 
			{
				//Importing to memory
				try
				{
					Duties.Memories.put(player.getName(), new Memory(player,0));
					return true;
				}
				catch(Exception exception)
				{
					Duties.GetInstance().LogMessage("Failed while importing player data in to memory: " + exception.getMessage());
					return false;
				}
			}
		}
		
		public class onDisable
		{	
			public boolean Broadcast() 
			{
				//Broadcasts duty mode change
				try
				{
					if(Duties.Config.GetBoolean("Broadcast-duty-changes") && ModeSwitcher.this.player.hasPermission("duties.broadcast") && !Duties.Hidden.contains(ModeSwitcher.this.player))
					{
						String FormattedName = ModeSwitcher.this.player.getName();
						
						if(Duties.Config.GetBoolean("Vault.NameFormatting") && Duties.GetInstance().getServer().getPluginManager().isPluginEnabled("Vault"))
						{
							FormattedName = (Duties.VaultAdapter.chat.getPlayerPrefix(ModeSwitcher.this.player) + FormattedName + Duties.VaultAdapter.chat.getPlayerSuffix(ModeSwitcher.this.player));
						}
							
						for(Player player : Duties.GetInstance().getServer().getOnlinePlayers())
						{
							if(player != ModeSwitcher.this.player && player.hasPermission("duties.getbroadcasts"))
							player.sendMessage(Duties.Messages.GetString("Client.Broadcast.Disabled").replaceAll("%PLAYER_NAME%", FormattedName));
						}
					}
					
					return true;
				}
				catch(Exception exception)
				{
					Duties.GetInstance().LogMessage("Failed while broadcasting duty mode change: " + exception.getMessage());
					return false;
				}
			}

			public boolean Messages() 
			{
				//Chats onDisable messages
				if(Duties.Config.GetStringList("Actions.onDisable.Messages") == null)return true;
				try
				{
					for(String message : Duties.Config.GetStringList("Actions.onDisable.Messages"))
					{	
						String parsedMessage = ("/".equals(message.charAt(0)) ? message.substring(1) : message)
								.replaceAll("%PLAYER_NAME%", player.getName())
								.replaceAll("%PLAYER_GAMEMODE%", player.getGameMode().toString());
						ModeSwitcher.this.player.sendMessage(parsedMessage);
					}
					
					return true;
				}
				catch(Exception exception)
				{
					Duties.GetInstance().LogMessage("Failed while chatting onDisable messages: " + exception.getMessage());
					return false;
				}
			}

			public boolean DataRemoval() {
				//Removes player data from memory
				try
				{
					Duties.Memories.remove(ModeSwitcher.this.player.getName());
					return true;
				}
				catch(Exception exception)
				{
					Duties.GetInstance().LogMessage("Failed while removing player data from memory: " + exception.getMessage());
					return false;
				}
			}

			public boolean TemporaryGroups()
			{
				if(!Duties.Config.GetBoolean("Vault.Permissions") || !Duties.GetInstance().getServer().getPluginManager().isPluginEnabled("Vault"))return true;
				
				try
				{	
					if(Duties.Config.GetStringList("Actions.TemporaryGroups") == null)return true;
					for(String group : Duties.Config.GetStringList("Actions.TemporaryGroups"))
					{
						try
						{			
							//Removing group
							if(Duties.GetInstance().getServer().getPluginManager().isPluginEnabled("bPermissions"))
							{
								for(World world : Duties.GetInstance().getServer().getWorlds())
									Duties.VaultAdapter.permission.playerRemoveGroup(world, ModeSwitcher.this.player.getName(), group.replaceAll("%PLAYER_NAME%", ModeSwitcher.this.player.getName()));
							}
							else if(Duties.VaultAdapter.permission.playerInGroup((String)null,ModeSwitcher.this.player.getName(), group.replaceAll("%PLAYER_NAME%", ModeSwitcher.this.player.getName())))
								Duties.VaultAdapter.permission.playerRemoveGroup((String)null, ModeSwitcher.this.player.getName(), group.replaceAll("%PLAYER_NAME%", ModeSwitcher.this.player.getName()));
							else
							{
								//Already not in group
								
							}
						}
						catch(Exception exception)
						{
							Duties.GetInstance().LogMessage("Failed while disabling temporary groups: Not a valid group: " + group.replaceAll("%PLAYER_NAME%", ModeSwitcher.this.player.getName()));
							Duties.GetInstance().LogMessage("Error occured: " +  exception.getMessage() + ". Ignoring it!");
						}
					}
					
					return true;
				}
				catch(Exception exception)
				{
					Duties.GetInstance().LogMessage("Failed while disabling temporary groups: " + exception.getMessage());
					return false;
				}
			}
			
			public boolean TemporaryPermissions() 
			{
				//Removes temporary permissions
				if(Duties.Config.GetStringList("Actions.TemporaryPermissions") == null)return true;
				try
				{
					int count = 0;
					for(String node : Duties.Config.GetStringList("Actions.TemporaryPermissions"))
					{
						try
						{	
							if(Duties.Config.GetBoolean("Vault.Permissions") && Duties.GetInstance().getServer().getPluginManager().isPluginEnabled("Vault"))
							{		
								if(node.contains(": "))
								{
									if(node.split(": ")[1].equalsIgnoreCase("true"))
									{
										Duties.VaultAdapter.permission.playerRemoveTransient(ModeSwitcher.this.player, node.split(": ")[0].replaceAll("%PLAYER_NAME%", ModeSwitcher.this.player.getName()));
									}
									else if(node.split(": ")[1].equalsIgnoreCase("false"))
									{
										Duties.VaultAdapter.permission.playerAddTransient(ModeSwitcher.this.player, node.split(": ")[0].replaceAll("%PLAYER_NAME%", ModeSwitcher.this.player.getName()));
									}
									else
									{
										Duties.GetInstance().LogMessage("Failed while disabling temporary permissions: '" + (node.split(": ")[1]) + "' is not a valid value for node: " + node.split((": "))[0] + ". Ignoring it!");
										continue;
									}
								}
								else
								{
									Duties.VaultAdapter.permission.playerRemoveTransient(ModeSwitcher.this.player, node.replaceAll("%PLAYER_NAME%", ModeSwitcher.this.player.getName()));
								}
							}
							else
							{
								if(!player.isOnline()){return true;}
								else
								{
									ModeSwitcher.this.player.removeAttachment(Duties.Memories.get(player).TemporaryPermissions.get(count));
								}
							}
						}
						catch(Exception exception)
						{
							Duties.GetInstance().LogMessage("Failed while disabling temporary permissions: Not a valid permission node: '" + node.replaceAll("%PLAYER_NAME%", ModeSwitcher.this.player.getName()));
							Duties.GetInstance().LogMessage("Error occured: " +  exception.getMessage() + ". Ignoring it!");
						}
							
						count++;
					}
					
					return true;
				}
				catch(Exception exception)
				{
					Duties.GetInstance().LogMessage("Failed while removing temporary permissions: " + exception.getMessage());
					return false;
				}
			}

			public boolean Commands() 
			{
				//Performs onDisable commands
				if(Duties.Config.GetStringList("Actions.onDisable.Commands") == null)return true;
				try
				{
					for(String command : Duties.Config.GetStringList("Actions.onDisable.Commands"))
					{
						String parsedCommand = ("/".equals(command.charAt(0)) ? command.substring(1) : command)
								.replaceAll("%PLAYER_NAME%", player.getName())
								.replaceAll("%PLAYER_GAMEMODE%", player.getGameMode().toString());
						player.performCommand(parsedCommand);
						));
					}
					
					return true;
				}
				catch(Exception exception)
				{
					Duties.GetInstance().LogMessage("Failed while performing onDisable commands: " + exception.getMessage());
					return false;
				}
			}

			public boolean CommandsByConsole() 
			{
				//Performs onDisable console commands
				if(Duties.Config.GetStringList("Actions.onDisable.CommandsByConsole") == null)return true;
				try
				{
					for(String command : Duties.Config.GetStringList("Actions.onDisable.CommandsByConsole"))
					{	
						String parsedCommand = ("/".equals(command.charAt(0)) ? command.substring(1) : command)
								.replaceAll("%PLAYER_NAME%", player.getName())
								.replaceAll("%PLAYER_GAMEMODE%", player.getGameMode().toString());
						Duties.GetInstance().getServer().dispatchCommand(Bukkit.getConsoleSender(), parsedCommand);
					}
					
					return true;
				}
				catch(Exception exception)
				{
					Duties.GetInstance().LogMessage("Failed while performing onDisable console commands: " + exception.getMessage());
					return false;
				}
			}

			public boolean MemoryExport() 
			{
				//Resets player data from memory
				try
				{	
					if(ModeSwitcher.this.player.isInsideVehicle())ModeSwitcher.this.player.getVehicle().eject();
					
					if(Duties.Config.GetBoolean("PreventTeleportCollision")) //Teleporting in to blocks fix, suggested by @riddle
						Duties.Memories.get(ModeSwitcher.this.player.getName()).Location.setY( Duties.Memories.get(ModeSwitcher.this.player.getName()).Location.getY() +1 );
					
					ModeSwitcher.this.player.teleport(Duties.Memories.get(ModeSwitcher.this.player.getName()).Location);
					if(Duties.Memories.get(ModeSwitcher.this.player.getName()).Vehicle != null){Duties.Memories.get(ModeSwitcher.this.player.getName()).Vehicle.setPassenger(ModeSwitcher.this.player);}
					ModeSwitcher.this.player.setVelocity(Duties.Memories.get(ModeSwitcher.this.player.getName()).Velocity);
					ModeSwitcher.this.player.setGameMode(Duties.Memories.get(ModeSwitcher.this.player.getName()).GameMode);
					ModeSwitcher.this.player.getInventory().clear();
					ModeSwitcher.this.player.getInventory().setContents(Duties.Memories.get(ModeSwitcher.this.player.getName()).Inventory);
					ModeSwitcher.this.player.getInventory().setArmorContents(Duties.Memories.get(ModeSwitcher.this.player.getName()).Armor);
					ModeSwitcher.this.player.setExhaustion(Duties.Memories.get(ModeSwitcher.this.player.getName()).Exhaustion);
					ModeSwitcher.this.player.setSaturation(Duties.Memories.get(ModeSwitcher.this.player.getName()).Saturation);
					ModeSwitcher.this.player.setFoodLevel(Duties.Memories.get(ModeSwitcher.this.player.getName()).FoodLevel);
					ModeSwitcher.this.player.setHealth(Duties.Memories.get(ModeSwitcher.this.player.getName()).Health);
					ModeSwitcher.this.player.setLevel(Duties.Memories.get(ModeSwitcher.this.player.getName()).Level);
					ModeSwitcher.this.player.setExp(Duties.Memories.get(ModeSwitcher.this.player.getName()).Experience);
					ModeSwitcher.this.player.setRemainingAir(Duties.Memories.get(ModeSwitcher.this.player.getName()).RemainingAir);
					ModeSwitcher.this.player.setFallDistance(Duties.Memories.get(ModeSwitcher.this.player.getName()).FallDistance);
					ModeSwitcher.this.player.setFireTicks(Duties.Memories.get(ModeSwitcher.this.player.getName()).FireTicks);
					for(PotionEffect potionEffect : ModeSwitcher.this.player.getActivePotionEffects())
					{
						ModeSwitcher.this.player.removePotionEffect(potionEffect.getType()); 
						
						//PotionEffectRemoval.removeMobEffect(ModeSwitcher.this.player, potionEffect.getType().getId());
					}
					ModeSwitcher.this.player.addPotionEffects(Duties.Memories.get(ModeSwitcher.this.player.getName()).PotionEffects);
					
					//Duties.GetInstance().LogMessage("Reached before bed spawn loc.");
					
					if(Duties.Memories.get(ModeSwitcher.this.player.getName()).BedSpawnLocation != null && ModeSwitcher.this.player.getBedSpawnLocation() != null &&  ! Duties.Memories.get(ModeSwitcher.this.player.getName()).BedSpawnLocation.equals(ModeSwitcher.this.player.getBedSpawnLocation()) )
					{
						//Broken since CB 1.4.7-RX.X
						ModeSwitcher.this.player.setBedSpawnLocation(
								Duties.Memories.get(ModeSwitcher.this.player.getName()).BedSpawnLocation);
					}
					
					ModeSwitcher.this.player.setTicksLived(Duties.Memories.get(ModeSwitcher.this.player.getName()).TicksLived);
					
					return true;
				}
				catch(Exception exception)
				{
					Duties.GetInstance().LogMessage("Failed while reseting player data from memory: " + exception.getMessage());
					return false;
				}
			}
		}
	}
}
