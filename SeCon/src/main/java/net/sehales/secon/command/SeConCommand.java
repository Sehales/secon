
package net.sehales.secon.command;

import java.util.HashMap;
import java.util.Map;

import net.sehales.secon.SeCon;
import net.sehales.secon.utils.chat.ChatUtils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public abstract class SeConCommand extends Command {
    
    private boolean             overrideHelp          = false;
    private Map<String, String> additionalPermissions = new HashMap<>();
    
    public SeConCommand(String name) {
        super(name);
        setDescription(ChatUtils.formatMessage("<red>no description"));
        setUsage(ChatUtils.formatMessage("<red>no usage information"));
    }
    
    public void displayHelp(CommandSender sender) {
        ChatUtils.sendFormattedMessage(sender, SeCon.getInstance().getLang().COMMAND_HELP_HEADER.replace("<command>", getName()));
        ChatUtils.sendFormattedMessage(sender, getDescription());
        ChatUtils.sendFormattedMessage(sender, getUsage());
    }
    
    public void displayNotAllowed(CommandSender sender) {
        ChatUtils.sendFormattedMessage(sender, SeCon.getInstance().getLang().YOU_CANNOT_USE_THIS);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SeConCommand other = (SeConCommand) obj;
        if (additionalPermissions == null) {
            if (other.additionalPermissions != null) {
                return false;
            }
        } else if (!additionalPermissions.equals(other.additionalPermissions)) {
            return false;
        }
        if (overrideHelp != other.overrideHelp) {
            return false;
        }
        return true;
    }
    
    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!testPermissionSilent(sender)) {
            sender.sendMessage(SeCon.getInstance().getLang().NO_PERMISSION.replace("<permission>", getPermission() != null ? getPermission() : "op only"));
            return true;
        }
        
        if (!overrideHelp && args != null && args.length > 0 && (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?"))) {
            displayHelp(sender);
            return true;
        }
        
        execute(sender, args);
        return true;
    }
    
    protected abstract void execute(CommandSender sender, String[] args);
    
    public String getPermission(String name) {
        return additionalPermissions.get(name);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (additionalPermissions == null ? 0 : additionalPermissions.hashCode());
        result = prime * result + (overrideHelp ? 1231 : 1237);
        return result;
    }
    
    public boolean isOverridingHelp() {
        return overrideHelp;
    }
    
    public void setAdditionalPermssion(String name, String permission) {
        additionalPermissions.put(name, permission);
    }
    
    @Override
    public Command setDescription(String description) {
        super.setDescription(ChatUtils.formatMessage(description));
        return this;
    }
    
    public void setOverrideHelp(boolean override) {
        this.overrideHelp = override;
    }
    
    @Override
    public Command setUsage(String usage) {
        super.setUsage(ChatUtils.formatMessage(usage));
        return this;
    }
    
}
