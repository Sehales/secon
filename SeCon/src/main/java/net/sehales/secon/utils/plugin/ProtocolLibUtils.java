
package net.sehales.secon.utils.plugin;

import java.lang.reflect.InvocationTargetException;

import net.sehales.secon.SeCon;

import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

public class ProtocolLibUtils {
    
    private ProtocolManager mngr;
    
    ProtocolLibUtils() {
        mngr = ProtocolLibrary.getProtocolManager();
    }
    
    /**
     * send a json encoded message to a player
     * 
     * @param player
     *            the message receiver
     * @param message
     *            the json encoded message
     */
    public void sendJsonMessage(Player player, String message) {
        PacketContainer msgPacket = mngr.createPacket(PacketType.Play.Server.CHAT);
        msgPacket.getChatComponents().write(0, WrappedChatComponent.fromJson(message));
        try {
            mngr.sendServerPacket(player, msgPacket);
        } catch (InvocationTargetException e) {
            SeCon.getInstance().log().severe(null, "Failed to send chat packet.");
            e.printStackTrace();
        }
    }
}
