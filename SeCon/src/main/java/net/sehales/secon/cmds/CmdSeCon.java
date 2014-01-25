
package net.sehales.secon.cmds;

import static net.sehales.secon.utils.MiscUtils.hasPermission;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.sehales.secon.SeCon;
import net.sehales.secon.addon.Addon;
import net.sehales.secon.addon.AddonManager;
import net.sehales.secon.command.SeConCommand;
import net.sehales.secon.config.LanguageConfig;
import net.sehales.secon.events.SeConReloadEndEvent;
import net.sehales.secon.events.SeConReloadStartEvent;
import net.sehales.secon.utils.mc.ChatUtils;
import net.sehales.secon.utils.mc.TextUtils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class CmdSeCon implements CommandExecutor {
    
    private SeCon          secon;
    private LanguageConfig lang;
    
    public CmdSeCon(SeCon secon) {
        this.secon = secon;
        this.lang = secon.getLang();
    }
    
    void displayAddonHelp(CommandSender sender) {
        List<String> mList = new ArrayList<String>();
        mList.add(TextUtils.strPadCenter(lang.COMMAND_HELP_HEADER.replace("<command>", "/secon addon"), 5, ' '));
        mList.add(lang.HELP_ADDON_ENABLE);
        mList.add(lang.HELP_ADDON_DISABLE);
        mList.add(lang.HELP_ADDON_SOFT_RELOAD);
        mList.add(lang.HELP_ADDON_SOFT_RELOAD_ALL);
        mList.add(lang.HELP_ADDON_LOAD);
        mList.add(lang.HELP_ADDON_LOAD_ALL);
        mList.add(lang.HELP_ADDON_UNLOAD);
        mList.add(lang.HELP_ADDON_UNLOAD_ALL);
        mList.add(lang.HELP_ADDON_RELOAD);
        mList.add(lang.HELP_ADDON_RELOAD_ALL);
        mList.add(lang.HELP_ADDON_LIST);
        // mList.add(lang.HELP_ADDON_MENU);
        ChatUtils.sendFormattedMessage(sender, mList);
    }
    
    void displaySeConHelp(CommandSender sender) {
        List<String> mList = new ArrayList<String>();
        mList.add(TextUtils.strPadCenter(lang.COMMAND_HELP_HEADER.replace("<command>", "/secon"), 5, ' '));
        mList.add(lang.HELP_SECON_ADDON);
        mList.add(lang.HELP_SECON_RELOAD);
        mList.add(lang.HELP_SECON_COMMANDS);
        // mList.add(lang.HELP_SECON_COMMANDMENU);
        ChatUtils.sendFormattedMessage(sender, mList);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (hasPermission(sender, "secon.command.secon", true)) {
            if (args.length > 0) {
                
                if (secon.isDebugEnabled()) {
                    System.out.println("CmdSeCon.onCommand() args: " + Arrays.toString(args));
                }
                switch (args[0].toLowerCase()) {
                    case "r":
                    case "reload": {
                        processReloadCmd(sender);
                        break;
                    }
                    
                    case "a":
                    case "addon": {
                        processAddonCmd(sender, args);
                        break;
                    }
                    
                    case "c":
                    case "commands": {
                        processCommandsCmd(sender);
                        break;
                    }
                    
                    case "cm":
                    case "commandmenu": {
                        processCommandMenuCmd(sender, args);
                        break;
                    }
                    
                    default: {
                        displaySeConHelp(sender);
                        break;
                    }
                }
            } else {
                displaySeConHelp(sender);
            }
        }
        return true;
    }
    
    private void processAddonCmd(CommandSender sender, String[] args) {
        if (hasPermission(sender, "secon.command.secon.addon", true)) {
            AddonManager am = secon.getAddonManager();
            if (args.length > 2) {
                switch (args[1].toLowerCase()) {
                    case "e":
                    case "enable": {
                        am.enableAddon(sender, args[2]);
                        return;
                    }
                    
                    case "d":
                    case "disable": {
                        am.disableAddon(sender, args[2]);
                        return;
                    }
                    
                    case "sr":
                    case "softreload": {
                        am.disableAddon(sender, args[2]);
                        am.enableAddon(sender, args[2]);
                        return;
                    }
                    
                    case "r":
                    case "reload": {
                        Addon a = am.getAddon(args[2]);
                        File f = a.getRawFile();
                        if (am.unloadAddon(sender, a)) {
                            am.loadAddon(sender, f);
                        }
                        return;
                    }
                    
                    case "l":
                    case "load": {
                        Addon a = am.loadAddon(sender, new File(SeCon.addonFolder, args[2]));
                        am.enableAddon(sender, a);
                        return;
                    }
                    
                    case "u":
                    case "unload": {
                        am.unloadAddon(sender, args[2]);
                        return;
                    }
                }
            } else if (args.length > 1) {
                switch (args[1].toLowerCase()) {
                    case "ra":
                    case "reloadall": {
                        am.unloadAddons(sender);
                        am.loadAddons(sender);
                        return;
                    }
                    
                    case "li":
                    case "list": {
                        StringBuilder enabled = new StringBuilder();
                        StringBuilder disabled = new StringBuilder();
                        for (Addon a : am.getAddons()) {
                            if (a.isEnabled()) {
                                enabled.append("<darkaqua>").append(a.getName()).append("<grey>, ");
                            } else {
                                disabled.append("<darkaqua>").append(a.getName()).append("<grey>, ");
                            }
                        }
                        ChatUtils.sendFormattedMessage(sender, "<green>Enabled: " + (enabled.length() > 0 ? enabled.substring(0, enabled.lastIndexOf("<")) : "<darkaqua>-----"));
                        ChatUtils.sendFormattedMessage(sender, "<red>Disabled: " + (disabled.length() > 0 ? disabled.substring(0, disabled.lastIndexOf("<")) : "<darkaqua>-----"));
                        
                        return;
                    }
                    
                    case "la":
                    case "loadall": {
                        am.loadAddons(sender);
                        return;
                    }
                    
                    case "ua":
                    case "unloadall": {
                        am.unloadAddons(sender);
                        return;
                    }
                }
            }
            displayAddonHelp(sender);
            
        }
    }
    
    // private void processAddonMenuCmd(CommandSender sender, String[] args) {
    // if (hasPermission(sender, "secon.command.secon.addon-menu", true)) {
    //
    // }
    // }
    
    private void processCommandMenuCmd(CommandSender sender, String[] args) {
        if (hasPermission(sender, "secon.command.secon.command-menu", true)) {
            sender.sendMessage("Sorry, the command menu is currently not available. :(");
        }
    }
    
    private void processCommandsCmd(CommandSender sender) {
        if (hasPermission(sender, "secon.command.secon.commands", true)) {
            StringBuilder sb = new StringBuilder();
            
            for (Map.Entry<String, Command> entry : secon.getCommandManager().getCommandMap().entrySet()) {
                if (entry.getValue() instanceof SeConCommand) {
                    sb.append(entry.getKey()).append("<grey>,<darkaqua>");
                }
            }
            
            ChatUtils.sendFormattedMessage(sender, "<aqua>Commands: <darkaqua>" + (sb.length() > 0 ? sb.substring(0, sb.lastIndexOf(",")) : "-----"));
        }
    }
    
    private void processReloadCmd(CommandSender sender) {
        if (hasPermission(sender, "secon.command.secon.reload", true)) {
            sendMessage(sender, lang.RELOAD_STARTED);
            secon.getServer().getPluginManager().callEvent(new SeConReloadStartEvent());
            secon.getSeConConfig().loadAndInform();
            secon.getSeConConfig().initValues();
            
            secon.getLang().loadAndInform();
            secon.getLang().initValues();
            
            secon.getCmdConfig().loadAndInform();
            secon.getItemConfig().loadAndInform();
            
            secon.getCommandManager().unregisterScriptCommands();
            secon.getCommandManager().loadScriptCommands(SeCon.scriptFolder);
            secon.getServer().getPluginManager().callEvent(new SeConReloadEndEvent());
            sendMessage(sender, lang.RELOAD_FINISHED);
        }
    }
    
    private void sendMessage(CommandSender sender, String message) {
        String msg = ChatUtils.formatMessage(message);
        if (!(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage(msg);
        }
        Bukkit.getConsoleSender().sendMessage(msg);
    }
}
