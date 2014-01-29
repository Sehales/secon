
package net.sehales.secon.command;

import javax.script.Bindings;
import javax.script.CompiledScript;
import javax.script.ScriptException;

import net.sehales.secon.SeCon;
import net.sehales.secon.utils.MiscUtils;
import net.sehales.secon.utils.chat.ChatUtils;
import net.sehales.secon.utils.mc.ItemUtils;
import net.sehales.secon.utils.string.StringUtils;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;

public class ScriptCommand extends TypedCommand {
    
    private CompiledScript script;
    private Bindings       bindings;
    
    public ScriptCommand(String name) {
        super(name);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ScriptCommand other = (ScriptCommand) obj;
        if (script == null) {
            if (other.script != null) {
                return false;
            }
        } else if (!script.equals(other.script)) {
            return false;
        }
        return true;
    }
    
    private void eval(CommandSender sender, String[] args) {
        try {
            bindings.put("sender", sender);
            bindings.put("command", this);
            bindings.put("args", args);
            script.eval(bindings);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void executeCommand(BlockCommandSender sender, String[] args) {
        eval(sender, args);
    }
    
    @Override
    protected void executeCommand(CommandSender sender, String[] args) {
        eval(sender, args);
    }
    
    @Override
    protected void executeCommand(ConsoleCommandSender sender, String[] args) {
        eval(sender, args);
    }
    
    @Override
    protected void executeCommand(Player sender, String[] args) {
        eval(sender, args);
    }
    
    @Override
    protected void executeCommand(RemoteConsoleCommandSender sender, String[] args) {
        eval(sender, args);
    }
    
    public CompiledScript getScript() {
        return script;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (script == null ? 0 : script.hashCode());
        return result;
    }
    
    public void setScript(CompiledScript script) {
        this.script = script;
        bindings = script.getEngine().createBindings();
        bindings.put("secon", SeCon.getInstance());
        bindings.put("command", this);
        bindings.put("ChatUtils", new ChatUtils());
        bindings.put("MiscUtils", new MiscUtils());
        bindings.put("ItemUtils", new ItemUtils());
        bindings.put("StringUtils", new StringUtils());
        // bindings.put("FontUtils", new TextUtils());
    }
    
}
