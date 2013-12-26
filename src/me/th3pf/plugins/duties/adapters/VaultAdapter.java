private package me.th3pf.plugins.duties.adapters;

import me.th3pf.plugins.duties.Duties;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultAdapter 
{
    public Permission permission = null;
    public Economy economy = null;
    public Chat chat = null;

	public VaultAdapter()
	{	
		if(Duties.Config.GetBoolean("Vault.Permissions") && (!setupPermissions()))
		{
			Duties.Config.SetBoolean("Vault.Permissions", false);
			Duties.GetInstance().saveConfig();
			Duties.GetInstance().LogMessage("Vault didn't hook any permissions plugin, disabled the setting!");
		}
		if(Duties.Config.GetBoolean("Vault.NameFormatting") && (!setupChat()))
		{
			Duties.Config.SetBoolean("Vault.NameFormatting", false);
			Duties.GetInstance().saveConfig();
			Duties.GetInstance().LogMessage("Vault didn't hook any chat plugin, disabled the setting!");
		}
		if(Duties.Config.GetBoolean("Vault.Economy") && (!setupEconomy()))
		{
			Duties.Config.SetBoolean("Vault.Economy", false);
			Duties.GetInstance().saveConfig();
			Duties.GetInstance().LogMessage("Vault didn't hook any economy plugin, disabled the setting!");
		}
	}

    private Boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = Duties.GetInstance().getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            this.permission = permissionProvider.getProvider();
            Duties.GetInstance().LogMessage("Vault permissions hooked.");
        }
        return (this.permission != null);
    }

    private Boolean setupChat()
    {
        RegisteredServiceProvider<Chat> chatProvider = Duties.GetInstance().getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            this.chat = chatProvider.getProvider();
            Duties.GetInstance().LogMessage("Vault chat hooked.");
        }

        return (this.chat != null);
    }

    private Boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = Duties.GetInstance().getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            this.economy = economyProvider.getProvider();
            Duties.GetInstance().LogMessage("Vault economy hooked.");
        }

        return (this.economy != null);
    }
}
