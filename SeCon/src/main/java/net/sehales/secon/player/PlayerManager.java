
package net.sehales.secon.player;

import java.util.Map;

import net.sehales.secon.SeCon;
import net.sehales.secon.events.PlayerOfflineEvent;
import net.sehales.secon.events.PlayerOnlineEvent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.collect.MapMaker;

public class PlayerManager {
    
    private Map<String, SCPlayer> players       = new MapMaker().softValues().makeMap();
    private Map<String, SCPlayer> onlinePlayers = new MapMaker().makeMap();
    private PlayerFactory         factory;
    private final Object          lock          = new Object();
    private SeCon                 secon;
    
    public PlayerManager(SeCon secon) {
        this.secon = secon;
        if (secon.getSQLDB() != null) {
            switch (secon.getSQLDB().getType()) {
                case MYSQL: {
                    this.factory = new MySQLPlayerFactory(secon);
                    break;
                }
                case SQLITE: {
                    secon.log().warning("PlayerManager", "SQLite is currently not supported. please change your database type to MySQL.");
                    break;
                }
                default: {
                    secon.log().warning("PlayerManager", "MySQL not configured, can't load data.");
                    break;
                }
            }
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            try {
                getPlayer(p.getName());
            } catch (Exception e) {
                secon.log().severe("PlayerManager", String.format("Failed to load data for player '%s'. Exception message: %s", p.getName(), e.getMessage()));
            }
        }
    }
    
    public SCPlayer getExistingPlayer(String name) {
        synchronized (lock) {
            if (players.containsKey(name)) {
                return players.get(name);
            } else {
                return null;
            }
        }
    }
    
    public SCPlayer getPlayer(String name) {
        synchronized (lock) {
            if (onlinePlayers.containsKey(name)) {
                return onlinePlayers.get(name);
            } else if (players.containsKey(name)) {
                return players.get(name);
            } else {
                SCPlayer player = null;
                try {
                    player = factory.loadPlayer(name);
                } catch (PlayerLoadException e) {
                    e.printStackTrace();
                }
                if (player != null) {
                    players.put(name, player);
                    return player;
                } else {
                    return null;
                }
            }
        }
    }
    
    public PlayerFactory getPlayerFactory() {
        return factory;
    }
    
    public void removePlayer(String name) {
        onlinePlayers.remove(name);
        players.remove(name);
    }
    
    public void saveOnlinePlayers() {
        for (SCPlayer player : onlinePlayers.values()) {
            try {
                factory.savePlayer(player);
            } catch (Exception e) {
                secon.log().severe("PlayerManager", e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    public void setPlayerOffline(final Player player) {
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                SCPlayer scplayer;
                synchronized (lock) {
                    scplayer = onlinePlayers.remove(player.getName());
                    
                    scplayer.putData(SCPlayer.KEY_LAST_ONLINE, System.currentTimeMillis() + "");
                    scplayer.putData(SCPlayer.KEY_LAST_IP, player.getAddress().getHostString());
                    scplayer.setOnline(false);
                    
                    Bukkit.getPluginManager().callEvent(new PlayerOfflineEvent(scplayer));
                    
                    scplayer.getTransientDataMap().clear();
                }
                try {
                    factory.savePlayer(scplayer);
                } catch (PlayerSaveException e) {
                    secon.log().severe("PlayerManager", e.getMessage());
                    e.printStackTrace();
                }
            }
            
        }).start();
        
    }
    
    public void setPlayerOnline(final String name) {
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                SCPlayer player = null;
                synchronized (lock) {
                    player = getPlayer(name);
                    
                    player.setOnline(true);
                    onlinePlayers.put(player.getName(), player);
                }
                Bukkit.getPluginManager().callEvent(new PlayerOnlineEvent(player));
            }
            
        }).start();
    }
}
