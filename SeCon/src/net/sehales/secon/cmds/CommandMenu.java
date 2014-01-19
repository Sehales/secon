
package net.sehales.secon.cmds;

import java.util.Arrays;

import net.sehales.secon.SeCon;
import net.sehales.secon.itemmenu.MenuItem;
import net.sehales.secon.itemmenu.OptionClickEvent;
import net.sehales.secon.itemmenu.PagedMenu;
import net.sehales.secon.utils.mc.ChatUtils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class CommandMenu extends PagedMenu {
    
    public static void openMenu(Player player) {
        PagedMenu menu = new CommandMenu(ChatUtils.formatMessage(SeCon.getInstance().getLang().CMDMENU_NAME.replace("<player>", player.getName())), SeCon.getInstance(), player);
        // menu.init(CommandMenuHelper.createCommandList(player));
        menu.open(player);
    }
    
    private Player          player;
    
    // why the hell shall that be a magic value? I can tell you all items with
    // their corresponding item ids, that's not magic... it is called having a
    // brain....
    @SuppressWarnings("deprecation")
    private ItemStack       selectedIcon = new ItemStack(SeCon.getInstance().getSeConConfig().CMDMENU_CMDITEM_MARKED_ITEM_ID, 1, (short) SeCon.getInstance().getSeConConfig().CMDMENU_CMDITEM_MARKED_ITEM_DAMAGE);
    
    private CommandMenuItem selectedItem;
    
    private CommandMenu(String name, Plugin plugin, Player player) {
        super(name, plugin);
        this.player = player;
    }
    
    @Override
    public void onOptionClick(OptionClickEvent e) {
        if (e.getPosition() < pages.getPage(this.currentPage).size()) {
            selectCmd(e.getPosition());
        } else if (e.getPosition() == 47 && selectedItem != null) {
            ChatUtils.sendFormattedMessage(this.player, SeCon.getInstance().getLang().COMMAND_HELP_HEADER.replace("<command>", "/" + selectedItem.getLabel()));
            ChatUtils.sendFormattedMessage(this.player, this.selectedItem.getHelp());
        }
        super.onOptionClick(e);
    }
    
    public void selectCmd(int position) {
        
        unmark();
        showPage(currentPage);
        
        selectedItem = (CommandMenuItem) getItem(position);
        selectedItem.setMarked(true);
        
        setMenuItem(position, selectedItem);
        
        MenuItem helpItem = new MenuItem(selectedIcon.clone(), ChatUtils.formatMessage(SeCon.getInstance().getLang().CMDMENU_LABEL_HELP), selectedItem.getHelp());
        MenuItem aliasesItem = new MenuItem(selectedIcon.clone(), ChatUtils.formatMessage(SeCon.getInstance().getLang().CMDMENU_LABEL_ALIASES), selectedItem.getAliases());
        MenuItem permItem = new MenuItem(selectedIcon.clone(), ChatUtils.formatMessage(SeCon.getInstance().getLang().CMDMENU_LABEL_PERMISSION), Arrays.asList(new String[] { selectedItem.getPermission() }));
        MenuItem addPermsItem = new MenuItem(selectedIcon.clone(), ChatUtils.formatMessage(SeCon.getInstance().getLang().CMDMENU_LABEL_ADD_PERMISSION), selectedItem.getAdditionalPermissions());
        
        setMenuItem(47, helpItem);
        setMenuItem(48, aliasesItem);
        setMenuItem(49, permItem);
        setMenuItem(50, addPermsItem);
    }
    
    @Override
    public void showPage(int position) {
        unmark();
        super.showPage(position);
    }
    
    public void unmark() {
        if (selectedItem != null) {
            selectedItem.setMarked(false);
            selectedItem = null;
        }
    }
}
