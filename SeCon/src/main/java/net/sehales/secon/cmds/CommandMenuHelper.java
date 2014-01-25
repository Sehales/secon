
package net.sehales.secon.cmds;


public class CommandMenuHelper {
    
    // TODO: complete rework needed
    // public static List<MenuItem> createCommandList(Player player) {
    //
    // Map<String, Object> commands = new TreeMap<String, Object>();
    //
    // commands.putAll(SeCon.getCommandManager().getCommandMap());
    //
    // for (Plugin plugin : Bukkit.getPluginManager().getPlugins())
    // if (plugin.getDescription().getCommands() != null)
    // for (String cmdName : plugin.getDescription().getCommands().keySet())
    // if (cmdName != null) {
    // Command cmd = Bukkit.getPluginCommand(cmdName);
    // commands.put(cmdName, cmd);
    // }
    //
    // List<MenuItem> items = new ArrayList<MenuItem>();
    //
    // for (String cmdName : commands.keySet()) {
    //
    // Object rawCmd = commands.get(cmdName);
    // if (rawCmd instanceof Command) {
    //
    // Command cmd = (Command) rawCmd;
    //
    // if (cmd.getPermission() != null)
    // if (!player.hasPermission(cmd.getPermission()))
    // continue;
    //
    // items.add(new CommandMenuItem(cmd));
    //
    // } else if (rawCmd instanceof OldSeConCommand) {
    // OldSeConCommand cmd = (OldSeConCommand) rawCmd;
    //
    // if (cmd.getPermission() != null)
    // if (!player.hasPermission(cmd.getPermission()))
    // continue;
    //
    // items.add(new CommandMenuItem(cmd));
    // }
    // }
    // return items;
    // }
}
