
package net.sehales.secon.db;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Properties;

public class MySQLDatabase extends Database {
    
    private Properties userInfo;
    private String     serverAddress, databaseName, tablePrefix;
    private int        serverPort;
    
    @SuppressWarnings("unused")
    private MySQLDatabase() {
    }
    
    public MySQLDatabase(String serverAddress, int serverPort, String databaseName, String user, String password, String tablePrefix) throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.databaseName = databaseName;
        this.tablePrefix = tablePrefix;
        
        userInfo = new Properties();
        userInfo.put("user", user);
        userInfo.put("password", password);
        
        connect();
    }
    
    @Override
    public synchronized void connect() throws SQLException {
        this.con = DriverManager.getConnection(getConectionString(), userInfo);
    }
    
    @Override
    public Statement createStatement() {
        if (!autoReconnect()) {
            return null;
        }
        try {
            synchronized (con) {
                return con.createStatement();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public boolean execute(String query) {
        if (!autoReconnect()) {
            return false;
        }
        try {
            Statement stmt = null;
            synchronized (con) {
                stmt = con.createStatement();
                if (isUpdate(query)) {
                    stmt.executeUpdate(query);
                } else {
                    stmt.executeQuery(query);
                }
            }
            stmt.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean executePrepared(String query, Object... values) {
        if (!autoReconnect()) {
            return false;
        }
        PreparedStatement stmt = prepareStatement(query, values);
        if (stmt == null) {
            return false;
        }
        try {
            synchronized (con) {
                if (isUpdate(query)) {
                    stmt.executeUpdate();
                } else {
                    stmt.executeQuery();
                }
            }
            stmt.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public ResultSet executePreparedQuery(String query, Object... values) {
        if (!autoReconnect()) {
            return null;
        }
        PreparedStatement stmt = prepareStatement(query, values);
        if (stmt == null) {
            return null;
        }
        try {
            synchronized (con) {
                return stmt.executeQuery();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public int executePreparedUpdate(String query, Object... values) {
        if (!autoReconnect()) {
            return -1;
        }
        PreparedStatement stmt = prepareStatement(query, values);
        if (stmt == null) {
            return -1;
        }
        try {
            synchronized (con) {
                return stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    @Override
    public ResultSet executeQuery(String query) {
        if (!autoReconnect()) {
            return null;
        }
        try {
            Statement stmt = null;
            synchronized (con) {
                stmt = con.createStatement();
                return stmt.executeQuery(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public int executeUpdate(String query) {
        if (!autoReconnect()) {
            return -1;
        }
        try {
            Statement stmt = null;
            synchronized (con) {
                stmt = con.createStatement();
                return stmt.executeUpdate(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    private String getConectionString() {
        return String.format("jdbc:mysql://%s:%s/%s", serverAddress, serverPort, databaseName);
    }
    
    @Override
    public String getDatabaseName() {
        return databaseName;
    }
    
    @Override
    public String getFormattedTableName(String tableName) {
        return String.format("`%s%s`", tablePrefix, tableName);
    }
    
    @Override
    public String getTablePrefix() {
        return tablePrefix;
    }
    
    @Override
    public DBType getType() {
        return DBType.MYSQL;
    }
    
    @Override
    public boolean isUpdate(String query) {
        final String q = query.trim();
        if (q.substring(0, 6).equalsIgnoreCase("SELECT")) {
            return false;
        } else if (q.substring(0, 6).equalsIgnoreCase("INSERT")) {
            return true;
        } else if (q.substring(0, 6).equalsIgnoreCase("UPDATE")) {
            return true;
        } else if (q.substring(0, 6).equalsIgnoreCase("DELETE")) {
            return true;
        } else if (q.substring(0, 6).equalsIgnoreCase("CREATE")) {
            return true;
        } else if (q.substring(0, 5).equalsIgnoreCase("ALTER")) {
            return true;
        } else if (q.substring(0, 4).equalsIgnoreCase("DROP")) {
            return true;
        } else if (q.substring(0, 8).equalsIgnoreCase("TRUNCATE")) {
            return true;
        } else if (q.substring(0, 6).equalsIgnoreCase("RENAME")) {
            return true;
        } else if (q.substring(0, 2).equalsIgnoreCase("DO")) {
            return true;
        } else if (q.substring(0, 7).equalsIgnoreCase("REPLACE")) {
            return true;
        } else if (q.substring(0, 4).equalsIgnoreCase("LOAD")) {
            return true;
        } else if (q.substring(0, 7).equalsIgnoreCase("HANDLER")) {
            return true;
        } else if (q.substring(0, 4).equalsIgnoreCase("CALL")) {
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public PreparedStatement prepareStatement(String statement, Object... values) {
        if (!autoReconnect()) {
            return null;
        }
        PreparedStatement stmt;
        synchronized (con) {
            try {
                stmt = con.prepareStatement(statement);
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }
        
        if (values == null) {
            return stmt;
        }
        
        int counter = 1;
        for (Object param : values) {
            try {
                if (param == null) {
                    stmt.setNull(counter++, Types.NULL);
                } else if (param instanceof Integer) {
                    stmt.setInt(counter++, (Integer) param);
                } else if (param instanceof String) {
                    stmt.setString(counter++, (String) param);
                } else if (param instanceof Long) {
                    stmt.setLong(counter++, (Long) param);
                } else if (param instanceof Double) {
                    stmt.setDouble(counter++, (Double) param);
                } else if (param instanceof Short) {
                    stmt.setShort(counter++, (Short) param);
                } else if (param instanceof Byte) {
                    stmt.setByte(counter++, (Byte) param);
                } else if (param instanceof Date) {
                    stmt.setDate(counter++, (Date) param);
                } else if (param instanceof java.util.Date) {
                    stmt.setDate(counter++, new Date(((java.util.Date) param).getTime()));
                } else if (param instanceof Object) {
                    stmt.setObject(counter++, param);
                } else {
                    System.out.printf("MySQLDB - Unsupported data type %s", param.getClass().getSimpleName());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return stmt;
    }
}
