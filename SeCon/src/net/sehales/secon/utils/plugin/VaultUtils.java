
package net.sehales.secon.utils.plugin;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultUtils {
    
    private Chat       chat;
    private Permission perm;
    private Economy    econ;
    
    VaultUtils() {
        setupChat();
        setupPermissions();
        setupEconomy();
    }
    
    public boolean bankDeposit(String name, double amount) {
        if (econ == null) {
            return false;
        }
        EconomyResponse res = econ.bankDeposit(name, amount);
        return res.transactionSuccess();
    }
    
    public boolean bankHas(String name, double amount) {
        if (econ == null) {
            return false;
        }
        EconomyResponse res = econ.bankHas(name, amount);
        return res.transactionSuccess();
    }
    
    public boolean bankWithdraw(String name, double amount) {
        if (econ == null) {
            return false;
        }
        EconomyResponse res = econ.bankWithdraw(name, amount);
        return res.transactionSuccess();
    }
    
    public double getBankBalance(String name) {
        if (econ == null) {
            return -1.0D;
        }
        EconomyResponse res = econ.bankBalance(name);
        
        return res.transactionSuccess() ? res.balance : 0.0D;
    }
    
    public Chat getChatImplementation() {
        return chat;
    }
    
    public Economy getEconomyImplementation() {
        return econ;
    }
    
    public Permission getPermissionImplementation() {
        return perm;
    }
    
    public double getPlayerBalance(String name) {
        if (econ == null) {
            return 0.0D;
        }
        return econ.getBalance(name);
    }
    
    public String getPlayerPrefix(Player p) {
        return getPlayerPrefix(p.getWorld().getName(), p.getName());
    }
    
    public String getPlayerPrefix(String world, String playerName) {
        if (chat == null) {
            return "";
        }
        return chat.getPlayerPrefix(world, playerName);
    }
    
    public String getPlayerSuffix(Player p) {
        return getPlayerSuffix(p.getWorld().getName(), p.getName());
    }
    
    public String getPlayerSuffix(String world, String playerName) {
        if (chat == null) {
            return "";
        }
        return chat.getPlayerSuffix(world, playerName);
    }
    
    public boolean hasBankSupport() {
        return econ.hasBankSupport();
    }
    
    public boolean isChatAvailable() {
        return chat != null;
    }
    
    public boolean isEnonomyAvailable() {
        return econ != null;
    }
    
    public boolean isPermissionAvailable() {
        return perm != null;
    }
    
    public boolean playerDeposit(String playerName, double amount) {
        if (econ == null) {
            return false;
        }
        EconomyResponse res = econ.depositPlayer(playerName, amount);
        return res.transactionSuccess();
    }
    
    public boolean playerHas(String playerName, double amount) {
        if (econ == null) {
            return false;
        }
        return econ.has(playerName, amount);
    }
    
    public boolean playerWithdraw(String playerName, double amount) {
        if (econ == null) {
            return false;
        }
        EconomyResponse res = econ.withdrawPlayer(playerName, amount);
        return res.transactionSuccess();
    }
    
    private void setupChat() {
        RegisteredServiceProvider<Chat> rsp = Bukkit.getServicesManager().getRegistration(Chat.class);
        if (rsp != null) {
            chat = rsp.getProvider();
        }
        
    }
    
    private void setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (rsp != null) {
            econ = rsp.getProvider();
        }
    }
    
    private void setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = Bukkit.getServicesManager().getRegistration(Permission.class);
        if (rsp != null) {
            perm = rsp.getProvider();
        }
        
    }
}
