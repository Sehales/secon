
package net.sehales.secon.config;

import java.io.File;

public class SeConConfig extends Config {
    
    public String  DB_TYPE;
    public String  MYSQL_TABLE_PREFIX;
    public String  MYSQL_DATABASE_PASSWORD;
    public String  MYSQL_DATABASE_USERNAME;
    public String  MYSQL_DATABASE_NAME;
    public int     MYSQL_SERVER_PORT;
    public String  MYSQL_SERVER_ADDRESS;
    
    public boolean SUPPORT_TOWNY_ENABLED;
    public boolean SUPPORT_MYSQL_ENABLED;
    
    public int     CMDMENU_CMDITEM_BUKKIT_ITEM_ID;
    public int     CMDMENU_CMDITEM_BUKKIT_ITEM_DAMAGE;
    public int     CMDMENU_CMDITEM_SECON_ITEM_ID;
    public int     CMDMENU_CMDITEM_SECON_ITEM_DAMAGE;
    public int     CMDMENU_CMDITEM_MARKED_ITEM_DAMAGE;
    public int     CMDMENU_CMDITEM_MARKED_ITEM_ID;
    
    public int     CONFIG_VERSION;
    
    public SeConConfig(File configFile) {
        super(configFile);
    }
    
    @Override
    public void initValues() {
        DB_TYPE = getString("database.type");// currently only supports
                                             // mysql
        MYSQL_TABLE_PREFIX = getString("database.mysql.prefix");
        MYSQL_DATABASE_PASSWORD = getString("database.mysql.password");
        MYSQL_DATABASE_USERNAME = getString("database.mysql.user");
        MYSQL_DATABASE_NAME = getString("database.mysql.name");
        MYSQL_SERVER_PORT = getInt("database.mysql.port");
        MYSQL_SERVER_ADDRESS = getString("database.mysql.address");
        
        CONFIG_VERSION = getInt("config-file-version");
        
        SUPPORT_TOWNY_ENABLED = getBoolean("support.towny.enabled");
        SUPPORT_MYSQL_ENABLED = getBoolean("support.mysql.enabled");
        
        CMDMENU_CMDITEM_BUKKIT_ITEM_DAMAGE = getInt("command-menu.cmd-item.bukkit.damage-value");
        CMDMENU_CMDITEM_BUKKIT_ITEM_ID = getInt("command-menu.cmd-item.bukkit.id");
        CMDMENU_CMDITEM_SECON_ITEM_DAMAGE = getInt("command-menu.cmd-item.secon.damage-value");
        CMDMENU_CMDITEM_SECON_ITEM_ID = getInt("command-menu.cmd-item.secon.id");
        CMDMENU_CMDITEM_MARKED_ITEM_DAMAGE = getInt("command-menu.cmd-item.marked.damage-value");
        CMDMENU_CMDITEM_MARKED_ITEM_ID = getInt("command-menu.cmd-item.marked.id");
    }
    
}
