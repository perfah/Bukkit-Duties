private package me.th3pf.plugins.duties;

import java.util.Collection;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

public class Memory
{
	//The memory where the plugin saves your inventory, location, armor etc...
	public Memory(Player Player,Location Location,Entity Vehicle,Vector Velocity,GameMode GameMode, ItemStack[] Inventory,ItemStack[] Armor,double Health,int FoodLevel, int Level, float Experience, float Saturation,float Exhaustion,int RemainingAir,float FallDistance,int FireTicks,Collection<PotionEffect> PotionEffects,Location BedSpawnLocation,int TicksLived,int ticksOnDuty)
	{
		this.Player = Player;
		
		this.Location = Location;
		this.Vehicle = Vehicle;
		this.Velocity = Velocity;
		this.GameMode = GameMode;
		this.Inventory = Inventory;
		this.Armor = Armor;
		this.Health = Health;
		this.FoodLevel = FoodLevel;
		this.Experience = Experience;
		this.Saturation = Saturation;
		this.Exhaustion = Exhaustion;
		this.RemainingAir = RemainingAir;
		this.FallDistance = FallDistance;
		this.FireTicks = FireTicks;
		this.PotionEffects = PotionEffects;
		this.BedSpawnLocation = BedSpawnLocation;
		this.TicksLived = TicksLived;
		
		this.ticksOnDuty = ticksOnDuty;
	}

	public Memory(Player player, int ticksOnDuty)
	{
		this.Player = player;
		this.Location = player.getLocation();
		this.Vehicle = player.getVehicle();
		this.Velocity = player.getVelocity();
		this.GameMode = player.getGameMode();
		this.Inventory = player.getInventory().getContents();
		this.Armor = player.getInventory().getArmorContents();
		this.Health = player.getHealth();
		this.FoodLevel = player.getFoodLevel();
		this.Level = player.getLevel();
		this.Experience = player.getExp();
		this.Saturation = player.getSaturation();
		this.Exhaustion = player.getExhaustion();
		this.RemainingAir = player.getRemainingAir();
		this.FallDistance = player.getFallDistance();
		this.FireTicks = player.getFireTicks();
		this.PotionEffects = player.getActivePotionEffects();
		this.BedSpawnLocation = player.getBedSpawnLocation();
		this.TicksLived = player.getTicksLived();
		
		this.ticksOnDuty = ticksOnDuty;
	}
	
	//Instance of player needed for shutdown
	public Player Player;
	
	public Location Location;
	public Entity Vehicle;
	public Vector Velocity;
	public GameMode GameMode;
	public ItemStack[] Inventory;
//	public int ItemHeldSlot;
	public ItemStack[] Armor;
	public double Health;
	public int FoodLevel;
	public int Level;
	public float Experience;
	public float Saturation;
	public float Exhaustion;
	public int FireTicks;
	public int RemainingAir;
	public float FallDistance;
	public Collection<PotionEffect> PotionEffects;
	public Location BedSpawnLocation;
	public int TicksLived;
	
	public int ticksOnDuty;
	
	//Features
	public List<PermissionAttachment> TemporaryPermissions;
}
