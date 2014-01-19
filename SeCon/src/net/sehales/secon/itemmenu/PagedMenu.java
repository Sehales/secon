
package net.sehales.secon.itemmenu;

import java.util.ArrayList;
import java.util.List;

import net.sehales.secon.SeCon;
import net.sehales.secon.config.LanguageConfig;
import net.sehales.secon.utils.PageBuilder;
import net.sehales.secon.utils.mc.ChatUtils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class PagedMenu extends Menu implements OptionClickEventHandler {
    
    private MenuItem                nextPageButton;
    private MenuItem                previousPageButton;
    
    private int                     nextPageButtonLocation;
    private int                     previousPageButtonLocation;
    
    protected int                   currentPage;
    protected PageBuilder<MenuItem> pages;
    
    public PagedMenu(String name, int size, OptionClickEventHandler handler, Plugin plugin) {
        super(name, size, handler, plugin);
    }
    
    public PagedMenu(String name, int size, Plugin plugin) {
        super(name, size, null, plugin);
        setHandler(this);
    }
    
    public PagedMenu(String name, Plugin plugin) {
        this(name, 54, plugin);
        
        LanguageConfig lang = SeCon.getInstance().getLang();
        List<String> nextInfo = new ArrayList<String>();
        nextInfo.add(ChatUtils.formatMessage(lang.PAGEDMENU_BTN_NEXTPAGE_TEXT));
        
        List<String> prevInfo = new ArrayList<String>();
        prevInfo.add(ChatUtils.formatMessage(lang.PAGEDMENU_BTN_PREVIOUSPAGE_TEXT));
        
        setNextPageButton(new MenuItem(new ItemStack(Material.WOOL, 1, (short) 5), ChatUtils.formatMessage(lang.PAGEDMENU_BTN_NEXTPAGE_LABEL), nextInfo));
        setPreviousPageButton(new MenuItem(new ItemStack(Material.WOOL, 1, (short) 14), ChatUtils.formatMessage(lang.PAGEDMENU_BTN_PREVIOUSPAGE_LABEL), prevInfo));
        
        setNextPageButtonLocation(53);
        setPreviousPageButtonLocation(52);
    }
    
    public int getCurrentPage() {
        return currentPage;
    }
    
    public MenuItem getNextPageButton() {
        return nextPageButton;
    }
    
    public int getNextPageButtonLocation() {
        return nextPageButtonLocation;
    }
    
    public PageBuilder<MenuItem> getPages() {
        return pages;
    }
    
    public MenuItem getPreviousPageButton() {
        return previousPageButton;
    }
    
    public int getPreviousPageButtonLocation() {
        return previousPageButtonLocation;
    }
    
    public void init(final List<MenuItem> content) {
        // for (MenuItem item : content)
        // System.out.println(item.getIcon() + " " + item.getLabel() + " " +
        // item.getText().size());
        pages = new PageBuilder<MenuItem>(content, 45);
        pages.build();
        if (pages.size() > 0) {
            showPage(0);
        }
    }
    
    public void nextPage() {
        currentPage++;
        if (currentPage >= pages.size()) {
            currentPage = 0;
        }
        showPage(currentPage);
    }
    
    @Override
    public void onOptionClick(OptionClickEvent e) {
        if (e.getPosition() == nextPageButtonLocation) {
            nextPage();
        } else if (e.getPosition() == previousPageButtonLocation) {
            previousPage();
        }
    }
    
    public void previousPage() {
        currentPage--;
        if (currentPage < 0) {
            currentPage = pages.size() - 1;
        }
        showPage(currentPage);
    }
    
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    
    public void setNextPageButton(MenuItem nextPageButton) {
        this.nextPageButton = nextPageButton;
    }
    
    public void setNextPageButtonLocation(int nextPageButtonLocation) {
        this.nextPageButtonLocation = nextPageButtonLocation;
    }
    
    public void setPages(PageBuilder<MenuItem> pages) {
        this.pages = pages;
    }
    
    public void setPreviousPageButton(MenuItem previousPageButton) {
        this.previousPageButton = previousPageButton;
    }
    
    public void setPreviousPageButtonLocation(int previousPageButtonLocation) {
        this.previousPageButtonLocation = previousPageButtonLocation;
    }
    
    public void showPage(int i) {
        clear();
        int k = 0;
        for (MenuItem item : pages.getPage(i)) {
            setMenuItem(k++, item);
        }
        
        setMenuItem(nextPageButtonLocation, nextPageButton);
        setMenuItem(previousPageButtonLocation, previousPageButton);
    }
    
}
