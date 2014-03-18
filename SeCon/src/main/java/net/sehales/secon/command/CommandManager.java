
package net.sehales.secon.command;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.script.Compilable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import net.sehales.secon.SeCon;
import net.sehales.secon.command.ScriptCmdDescription.Description;
import net.sehales.secon.command.ScriptCmdDescription.ScriptType;
import net.sehales.secon.config.CommandConfig;
import net.sehales.secon.utils.java.ReflectionUtils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;

public class CommandManager {
    
    private CommandConfig        cmdConfig;
    private List<Command>        scriptCommandList          = new ArrayList<>();
    
    private CommandMap           bukkitCmdMap;
    private Map<String, Command> knownCommands;
    private Compilable           jsEngine;
    
    private SeCon                secon;
    private static final String  FALLBACK_PREFIX            = "secon";
    
    private static final char    DOT                        = '.';
    private static final String  PATH_ALIASES               = ".aliases";
    private static final String  PATH_DESCRIPTION           = ".description";
    private static final String  PATH_USAGE                 = ".usage";
    private static final String  PATH_ADDITIONAL_PERMISSION = ".additional-permissions";
    private static final String  PATH_PERMISSION            = ".permission";
    private static final String  PATH_STATE                 = ".state";
    private static final String  PATH_NAME                  = ".name";
    private static final String  NO_TAB_METHOD              = "none";
    
    @SuppressWarnings("unchecked")
    public CommandManager(SeCon secon) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        this.secon = secon;
        this.cmdConfig = secon.getCmdConfig();
        Field cmF = Bukkit.getServer().getClass().getDeclaredField("commandMap");
        cmF.setAccessible(true);
        
        bukkitCmdMap = (CommandMap) ReflectionUtils.getDeclaredFieldValue(Bukkit.getServer(), "commandMap");
        knownCommands = (Map<String, Command>) ReflectionUtils.getDeclaredFieldValue(SimpleCommandMap.class, bukkitCmdMap, "knownCommands");
        
        jsEngine = loadScriptEngine("javascript");
        if (jsEngine == null) {
            secon.log().warning("CommandManager", "Can't load JavaScript engine. JavaScript support is disabled.");
        }
        
        SeCon.scriptFolder.mkdirs();
    }
    
    public ScriptCommand createScriptCommand(String name, CommandType cmdType, ScriptType scriptType, Reader scriptReader) {
        Compilable engine = null;
        switch (scriptType) {
            case JAVASCRIPT: {
                engine = jsEngine;
                break;
            }
        }
        
        if (engine == null) {
            return null;
        }
        ScriptCommand cmd = new ScriptCommand(name);
        try {
            cmd.setScript(engine.compile(scriptReader));
        } catch (ScriptException e) {
            secon.log().warning("CommandManager", "Error while compiling process");
            e.printStackTrace();
            return null;
        }
        
        return cmd;
    }
    
    public Command getCommand(String name) {
        return knownCommands.get(name);
    }
    
    public Map<String, Command> getCommandMap() {
        return knownCommands;
    }
    
    public List<ScriptCommand> loadScriptCommands(File folder) {
        if (!folder.isDirectory()) {
            return Collections.emptyList();
        }
        
        secon.log().info("CommandManager", String.format("Loading script commands from folder '%s'", folder.getAbsolutePath()));
        File descFile = null;
        for (File f : folder.listFiles(new FilenameFilter() {
            
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".cmddesc");
            }
        })) {
            descFile = f;
            break;
        }
        
        if (descFile == null) {
            return Collections.emptyList();
        }
        List<ScriptCommand> cmdList = new ArrayList<>();
        ScriptCmdDescription desc = ScriptCmdDescription.readFromFile(descFile);
        for (Description d : desc.getDescriptionList()) {
            try {
                ScriptCommand cmd = createScriptCommand(d.getCmdName(), d.getCmdType(), d.getScriptType(), new FileReader(d.getFile()));
                cmd.setAliases(d.getAliases());
                cmd.setUsage(d.getUsage());
                cmd.setDescription(d.getCmdDesc());
                cmd.setOverrideHelp(d.isOverridingHelp());
                cmd.setPermission(d.getPermission());
                cmd.setType(d.getCmdType());
                
                if (registerCommand(FALLBACK_PREFIX, cmd)) {
                    cmdList.add(cmd);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return cmdList;
        
    }
    
    private Compilable loadScriptEngine(String engineName) {
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine engine = sem.getEngineByName(engineName);
        if (engine == null || !(engine instanceof Compilable)) {
            return null;
        }
        
        return (Compilable) engine;
    }
    
    public boolean registerCommand(String fallbackPrefix, Command cmd) {
        synchronized (scriptCommandList) {
            boolean result = bukkitCmdMap.register(fallbackPrefix, cmd);
            if (result) {
                if (cmd instanceof ScriptCommand) {
                    scriptCommandList.add(cmd);
                }
            }
            return result;
        }
    }
    
    public synchronized List<Command> registerCommandsFromObject(String categoryName, Object obj) {
        if (secon.isDebugEnabled()) {
            System.out.println("BEGIN CommandManager.registerCommandsFromObject()");
        }
        List<Command> cmdList = new ArrayList<>();
        
        Class<?> objectClass = obj.getClass();
        for (Method method : objectClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(MethodCommandHandler.class)) {
                MethodCommandHandler mch = method.getAnnotation(MethodCommandHandler.class);
                
                List<String> aliases = Arrays.asList(mch.aliases());
                List<String> additionalPerms = Arrays.asList(mch.additionalPerms());
                
                String cmdName = mch.name().toLowerCase();
                String usage = mch.usage();
                String description = mch.description();
                String permission = mch.permission();
                
                if (secon.isDebugEnabled()) {
                    System.out.println("BEGIN BEFORE CONFIG");
                    System.out.println("name: " + cmdName);
                    System.out.println("usage: " + usage);
                    System.out.println("description: " + description);
                    System.out.println("permission: " + permission);
                    System.out.println("aliases: " + aliases.toString());
                    System.out.println("add perms: " + additionalPerms.toString());
                    System.out.println("END BEFORE CONFIG");
                }
                
                String path = categoryName + DOT + cmdName;
                String namePath = path + PATH_NAME;
                String usagePath = path + PATH_USAGE;
                String descriptionPath = path + PATH_DESCRIPTION;
                String permissionPath = path + PATH_PERMISSION;
                String statePath = path + PATH_STATE;
                String additionalPermsPath = path + PATH_ADDITIONAL_PERMISSION;
                String aliasesPath = path + PATH_ALIASES;
                
                CmdState state = CmdState.ENABLED;
                
                if (cmdConfig.contains(namePath)) {
                    cmdName = cmdConfig.getString(namePath);
                } else {
                    cmdConfig.set(namePath, cmdName);
                }
                
                if (cmdConfig.contains(usagePath)) {
                    usage = cmdConfig.getString(usagePath);
                } else {
                    cmdConfig.set(usagePath, usage);
                }
                
                if (cmdConfig.contains(descriptionPath)) {
                    description = cmdConfig.getString(descriptionPath);
                } else {
                    cmdConfig.set(descriptionPath, description);
                }
                
                if (cmdConfig.contains(permissionPath)) {
                    permission = cmdConfig.getString(permissionPath);
                } else {
                    cmdConfig.set(permissionPath, permission);
                }
                
                if (cmdConfig.contains(statePath)) {
                    state = CmdState.valueOf(cmdConfig.getString(statePath));
                } else {
                    cmdConfig.set(statePath, state.toString());
                }
                
                if (cmdConfig.contains(aliasesPath)) {
                    aliases = cmdConfig.getStringList(aliasesPath);
                } else {
                    cmdConfig.set(aliasesPath, aliases);
                }
                
                if (cmdConfig.contains(additionalPermsPath)) {
                    additionalPerms = cmdConfig.getStringList(additionalPermsPath);
                } else {
                    cmdConfig.set(additionalPermsPath, additionalPerms);
                }
                
                switch (state) {
                    case DISABLED:
                        continue;
                    default:
                        break;
                }
                
                MethodCommand cmd = new MethodCommand(cmdName, obj, method);
                String tabMethodName = mch.tabExecutorMethod();
                if (tabMethodName != null && !tabMethodName.isEmpty() && !tabMethodName.equalsIgnoreCase(NO_TAB_METHOD)) {
                    try {
                        Method tabMethod = objectClass.getDeclaredMethod(tabMethodName, CommandSender.class, MethodCommand.class, String.class, String[].class);
                        cmd.setTabCompleteMethod(tabMethod);
                    } catch (NoSuchMethodException e) {
                        secon.log().info("CommandManager", String.format("Can't find method '%s' in class '%s'", tabMethodName, objectClass.getCanonicalName()));
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }
                
                if (secon.isDebugEnabled()) {
                    System.out.println("BEGIN AFTER CONFIG");
                    System.out.println("name: " + cmdName);
                    System.out.println("usage: " + usage);
                    System.out.println("description: " + description);
                    System.out.println("permission: " + permission);
                    System.out.println("aliases: " + aliases.toString());
                    System.out.println("add perms: " + additionalPerms.toString());
                    System.out.println("END AFTER CONFIG");
                }
                
                cmd.setAliases(aliases);
                cmd.setUsage(usage);
                cmd.setDescription(description);
                cmd.setType(mch.type());
                cmd.setPermission(permission);
                cmd.setOverrideHelp(mch.overrideHelp());
                
                for (String s : additionalPerms) {
                    String[] permInfo = s.split(":");
                    if (permInfo.length == 2) {
                        cmd.setAdditionalPermssion(permInfo[0], permInfo[1]);
                    }
                }
                
                if (registerCommand(FALLBACK_PREFIX, cmd)) {
                    cmdList.add(cmd);
                }
            }
        }
        cmdConfig.save();
        
        if (secon.isDebugEnabled()) {
            System.out.println("END CommandManager.registerCommandsFromObject()");
        }
        
        return cmdList;
    }
    
    public void unregisterCommand(Command cmd) {
        synchronized (scriptCommandList) {
            synchronized (knownCommands) {
                if (cmd instanceof ScriptCommand) {
                    scriptCommandList.remove(cmd);
                }
                
                if (cmd.equals(getCommand(cmd.getName()))) {
                    knownCommands.remove(cmd.getName());
                }
                
                for (String alias : cmd.getAliases()) {
                    
                    if (knownCommands.containsKey(alias) && knownCommands.get(alias).equals(cmd)) {
                        knownCommands.remove(alias);
                    }
                }
            }
        }
    }
    
    public void unregisterCommandsFromObject(Object obj) {
        for (Command command : knownCommands.values().toArray(new Command[0])) {
            if (command instanceof MethodCommand) {
                MethodCommand cmd = (MethodCommand) command;
                if (cmd.getExecutorObject().equals(obj)) {
                    unregisterCommand(cmd);
                }
            }
        }
    }
    
    public void unregisterScriptCommands() {
        for (Command cmd : scriptCommandList.toArray(new Command[0])) {
            unregisterCommand(cmd);
        }
    }
}
