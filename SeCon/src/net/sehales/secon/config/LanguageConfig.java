
package net.sehales.secon.config;

import java.io.File;

public class LanguageConfig extends Config {
    
    /**
     * LANGUAGE_NODES
     */
    public String COMMAND_HELP_HEADER;
    
    public String ADDON_LOADING_ADDONS;
    public String ADDON_LOADED_ADDONS;
    public String ADDON_ALREADY_LOADED;
    public String ADDON_FAILED_LOAD;
    public String ADDON_LOADED;
    public String ADDON_ALREADY_ENABLED;
    public String ADDON_ENABLED;
    public String ADDON_ALREADY_DISABLED;
    public String ADDON_DISABLED;
    public String ADDON_UNLOADING_ADDONS;
    public String ADDON_UNLOADED;
    public String ADDON_UNLOADED_ADDONS;
    
    public String ARGUMENT_CANT_BE_NULL;
    public String NO_PERMISSION;
    public String YOU_CANNOT_USE_THIS;
    public String FILE_NOT_FOUND;
    public String CANT_READ_FILE;
    public String EXCEPTION_OCCURED;
    public String INVALID_CLASS_TYPE;
    
    public String CMDMENU_NAME;
    public String CMDMENU_LABEL_HELP;
    public String CMDMENU_LABEL_ALIASES;
    public String CMDMENU_LABEL_PERMISSION;
    public String CMDMENU_LABEL_ADD_PERMISSION;
    public String CMDMENU_DEFAULT_TEXT_PREFIX;
    public String CMDMENU_CMDITEM_SECON_LABEL;
    public String CMDMENU_CMDITEM_BUKKIT_LABEL;
    public String CMDMENU_CMDITEM_TEXT;
    
    public String PAGEDMENU_NAME;
    public String PAGEDMENU_BTN_NEXTPAGE_LABEL;
    public String PAGEDMENU_BTN_NEXTPAGE_TEXT;
    public String PAGEDMENU_BTN_PREVIOUSPAGE_LABEL;
    public String PAGEDMENU_BTN_PREVIOUSPAGE_TEXT;
    
    public String HELP_ADDON_ENABLE;
    public String HELP_ADDON_DISABLE;
    public String HELP_ADDON_SOFT_RELOAD;
    public String HELP_ADDON_SOFT_RELOAD_ALL;
    public String HELP_ADDON_LOAD;
    public String HELP_ADDON_LOAD_ALL;
    public String HELP_ADDON_UNLOAD;
    public String HELP_ADDON_UNLOAD_ALL;
    public String HELP_ADDON_RELOAD;
    public String HELP_ADDON_RELOAD_ALL;
    public String HELP_ADDON_LIST;
    public String HELP_ADDON_MENU;
    
    public String HELP_SECON_ADDON;
    public String HELP_SECON_RELOAD;
    public String HELP_SECON_COMMANDS;
    public String HELP_SECON_COMMANDMENU;
    
    public String CONFIG_FILE_LOADED;
    public String CONFIG_FILE_SAVED;
    public String RELOAD_STARTED;
    public String RELOAD_FINISHED;
    public String PLAYER_NOT_FOUND;
    public String NOT_ENOUGH_ARGUMENTS;
    
    public LanguageConfig(File configFile) {
        super(configFile);
    }
    
    @Override
    public void initValues() {
        COMMAND_HELP_HEADER = getString("global.standard-cmdhelp-head");
        ARGUMENT_CANT_BE_NULL = getString("global.argument-cant-be-null");
        NOT_ENOUGH_ARGUMENTS = getString("global.not-enough-arguments");
        NO_PERMISSION = getString("global.no-permission");
        YOU_CANNOT_USE_THIS = getString("global.you-cant-use-this");
        FILE_NOT_FOUND = getString("global.file-not-found");
        CANT_READ_FILE = getString("global.cant-read-file");
        EXCEPTION_OCCURED = getString("global.exception-occured");
        INVALID_CLASS_TYPE = getString("global.invalid-class-type");
        CONFIG_FILE_SAVED = getString("global.config-saved");
        CONFIG_FILE_LOADED = getString("global.config-loaded");
        RELOAD_STARTED = getString("global.reload-started");
        RELOAD_FINISHED = getString("global.reload-finished");
        PLAYER_NOT_FOUND = getString("global.player-not-found");
        
        ADDON_LOADING_ADDONS = getString("addon-manager.loading-addons");
        ADDON_LOADED_ADDONS = getString("addon-manager.loaded-addons");
        ADDON_ALREADY_LOADED = getString("addon-manager.already-loaded");
        ADDON_FAILED_LOAD = getString("addon-manager.failed-to-load");
        ADDON_LOADED = getString("addon-manager.loaded-addon");
        ADDON_ALREADY_ENABLED = getString("addon-manager.already-enabled");
        ADDON_ENABLED = getString("addon-manager.enabled-addon");
        ADDON_ALREADY_DISABLED = getString("addon-manager.already-disabled");
        ADDON_DISABLED = getString("addon-manager.disabled-addon");
        ADDON_UNLOADING_ADDONS = getString("addon-manager.unloading-addons");
        ADDON_UNLOADED = getString("addon-manager.unloaded-addon");
        ADDON_UNLOADED_ADDONS = getString("addon-manager.unloaded-addons");
        
        HELP_ADDON_ENABLE = getString("cmd-help.addon.enable");
        HELP_ADDON_DISABLE = getString("cmd-help.addon.disable");
        HELP_ADDON_SOFT_RELOAD = getString("cmd-help.addon.soft-reload");
        HELP_ADDON_SOFT_RELOAD_ALL = getString("cmd-help.addon.soft-reload-all");
        HELP_ADDON_LOAD = getString("cmd-help.addon.load");
        HELP_ADDON_LOAD_ALL = getString("cmd-help.addon.load-all");
        HELP_ADDON_UNLOAD = getString("cmd-help.addon.unload");
        HELP_ADDON_UNLOAD_ALL = getString("cmd-help.addon.unload-all");
        HELP_ADDON_RELOAD = getString("cmd-help.addon.reload");
        HELP_ADDON_RELOAD_ALL = getString("cmd-help.addon.reload-all");
        HELP_ADDON_LIST = getString("cmd-help.addon.list");
        HELP_ADDON_MENU = getString("cmd-help.addon.menu");
        
        HELP_SECON_ADDON = getString("cmd-help.cmd-addon");
        HELP_SECON_RELOAD = getString("cmd-help.cmd-reload");
        HELP_SECON_COMMANDS = getString("cmd-help.cmd-commands");
        HELP_SECON_COMMANDMENU = getString("cmd-help.cmd-commandmenu");
        
        PAGEDMENU_NAME = getString("paged-menu.name");
        PAGEDMENU_BTN_NEXTPAGE_LABEL = getString("paged-menu.button-label.next-page");
        PAGEDMENU_BTN_NEXTPAGE_TEXT = getString("paged-menu.button-text.next-page");
        PAGEDMENU_BTN_PREVIOUSPAGE_LABEL = getString("paged-menu.button-label.previous-page");
        PAGEDMENU_BTN_PREVIOUSPAGE_TEXT = getString("paged-menu.button-text.previous-page");
        
        CMDMENU_NAME = getString("command-menu.name");
        CMDMENU_LABEL_HELP = getString("command-menu.button-label.help");
        CMDMENU_LABEL_ALIASES = getString("command-menu.button-label.aliases");
        CMDMENU_LABEL_PERMISSION = getString("command-menu.button-label.permission");
        CMDMENU_LABEL_ADD_PERMISSION = getString("command-menu.button-label.additional-permissions");
        CMDMENU_DEFAULT_TEXT_PREFIX = getString("command-menu.default-text-prefix");
        CMDMENU_CMDITEM_TEXT = getString("command-menu.command-item.text");
    }
}
