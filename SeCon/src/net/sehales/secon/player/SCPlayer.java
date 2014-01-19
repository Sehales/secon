
package net.sehales.secon.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SCPlayer {
    
    public static final String  KEY_LAST_IP      = "LAST_IP";
    public static final String  KEY_LAST_ONLINE  = "LAST_ONLINE";
    public static final String  KEY_FIRST_ONLINE = "FIRST_ONLINE";
    
    private String              name             = null;
    private boolean             online           = false;
    
    private Map<String, String> data             = new HashMap<>();
    private Map<String, Object> transientData    = new HashMap<>();
    private List<String>        removedData      = new ArrayList<>();
    
    protected SCPlayer(String name) {
        this.name = name;
    }
    
    public boolean addData(String key, String value) {
        if (key == null || key.isEmpty() || value == null) {
            return false;
        }
        if (!data.containsKey(value)) {
            data.put(key, value);
            return true;
        }
        return false;
    }
    
    public boolean addTransientData(String key, String value) {
        if (key == null || key.isEmpty()) {
            return false;
        }
        if (!transientData.containsKey(value)) {
            transientData.put(key, value);
            return true;
        }
        return false;
    }
    
    public Map<String, String> getDataMap() {
        return data;
    }
    
    public long getFirstOnline() {
        return Long.parseLong(data.get(KEY_FIRST_ONLINE));
    }
    
    public String getLastIP() {
        return data.get(KEY_LAST_IP);
    }
    
    public long getLastOnline() {
        return Long.parseLong(data.get(KEY_LAST_ONLINE));
    }
    
    public String getName() {
        return name;
    }
    
    List<String> getRemovedDataKeys() {
        return removedData;
    }
    
    public Map<String, Object> getTransientDataMap() {
        return transientData;
    }
    
    public Object getTransientValue(String key) {
        return transientData.get(key);
    }
    
    public String getValue(String key) {
        return data.get(key);
    }
    
    public boolean hasData(String key) {
        return data.containsKey(key);
    }
    
    public boolean hasTransientData(String key) {
        return transientData.containsKey(key);
    }
    
    public boolean isFirstConnect() {
        return transientData.get("FIRST_CONNECT") != null;
    }
    
    public boolean isOnline() {
        return online;
    }
    
    public boolean putData(String key, String value) {
        if (key == null || key.isEmpty() || value == null) {
            return false;
        }
        data.put(key, value);
        return true;
    }
    
    public boolean putTransientData(String key, Object value) {
        if (key == null || key.isEmpty()) {
            return false;
        }
        transientData.put(key, value);
        return true;
    }
    
    public String removeData(String key) {
        removedData.add(key);
        return data.remove(key);
    }
    
    public Object removeTransientData(String key) {
        return transientData.remove(key);
    }
    
    void setOnline(boolean online) {
        this.online = online;
    }
}
