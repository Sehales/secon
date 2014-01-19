
package net.sehales.secon.utils.mc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatUtils {
    
    private static final Pattern       PATTERN_ON_SPACE = Pattern.compile(" ", Pattern.LITERAL);
    private static Map<String, String> colorReplaceMap  = new HashMap<>();
    private static Map<String, String> formatReplaceMap = new HashMap<>();
    private static Map<String, String> markupReplaceMap = new HashMap<>();
    
    static {
        // color codes
        colorReplaceMap.put("<black>", ChatColor.BLACK.toString());
        colorReplaceMap.put("&0", ChatColor.BLACK.toString());
        colorReplaceMap.put("<darkblue>", ChatColor.DARK_BLUE.toString());
        colorReplaceMap.put("&1", ChatColor.DARK_BLUE.toString());
        colorReplaceMap.put("<darkgreen>", ChatColor.DARK_GREEN.toString());
        colorReplaceMap.put("&2", ChatColor.DARK_GREEN.toString());
        colorReplaceMap.put("<darkaqua>", ChatColor.DARK_AQUA.toString());
        colorReplaceMap.put("&3", ChatColor.DARK_AQUA.toString());
        colorReplaceMap.put("<darkred>", ChatColor.DARK_RED.toString());
        colorReplaceMap.put("&4", ChatColor.DARK_RED.toString());
        colorReplaceMap.put("<darkpurple>", ChatColor.DARK_PURPLE.toString());
        colorReplaceMap.put("&5", ChatColor.DARK_PURPLE.toString());
        colorReplaceMap.put("<gold>", ChatColor.GOLD.toString());
        colorReplaceMap.put("&6", ChatColor.GOLD.toString());
        colorReplaceMap.put("<grey>", ChatColor.GRAY.toString());
        colorReplaceMap.put("<gray>", ChatColor.GRAY.toString());
        colorReplaceMap.put("&7", ChatColor.GRAY.toString());
        colorReplaceMap.put("<darkgrey>", ChatColor.DARK_GRAY.toString());
        colorReplaceMap.put("<darkgray>", ChatColor.DARK_GRAY.toString());
        colorReplaceMap.put("&8", ChatColor.DARK_GRAY.toString());
        colorReplaceMap.put("<blue>", ChatColor.BLUE.toString());
        colorReplaceMap.put("&9", ChatColor.BLUE.toString());
        colorReplaceMap.put("<green>", ChatColor.GREEN.toString());
        colorReplaceMap.put("&a", ChatColor.GREEN.toString());
        colorReplaceMap.put("<aqua>", ChatColor.AQUA.toString());
        colorReplaceMap.put("&b", ChatColor.AQUA.toString());
        colorReplaceMap.put("<red>", ChatColor.RED.toString());
        colorReplaceMap.put("&c", ChatColor.RED.toString());
        colorReplaceMap.put("<purple>", ChatColor.LIGHT_PURPLE.toString());
        colorReplaceMap.put("&d", ChatColor.LIGHT_PURPLE.toString());
        colorReplaceMap.put("<yellow>", ChatColor.YELLOW.toString());
        colorReplaceMap.put("&e", ChatColor.YELLOW.toString());
        colorReplaceMap.put("<white>", ChatColor.WHITE.toString());
        colorReplaceMap.put("&f", ChatColor.WHITE.toString());
        
        // formatting codes
        formatReplaceMap.put("<bold>", ChatColor.BOLD.toString());
        formatReplaceMap.put("&l", ChatColor.BOLD.toString());
        formatReplaceMap.put("<italic>", ChatColor.ITALIC.toString());
        formatReplaceMap.put("&o", ChatColor.ITALIC.toString());
        formatReplaceMap.put("<magic>", ChatColor.MAGIC.toString());
        formatReplaceMap.put("&k", ChatColor.MAGIC.toString());
        formatReplaceMap.put("<strikethrough>", ChatColor.STRIKETHROUGH.toString());
        formatReplaceMap.put("&m", ChatColor.STRIKETHROUGH.toString());
        formatReplaceMap.put("<underline>", ChatColor.UNDERLINE.toString());
        formatReplaceMap.put("&n", ChatColor.UNDERLINE.toString());
        formatReplaceMap.put("<normal>", ChatColor.RESET.toString());
        formatReplaceMap.put("&r", ChatColor.RESET.toString());
        formatReplaceMap.put("<resettext>", ChatColor.RESET.toString());
        
        // markup codes
        markupReplaceMap.put("&amp;", "&");
        markupReplaceMap.put("&gt;", ">");
        markupReplaceMap.put("&lt;", "<");
    }
    
    /**
     * broadcast a server wide formatted message
     * 
     * @param message
     */
    public static void broadcastFormattedMessage(List<String> messageList) {
        for (String s : messageList) {
            Bukkit.broadcastMessage(formatMessage(s));
        }
    }
    
    /**
     * broadcast a world wide list of formatted messages
     * 
     * @param messageList
     * @param worldName
     * @return true if the world exists
     */
    public static boolean broadcastFormattedMessage(List<String> messageList, String worldName) {
        World w;
        if ((w = Bukkit.getWorld(worldName)) != null) {
            broadcastFormattedMessage(messageList, w);
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * broadcast a world wide list of formatted messages
     * 
     * @param messageList
     * @param world
     */
    public static void broadcastFormattedMessage(List<String> messageList, World world) {
        for (Player p : world.getPlayers()) {
            sendFormattedMessage(p, messageList);
        }
        sendFormattedMessage(Bukkit.getConsoleSender(), messageList);
    }
    
    /**
     * broadcast a server wide formatted message
     * 
     * @param message
     */
    public static void broadcastFormattedMessage(String message) {
        Bukkit.broadcastMessage(formatMessage(message));
    }
    
    /**
     * broadcast a world wide formatted message
     * 
     * @param message
     * @param worldName
     * @return true if the world exists
     */
    public static boolean broadcastFormattedMessage(String message, String worldName) {
        World w;
        if ((w = Bukkit.getWorld(worldName)) != null) {
            broadcastFormattedMessage(message, w);
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * broadcast a world wide formatted message
     * 
     * @param message
     * @param world
     */
    public static void broadcastFormattedMessage(String message, World world) {
        for (Player p : world.getPlayers()) {
            sendFormattedMessage(p, message);
        }
    }
    
    /**
     * format the message and replace color-codes/formating-codes
     * 
     * @param message
     * @return formatted message
     */
    public static String formatMessage(String message) {
        
        message = parseColorCodes(message);
        message = parseFormatCodes(message);
        message = parseMarkupCodes(message);
        
        return message;
    }
    
    /**
     * get an array splitted at spaces from a string
     * 
     * @param string
     * @return a string-array
     */
    public static String[] getArrayOutOfString(String string) {
        return PATTERN_ON_SPACE.split(string);
    }
    
    public static String[] getArrayOutOfString(String string, String splitRegEx) {
        return string.split(splitRegEx);
    }
    
    /**
     * get a string out of the given array, starting at the given position
     * 
     * @param array
     * @param position
     * @return string
     */
    public static String getStringOfArray(String[] array, int position) {
        StringBuilder sb = new StringBuilder();
        for (int i = position; i < array.length; i++) {
            sb.append(array[i]);
            sb.append(" ");
        }
        return sb.substring(0, sb.length() - 1);
    }
    
    /**
     * format the message and replace color-codes
     * 
     * @param format
     * @return formatted string
     */
    public static String parseColorCodes(String format) {
        for (Map.Entry<String, String> entry : colorReplaceMap.entrySet()) {
            format = format.replace(entry.getKey(), entry.getValue());
        }
        return format;
    }
    
    /**
     * format the message and replace formatting-codes
     * 
     * @param format
     * @return formatted String
     */
    public static String parseFormatCodes(String format) {
        for (Map.Entry<String, String> entry : formatReplaceMap.entrySet()) {
            format = format.replace(entry.getKey(), entry.getValue());
        }
        return format;
    }
    
    /**
     * format the message and replace the special markup chars: &amp; --> &,
     * &gt; --> >, &lt; --> <
     * 
     * @param format
     * @return formatted String
     */
    public static String parseMarkupCodes(String format) {
        for (Map.Entry<String, String> entry : markupReplaceMap.entrySet()) {
            format = format.replace(entry.getKey(), entry.getValue());
        }
        return format;
    }
    
    /**
     * sends a formatted message to the given sender
     * 
     * @param sender
     * @param messageList
     */
    public static void sendFormattedMessage(CommandSender sender, List<String> messageList) {
        for (String message : messageList) {
            sendFormattedMessage(sender, message);
        }
    }
    
    /**
     * sends a formatted message to the given sender
     * 
     * @param sender
     * @param message
     */
    public static void sendFormattedMessage(CommandSender sender, String message) {
        message = formatMessage(message);
        sender.sendMessage(message);
    }
    
    /**
     * send a list of formatted messages to multiple players
     * 
     * @param senderList
     * @param messageList
     */
    public static void sendFormattedMessage(List<CommandSender> senderList, List<String> messageList) {
        for (CommandSender c : senderList) {
            sendFormattedMessage(c, messageList);
        }
    }
    
    /**
     * send a formatted message to multiple players
     * 
     * @param senderList
     * @param message
     */
    public static void sendFormattedMessage(List<CommandSender> senderList, String message) {
        for (CommandSender c : senderList) {
            sendFormattedMessage(c, message);
        }
    }
}
