package net.sehales.secon.command;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;

public abstract class TypedCommand extends SeConCommand {

	private CommandType type = CommandType.ALL;

	public TypedCommand(String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TypedCommand other = (TypedCommand) obj;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	protected void execute(CommandSender sender, String[] args) {
		switch (type) {
			case PLAYER: {
				if (sender instanceof Player)
					executeCommand((Player) sender, args);
				else
					displayNotAllowed(sender);

				break;
			}

			case CONSOLE: {
				if (sender instanceof ConsoleCommandSender)
					executeCommand((ConsoleCommandSender) sender, args);
				else
					displayNotAllowed(sender);

				break;
			}

			case BLOCK: {
				if (sender instanceof BlockCommandSender)
					executeCommand((BlockCommandSender) sender, args);
				else
					displayNotAllowed(sender);

				break;
			}

			case REMOTE_CONSOLE: {
				if (sender instanceof RemoteConsoleCommandSender)
					executeCommand((RemoteConsoleCommandSender) sender, args);
				else
					displayNotAllowed(sender);

				break;
			}

			default: {
				executeCommand(sender, args);
				break;
			}
		}

	}

	protected abstract void executeCommand(BlockCommandSender sender, String[] args);

	protected abstract void executeCommand(CommandSender sender, String[] args);

	protected abstract void executeCommand(ConsoleCommandSender sender, String[] args);

	protected abstract void executeCommand(Player sender, String[] args);

	protected abstract void executeCommand(RemoteConsoleCommandSender sender, String[] args);

	public CommandType getType() {
		return type;
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
		result = prime * result + (type == null? 0 : type.hashCode());
		return result;
	}

	public void setType(CommandType type) {
		this.type = type;
	}

}
