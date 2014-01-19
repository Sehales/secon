package net.sehales.secon.command;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class ScriptCmdDescription {

	public class Description {

		private String       cmdName;
		private ScriptType   type;
		private File         file;
		private List<String> aliases;
		private String       permission;
		private CommandType  cmdType;
		private String       cmdDesc;
		private String       usage;
		private boolean      overrideHelp;

		Description(String cmdName, CommandType cmdType, ScriptType type, File file, List<String> aliases, String permission, String cmdDescription, String usage, boolean overrideHelp) {
			this.cmdName = cmdName;
			this.type = type;
			this.cmdType = cmdType;
			this.file = file;
			this.aliases = aliases;
			this.cmdDesc = cmdDescription;
			this.usage = usage;
			this.overrideHelp = overrideHelp;
			this.permission = permission;
		}

		public List<String> getAliases() {
			return aliases;
		}

		public String getCmdDesc() {
			return cmdDesc;
		}

		public String getCmdName() {
			return cmdName;
		}

		public CommandType getCmdType() {
			return cmdType;
		}

		public File getFile() {
			return file;
		}

		public String getPermission() {
			return permission;
		}

		public ScriptType getScriptType() {
			return type;
		}

		public String getUsage() {
			return usage;
		}

		public boolean isOverridingHelp() {
			return overrideHelp;
		}
	}

	public enum ScriptType {
		JAVASCRIPT
	}

	public static ScriptCmdDescription readFromFile(File file) {
		ScriptCmdDescription desc = new ScriptCmdDescription(file);
		return desc;
		//		return null;
	}

	private List<Description>   descriptions      = new ArrayList<>();

	private static final String STR_TYPE          = "script-type";
	private static final String STR_CMD_TYPE      = "command-type";
	private static final String STR_FILE_NAME     = "file-name";
	private static final String STR_ALIASES       = "aliases";
	private static final String STR_PERMISSION    = "permission";
	private static final String STR_DESCRIPTION   = "description";
	private static final String STR_USAGE         = "usage";
	private static final String STR_OVERRIDE_HELP = "override-help";

	private ScriptCmdDescription(File file) {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		for (String cmdName : config.getKeys(false)) {
			ConfigurationSection section = config.getConfigurationSection(cmdName);

			ScriptType scriptType;
			try {
				scriptType = ScriptType.valueOf(section.getString(STR_TYPE).toUpperCase());
			} catch (Exception e) {
				throw new IllegalArgumentException(String.format("Invalid or missing script type, at key '%s'", cmdName));
			}

			CommandType cmdType;
			try {
				cmdType = CommandType.valueOf(section.getString(STR_CMD_TYPE).toUpperCase());
			} catch (Exception e) {
				throw new IllegalArgumentException(String.format("Invalid or missing command type, at key '%s'", cmdName));
			}

			String fileName = section.getString(STR_FILE_NAME);
			if (fileName == null || fileName.isEmpty())
				throw new IllegalArgumentException(String.format("Missing file name, at key '%s'", cmdName));

			File scriptFile = new File(file.getParent(), fileName);
			if (scriptFile == null || !scriptFile.exists())
				throw new IllegalArgumentException(String.format("File is null or does not exist, at key '%s'", cmdName));

			List<String> aliases = Collections.emptyList();
			if (section.contains(STR_ALIASES)) {
				String rawAliases = section.getString(STR_ALIASES);
				if (!rawAliases.isEmpty())
					aliases = Arrays.asList(rawAliases.split(","));
			}

			String permission = "op";
			if (section.contains(STR_PERMISSION))
				permission = section.getString(STR_PERMISSION);

			String description = null;
			if (section.contains(STR_DESCRIPTION))
				description = section.getString(STR_DESCRIPTION);

			String usage = null;
			if (section.contains(STR_USAGE))
				usage = section.getString(STR_USAGE);

			boolean overrideHelp = false;
			if (section.contains(STR_OVERRIDE_HELP))
				overrideHelp = section.getBoolean(STR_OVERRIDE_HELP);

			descriptions.add(new Description(cmdName, cmdType, scriptType, scriptFile, aliases, permission, description, usage, overrideHelp));
		}
	}

	public List<Description> getDescriptionList() {
		return descriptions;
	}
}
