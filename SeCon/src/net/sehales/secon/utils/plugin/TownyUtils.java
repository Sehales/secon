
package net.sehales.secon.utils.plugin;

import com.palmergames.bukkit.towny.TownySettings;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownyUniverse;

public class TownyUtils {
    
    TownyUtils() {
        
    }
    
    public String getNationName(String playerName) {
        try {
            Resident res = TownyUniverse.getDataSource().getResident(playerName);
            if (res.hasNation()) {
                return res.getTown().getNation().getName();
            }
        } catch (NotRegisteredException e) {
            return "";
        }
        return "";
    }
    
    public String getNationTag(String playerName) {
        try {
            Resident res = TownyUniverse.getDataSource().getResident(playerName);
            if (res.hasNation()) {
                if (res.getTown().getNation().hasTag()) {
                    return res.getTown().getNation().getTag();
                } else {
                    return "";
                }
            }
        } catch (NotRegisteredException e) {
            return "";
        }
        return "";
    }
    
    public String getSurname(String playerName) {
        Resident res;
        try {
            res = TownyUniverse.getDataSource().getResident(playerName);
            if (res.isKing()) {
                if (res.hasSurname()) {
                    return res.getSurname();
                } else {
                    return TownySettings.getKingPostfix(res);
                }
                
            } else if (res.isMayor()) {
                if (res.hasSurname()) {
                    return res.getSurname();
                } else {
                    return TownySettings.getMayorPostfix(res);
                }
            } else if (res.hasSurname()) {
                return res.getSurname();
            } else {
                return "";
            }
        } catch (NotRegisteredException e) {
            return "";
        }
    }
    
    public String getTitle(String playerName) {
        Resident res;
        try {
            res = TownyUniverse.getDataSource().getResident(playerName);
            if (res.isKing()) {
                if (res.hasTitle()) {
                    return res.getTitle();
                } else {
                    return TownySettings.getKingPrefix(res);
                }
                
            } else if (res.isMayor()) {
                if (res.hasTitle()) {
                    return res.getTitle();
                } else {
                    return TownySettings.getMayorPrefix(res);
                }
            } else if (res.hasTitle()) {
                return res.getTitle();
            } else {
                return "";
            }
        } catch (NotRegisteredException e) {
            return "";
        }
    }
    
    public String getTownName(String playerName) {
        try {
            Resident res = TownyUniverse.getDataSource().getResident(playerName);
            if (res.hasTown()) {
                return res.getTown().getName();
            } else {
                return "";
            }
        } catch (NotRegisteredException e) {
            return "";
        }
    }
    
    public String getTownTag(String playerName) {
        try {
            Resident res = TownyUniverse.getDataSource().getResident(playerName);
            if (res.hasTown()) {
                if (res.getTown().hasTag()) {
                    return res.getTown().getTag();
                } else {
                    return "";
                }
            }
        } catch (NotRegisteredException e) {
            return "";
        }
        return "";
    }
    
    public boolean hasNation(String playerName) {
        try {
            Resident res = TownyUniverse.getDataSource().getResident(playerName);
            if (res.hasNation()) {
                return true;
            }
        } catch (NotRegisteredException e) {
            return false;
        }
        return false;
    }
    
    public boolean hasSurname(String playerName) {
        try {
            Resident res = TownyUniverse.getDataSource().getResident(playerName);
            if (res.hasSurname() || res.isKing() || res.isMayor()) {
                return true;
            }
        } catch (NotRegisteredException e) {
            return false;
        }
        return false;
    }
    
    public boolean hasTitle(String playerName) {
        try {
            Resident res = TownyUniverse.getDataSource().getResident(playerName);
            if (res.hasTitle() || res.isKing() || res.isMayor()) {
                return true;
            }
        } catch (NotRegisteredException e) {
            return false;
        }
        return false;
    }
    
    public boolean hasTown(String playerName) {
        try {
            Resident res = TownyUniverse.getDataSource().getResident(playerName);
            if (res.hasTown()) {
                return true;
            }
        } catch (NotRegisteredException e) {
            return false;
        }
        return false;
    }
    
    public boolean isWorldUsingTowny(String worldName) {
        try {
            if (TownyUniverse.getDataSource().getWorld(worldName).isUsingTowny()) {
                return true;
            }
        } catch (NotRegisteredException e) {
            return false;
        }
        return false;
    }
}
