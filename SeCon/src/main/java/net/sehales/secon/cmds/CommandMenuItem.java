
package net.sehales.secon.cmds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sehales.secon.SeCon;
import net.sehales.secon.itemmenu.MenuItem;
import net.sehales.secon.utils.mc.ChatUtils;

import org.bukkit.command.Command;
import org.bukkit.inventory.ItemStack;

// TODO: complete rework
public class CommandMenuItem extends MenuItem {
    
    private ItemStack    originalIcon;
    private List<String> aliases;
    private List<String> help;
    private String       permission;
    private List<String> additionalPerms;
    private boolean      marked;
    
    public CommandMenuItem(Command cmd) {
        super(null, null, new ArrayList<String>());
        // super(new
        // ItemStack(SeCon.getInstance().getSeConConfig().CMDMENU_CMDITEM_BUKKIT_ITEM_ID,
        // 1, (short)
        // SeCon.getInstance().getSeConConfig().CMDMENU_CMDITEM_BUKKIT_ITEM_DAMAGE);
        
        // ChatUtils.formatMessage(SeCon.getInstance().getLang().CMDMENU_CMDITEM_BUKKIT_LABEL.replace("<command>",
        // cmd.getName())),
        
        Arrays.asList(ChatUtils.formatMessage(SeCon.getInstance().getLang().CMDMENU_CMDITEM_TEXT));
        
        String prefix = SeCon.getInstance().getLang().CMDMENU_DEFAULT_TEXT_PREFIX;
        
        List<String> help = new ArrayList<String>();
        help.add(ChatUtils.formatMessage(prefix + cmd.getDescription()));
        help.add(ChatUtils.formatMessage(prefix + cmd.getUsage()));
        
        List<String> aliases = new ArrayList<String>();
        for (String s : cmd.getAliases()) {
            aliases.add(ChatUtils.formatMessage(prefix + s));
        }
        
        String perm = cmd.getPermission() != null ? ChatUtils.formatMessage(prefix + cmd.getPermission()) : "";
        
        setHelp(help);
        setAliases(aliases);
        setPermission(perm);
        setAdditionalPermissions(new ArrayList<String>());
    }
    
    // public CommandMenuItem(OldSeConCommand cmd) {
    // super(new
    // ItemStack(SeCon.getInstance().getSeConConfig().CMDMENU_CMDITEM_SECON_ITEM_ID,
    // 1, (short)
    // SeCon.getInstance().getSeConConfig().CMDMENU_CMDITEM_SECON_ITEM_DAMAGE),
    //
    // SeCon.getAPI().getChatUtils().formatMessage(LanguageConfig.CMDMENU_CMDITEM_SECON_LABEL.replace("<command>",
    // cmd.getName())),
    //
    // Arrays.asList(SeCon.getAPI().getChatUtils().formatMessage(LanguageConfig.CMDMENU_CMDITEM_TEXT)));
    //
    // String prefix = LanguageConfig.CMDMENU_DEFAULT_TEXT_PREFIX;
    // ChatUtils chat = SeCon.getAPI().getChatUtils();
    //
    // List<String> aliases = new ArrayList<String>();
    // for (String s : cmd.getAliasList()) {
    // aliases.add(ChatUtils.formatMessage(prefix + s));
    // }
    //
    // List<String> help = new ArrayList<String>();
    // for (String s : cmd.getHelp()) {
    // help.add(ChatUtils.formatMessage(prefix + s));
    // }
    //
    // List<String> addPerms = new ArrayList<String>();
    // for (String s : cmd.getPermissionMap().keySet()) {
    // addPerms.add(chat.formatMessage(prefix + s + ": " +
    // cmd.getPermissionMap().get(s)));
    // }
    //
    // String perm = cmd.getPermission() != null ? chat.formatMessage(prefix +
    // cmd.getPermission()) : "";
    //
    // setHelp(help);
    // setAliases(aliases);
    // setPermission(perm);
    // setAdditionalPermissions(addPerms);
    // }
    //
    public List<String> getAdditionalPermissions() {
        return additionalPerms;
    }
    
    public List<String> getAliases() {
        return aliases;
    }
    
    public List<String> getHelp() {
        return help;
    }
    
    public String getPermission() {
        return permission;
    }
    
    public boolean isMarked() {
        return marked;
    }
    
    public void setAdditionalPermissions(List<String> permissions) {
        this.additionalPerms = permissions;
    }
    
    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }
    
    public void setHelp(List<String> help) {
        this.help = help;
    }
    
    public void setMarked(boolean marked) {
        if (marked) {
            originalIcon = getIcon().clone();
            getIcon().setTypeId(SeCon.getInstance().getSeConConfig().CMDMENU_CMDITEM_MARKED_ITEM_ID);
            getIcon().setDurability((short) SeCon.getInstance().getSeConConfig().CMDMENU_CMDITEM_MARKED_ITEM_DAMAGE);
        } else {
            setIcon(originalIcon);
            originalIcon = null;
        }
        this.marked = marked;
    }
    
    public void setPermission(String permission) {
        this.permission = permission;
    }
}
