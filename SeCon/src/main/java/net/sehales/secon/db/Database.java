
package net.sehales.secon.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Database {
    
    public static enum DBType {
        SQLITE, MYSQL
    }
    
    protected Connection con;
    
    public boolean autoReconnect() {
        if (!isConnectionValid()) {
            try {
                try {
                    con.close();
                } catch (Exception e) {
                    
                }
                connect();
                return isConnectionValid();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
    
    /**
     * get the database connection
     * 
     * @return the connection
     */
    public Connection con() {
        return con;
    }
    
    /**
     * self explaining
     * 
     * @throws SQLException
     */
    public abstract void connect() throws SQLException;
    
    /**
     * 
     * @return
     */
    public abstract Statement createStatement();
    
    /**
     * execute a single query
     * 
     * @param query
     * @return true on success
     */
    public abstract boolean execute(String query);
    
    /**
     * create a prepared statement and execute it
     * 
     * @param query
     * @param values
     * @return
     */
    public abstract boolean executePrepared(String query, Object... values);
    
    /**
     * 
     * @param query
     * @param values
     * @return
     */
    public abstract ResultSet executePreparedQuery(String query, Object... values);
    
    /**
     * 
     * @param query
     * @param values
     * @return -1 if an error occures else look at java.sql.Statement
     * @see java.sql.Statement#executeUpdate
     */
    public abstract int executePreparedUpdate(String query, Object... values);
    
    /**
     * 
     * @param query
     * @return
     */
    public abstract ResultSet executeQuery(String query);
    
    /**
     * 
     * @param query
     * @return -1 if an error occures else look at java.sql.Statement
     * @see java.sql.Statement#executeUpdate
     */
    public abstract int executeUpdate(String query);
    
    /**
     * get the database connection
     * 
     * @return the connection
     */
    public Connection getConnection() {
        return con;
    }
    
    public abstract String getDatabaseName();
    
    /**
     * this will maybe add a configured prefix to your tablename
     * so you don't have to care about that by yourself
     * 
     * @param tableName
     * @return formatted or same table name, depending on the underlying
     *         database implementation
     */
    public abstract String getFormattedTableName(String tableName);
    
    public abstract String getTablePrefix();
    
    /**
     * get the underlying type of that database
     * 
     * @return
     */
    public abstract DBType getType();
    
    public boolean isConnectionValid() {
        try {
            return con != null ? con.isValid(3) : false;
        } catch (SQLException e) {
            return false;
        }
    }
    
    /**
     * checks the querys first statement for an update statement
     * 
     * that method is used by Database.execute... methods to decide to use
     * either Statement.executeQuery() or Statement.executeUpdate()
     * 
     * @param query
     * @return
     */
    public abstract boolean isUpdate(String query);
    
    /**
     * creates a prepared statement and fills in the values for that prepared
     * statement
     * 
     * @param statement
     * @param values
     * @return
     */
    public abstract PreparedStatement prepareStatement(String statement, Object... values);
}
