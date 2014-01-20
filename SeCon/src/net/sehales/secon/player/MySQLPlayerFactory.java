
package net.sehales.secon.player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import net.sehales.secon.SeCon;
import net.sehales.secon.db.Database;
import net.sehales.secon.events.PlayerLoadEvent;
import net.sehales.secon.events.PlayerSaveEvent;

import org.bukkit.Bukkit;

public class MySQLPlayerFactory implements PlayerFactory {
    
    private SeCon             secon;
    private Database          db;
    private Map<String, Long> playerIds            = new HashMap<String, Long>();
    private PreparedStatement GET_DATA, CREATE_PLAYER_ENTRY;
    private String            INSERT_DATA;
    private final String      insertValuesTemplate = "('%d',?,?),";
    private String            DELETE_KEYS;
    
    @SuppressWarnings("unused")
    private MySQLPlayerFactory() {
        
    }
    
    MySQLPlayerFactory(SeCon secon) {
        this.secon = secon;
        db = secon.getSQLDB();
        init();
    }
    
    private boolean createDBEntry(SCPlayer player) {
        try {
            CREATE_PLAYER_ENTRY.clearParameters();
            CREATE_PLAYER_ENTRY.setString(1, player.getName());
            CREATE_PLAYER_ENTRY.execute();
            ResultSet result = CREATE_PLAYER_ENTRY.getGeneratedKeys();
            result.next();
            playerIds.put(player.getName(), result.getLong(1));
            player.putTransientData("FIRST_CONNECT", true);
            player.putData(SCPlayer.KEY_FIRST_ONLINE, System.currentTimeMillis() + "");
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    private void init() {
        String dbName = "`" + db.getDatabaseName() + "`";
        String dataTable = dbName + "." + db.getFormattedTableName("playerdata");
        String playerTable = dbName + "." + db.getFormattedTableName("players");
        
        //@formatter:off
        db.execute("CREATE TABLE IF NOT EXISTS " + dataTable + " (" + 
                   "`dbId` INT NOT NULL," + 
                   "`key` VARCHAR(45) NOT NULL," + 
                   "`value` TEXT," + 
                   "UNIQUE KEY `unique_pData` (`dbId`,`key`))" + 
                   "ENGINE=InnoDB CHARSET=utf8;");
        
        db.execute("CREATE TABLE IF NOT EXISTS " + playerTable + " (" +
                   "`id` INT NOT NULL AUTO_INCREMENT," +
                   "`name` VARCHAR(16) NULL," +
                   "`playerId` INT NULL DEFAULT -1," +
                   "PRIMARY KEY (`id`)," +
                   "UNIQUE INDEX `name_UNIQUE` (`name` ASC))" +
//                   "CONSTRAINT `foreignKey_player`" +
//                   "FOREIGN KEY (`id`)" +
//                     "REFERENCES " + dataTableName + " (`id`)" +
//                     "ON DELETE CASCADE" +
//                     "ON UPDATE CASCADE)" +
                   "ENGINE=InnoDB CHARSET=utf8;");
        
        
        try {
            CREATE_PLAYER_ENTRY = db.getConnection().prepareStatement(
                                   String.format(
                                                 "INSERT INTO %1$s (`name`) VALUES (?);" 
                                                //+  "SELECT LAST_INSERT_ID();"
                                                                 , playerTable
                                                 ), Statement.RETURN_GENERATED_KEYS);
            
            GET_DATA = db.getConnection().prepareStatement(
                                   String.format(
                                                 "SELECT %1$s.`id`," +
                                                 "%2$s.`key`," + 
                                                 "%2$s.`value` " + 
                                                 "FROM %1$s " +
                                                 "LEFT JOIN %2$s " +
                                                 "ON %1$s.`id` = %2$s.`dbId` "+
                                                 "WHERE %1$s.`name` = ?;", playerTable, dataTable
                                                 ));
            
            INSERT_DATA = String.format(
                                        "INSERT INTO %1$s " +
                                        "(`dbId`,`key`,`value`)" +
                                        "VALUES %2$s " +
                                        "ON DUPLICATE KEY UPDATE `value` = VALUES(`value`)", dataTable,"%s"
                                        );
            
            DELETE_KEYS = String.format(
                                        "DELETE FROM %1$s " +
                                        "WHERE `dbId` = ? AND `key` IN (%2$s)", dataTable,"%s"
                                        );
        
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //@formatter:on
    }
    
    @Override
    public SCPlayer loadPlayer(String name) throws PlayerLoadException {
        SCPlayer player = null;
        player = new SCPlayer(name);
        ResultSet result = null;
        boolean success = true;
        try {
            GET_DATA.clearParameters();
            GET_DATA.setString(1, name);
            
            synchronized (db.getConnection()) {
                result = GET_DATA.executeQuery();
            }
            
            if (result != null && result.next()) {
                playerIds.put(name, result.getLong("id"));
                do {
                    player.putData(result.getString("key"), result.getString("value"));
                } while (result.next());
            } else {
                success = createDBEntry(player);
            }
        } catch (SQLException e) {
            success = false;
            // secon.log().severe("MySQLPlayerFactory",
            // String.format("Failed to load data for player '%s'!", name));
            throw new PlayerLoadException(String.format("Failed to load data for player '%s'!", name), e);
        }
        if (success) {
            Bukkit.getPluginManager().callEvent(new PlayerLoadEvent(player));
        } else {
            throw new PlayerLoadException(String.format("Failed to load data for player '%s'!", name));
        }
        return player;
    }
    
    // TODO: test it!!!
    private PreparedStatement prepareDataInsertStatement(SCPlayer player) throws SQLException {
        StringBuilder sb = new StringBuilder();
        long id = playerIds.get(player.getName());
        
        String s = String.format(insertValuesTemplate, id);
        int entryCount = player.getDataMap().size();
        for (int i = 0; i < entryCount; i++) {
            sb.append(s);
        }
        
        PreparedStatement stmt = db.getConnection().prepareStatement(String.format(INSERT_DATA, sb.substring(0, sb.length() - 1)));
        
        int pos = 1;
        for (Map.Entry<String, String> entry : player.getDataMap().entrySet()) {
            stmt.setString(pos++, entry.getKey());
            stmt.setString(pos++, entry.getValue());
        }
        
        if (secon.isDebugEnabled()) {
            System.out.println("DEBUG: PreparedStatement in MySQLPlayerFactory.java:prepareInsertStatement()");
            System.out.println(stmt.toString());
        }
        return stmt;
    }
    
    private PreparedStatement prepareKeyDeleteStatement(SCPlayer player) throws SQLException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < player.getRemovedDataKeys().size(); i++) {
            sb.append("?,");
        }
        
        PreparedStatement stmt = db.getConnection().prepareStatement(String.format(DELETE_KEYS, sb.substring(0, sb.length() - 1)));
        
        int i = 1;
        stmt.setLong(i++, playerIds.get(player.getName()));
        
        for (String key : player.getRemovedDataKeys()) {
            stmt.setString(i++, key);
        }
        
        if (secon.isDebugEnabled()) {
            System.out.println("DEBUG: PreparedStatement in MySQLPlayerFactory.java:prepareKeyDeleteStatement()");
            System.out.println(stmt.toString());
        }
        return stmt;
    }
    
    @Override
    public void savePlayer(SCPlayer player) throws PlayerSaveException {
        for (String key : player.getRemovedDataKeys().toArray(new String[0])) {
            if (player.getDataMap().containsKey(key)) {
                player.getRemovedDataKeys().remove(key);
            }
        }
        Bukkit.getPluginManager().callEvent(new PlayerSaveEvent(player));
        
        try (PreparedStatement saveData = prepareDataInsertStatement(player)) {
            synchronized (db.getConnection()) {
                saveData.execute();
            }
        } catch (SQLException e) {
            throw new PlayerSaveException(String.format("Failed to save data for player '%s'", player.getName()));
        }
        
        if (!player.getRemovedDataKeys().isEmpty()) {
            try (PreparedStatement deleteKeys = prepareKeyDeleteStatement(player)) {
                synchronized (db.getConnection()) {
                    deleteKeys.execute();
                }
            } catch (SQLException e) {
                throw new PlayerSaveException(String.format("Failed to delete obsolete keys for player '%s'. List of obsolete keys: %s", player.getName(), player.getRemovedDataKeys().toString()));
            }
        }
        
    }
}
