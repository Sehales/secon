// ALL RIGHTS RESERVED SEE LICENSE

package net.sehales.secon;

import java.io.File;
import java.lang.reflect.Field;
import java.sql.SQLException;

import net.sehales.secon.addon.AddonManager;
import net.sehales.secon.cmds.CmdSeCon;
import net.sehales.secon.command.CommandManager;
import net.sehales.secon.config.CommandConfig;
import net.sehales.secon.config.ItemConfig;
import net.sehales.secon.config.LanguageConfig;
import net.sehales.secon.config.SeConConfig;
import net.sehales.secon.db.Database;
import net.sehales.secon.db.MySQLDatabase;
import net.sehales.secon.player.PlayerListener;
import net.sehales.secon.player.PlayerManager;
import net.sehales.secon.utils.plugin.PluginUtils;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Sehales
 * 
 */
public class SeCon extends JavaPlugin {
    
    private static SeCon secon;
    
    public static SeCon getInstance() {
        return secon;
    }
    
    private boolean        debugEnabled = false;
    private SeConLogger    logger;
    
    private SeConConfig    config;
    private LanguageConfig lang;
    private CommandConfig  cmdConfig;
    private ItemConfig     itemConfig;
    
    private PluginUtils    pluginUtils;
    
    public static File     scriptFolder;
    public static File     addonFolder;
    public static File     addonConfigFolder;
    
    private Database       db           = null;
    
    private AddonManager   addonManager;
    private CommandManager commandManager;
    private PlayerManager  playerManager;
    private PlayerListener playerListener;
    
    public AddonManager getAddonManager() {
        return addonManager;
    }
    
    public CommandConfig getCmdConfig() {
        return cmdConfig;
    }
    
    public CommandManager getCommandManager() {
        return commandManager;
    }
    
    public ItemConfig getItemConfig() {
        return itemConfig;
    }
    
    public LanguageConfig getLang() {
        return lang;
    }
    
    public PlayerManager getPlayerManager() {
        return playerManager;
    }
    
    public PluginUtils getPluginUtils() {
        return pluginUtils;
    }
    
    public SeConConfig getSeConConfig() {
        return config;
    }
    
    public Database getSQLDB() {
        return db;
    }
    
    private void initListener() {
        getServer().getPluginManager().registerEvents((playerListener = new PlayerListener(playerManager)), this);
    }
    
    public boolean isDebugEnabled() {
        return debugEnabled;
    }
    
    public SeConLogger log() {
        return logger;
    }
    
    @Override
    public void onDisable() {
        HandlerList.unregisterAll(playerListener);
        addonManager.unloadAddons(null);
        commandManager.unregisterScriptCommands();
        playerManager.saveOnlinePlayers();
    }
    
    @Override
    public void onEnable() {
        logger = new SeConLogger(this);
        String root = getDataFolder() + File.separator;
        addonFolder = new File(root + "addons");
        scriptFolder = new File(root + "scripts");
        addonConfigFolder = new File(root + "addonconfig");
        
        saveResource("config.yml", false);
        saveResource("language.yml", false);
        saveResource("items.yml", false);
        saveResource("commands.yml", false);
        
        config = new SeConConfig(new File(root + "config.yml"));
        config.load();
        config.initValues();
        
        lang = new LanguageConfig(new File(root + "language.yml"));
        lang.load();
        lang.initValues();
        
        itemConfig = new ItemConfig(new File(root + "items.yml"));
        itemConfig.load();
        itemConfig.initValues();
        
        cmdConfig = new CommandConfig(new File(root + "commands.yml"));
        cmdConfig.load();
        
        if (debugEnabled) {
            System.out.println("configfile: " + config.getConfigFile());
            System.out.println("langfile: " + lang.getConfigFile());
            System.out.println("itemconfigfile: " + itemConfig.getConfigFile());
            System.out.println("cmdconfigfile: " + cmdConfig.getConfigFile());
            
            System.out.println("Begin config values!");
            try {
                for (Field f : config.getClass().getDeclaredFields()) {
                    if (f.getName().equals("MYSQL_DATABASE_PASSWORD")) {
                        System.out.println(f.getName() + " = CENSORED, length = " + f.get(config).toString().length());
                    } else {
                        System.out.println(f.getName() + " = " + f.get(config));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("End config values!");
            
            System.out.println("Begin language values!");
            try {
                for (Field f : lang.getClass().getDeclaredFields()) {
                    System.out.println(f.getName() + " = " + f.get(lang));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("End language values!");
            
        }
        
        switch (config.DB_TYPE.toLowerCase()) {
            case "mysql": {
                try {
                    db = new MySQLDatabase(config.MYSQL_SERVER_ADDRESS, config.MYSQL_SERVER_PORT, config.MYSQL_DATABASE_NAME, config.MYSQL_DATABASE_USERNAME, config.MYSQL_DATABASE_PASSWORD, config.MYSQL_TABLE_PREFIX);
                    getServer().getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
                        
                        @Override
                        public void run() {
                            synchronized (db) {
                                try {
                                    SeCon.getInstance().getSQLDB().executeQuery("SELECT 1;");
                                } catch (Exception e) {
                                    SeCon.getInstance().getSQLDB().autoReconnect();
                                }
                            }
                        }
                        
                    }, 20 * 60 * 60 * 4, 20 * 60 * 60 * 4);
                } catch (SQLException e) {
                    log().severe("Database", "Can't connect to mysql database. Not yet configured?");
                    e.printStackTrace();
                }
                break;
            }
            default: {
                break;
            }
        }
        
        try {
            commandManager = new CommandManager(this);
        } catch (SecurityException | IllegalArgumentException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        this.pluginUtils = new PluginUtils(this);
        playerManager = new PlayerManager(this);
        
        commandManager.loadScriptCommands(scriptFolder);
        
        addonManager = new AddonManager(this, addonFolder);
        addonManager.loadAddons(null);
        
        initListener();
        getCommand("secon").setExecutor(new CmdSeCon(this));
    }
    
    @Override
    public void onLoad() {
        secon = this;
        
        if (new File(getDataFolder(), "debug.enabled").exists()) {
            debugEnabled = true;
        }
        
    }
}
