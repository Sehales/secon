
package net.sehales.secon.cmds;

import static net.sehales.secon.utils.MiscUtils.hasPermission;

import java.util.ArrayList;
import java.util.List;

import net.sehales.secon.SeCon;
import net.sehales.secon.config.LanguageConfig;
import net.sehales.secon.events.SeConReloadEndEvent;
import net.sehales.secon.events.SeConReloadStartEvent;
import net.sehales.secon.utils.mc.ChatUtils;
import net.sehales.secon.utils.string.FontWidthCalculator;

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
        mList.add(FontWidthCalculator.strPadCenter(lang.COMMAND_HELP_HEADER.replace("<command>", "/secon addon"), 5, ' '));
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
        mList.add(lang.HELP_ADDON_MENU);
        ChatUtils.sendFormattedMessage(sender, mList);
    }
    
    void displaySeConHelp(CommandSender sender) {
        List<String> mList = new ArrayList<String>();
        mList.add(FontWidthCalculator.strPadCenter(lang.COMMAND_HELP_HEADER.replace("<command>", "/secon"), 5, ' '));
        mList.add(lang.HELP_SECON_ADDON);
        mList.add(lang.HELP_SECON_RELOAD);
        mList.add(lang.HELP_SECON_COMMANDS);
        mList.add(lang.HELP_SECON_COMMANDMENU);
        ChatUtils.sendFormattedMessage(sender, mList);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
        if (args.length > 0 && hasPermission(sender, "secon.command.secon", true)) {
            
            switch (args[0].toLowerCase()) {
                case "r":
                case "reload": {
                    processReloadCmd(sender, args);
                    break;
                }
                
                case "a":
                case "addon": {
                    processAddonCmd(sender, args);
                    break;
                }
                
                case "c":
                case "commands": {
                    processCommandsCmd(sender, args);
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
        } else if (hasPermission(sender, "secon.command.secon", true)) {
            displaySeConHelp(sender);
        }
        return true;
    }
    
    private void processAddonCmd(CommandSender sender, String[] args) {
        if (hasPermission(sender, "secon.command.secon.addon", true)) {
            
        }
    }
    
    private void processAddonMenuCmd(CommandSender sender, String[] args) {
        if (hasPermission(sender, "secon.command.secon.addon-menu", true)) {
            
        }
    }
    
    private void processCommandMenuCmd(CommandSender sender, String[] args) {
        if (hasPermission(sender, "secon.command.secon.command-menu", true)) {
            
        }
    }
    
    private void processCommandsCmd(CommandSender sender, String[] args) {
        if (hasPermission(sender, "secon.command.secon.commands", true)) {
            
        }
    }
    
    private void processReloadCmd(CommandSender sender, String[] args) {
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
