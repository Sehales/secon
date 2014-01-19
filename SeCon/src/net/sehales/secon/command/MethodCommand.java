package net.sehales.secon.command;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;

public class MethodCommand extends TypedCommand {

	private Method executorMethod;
	private Object executorObject;

	private Method tabCompleteMethod;

	public MethodCommand(String name) {
		super(name);
	}

	public MethodCommand(String name, Object executorObject, Method executorMethod) {
		super(name);
		this.executorObject = executorObject;
		this.executorMethod = executorMethod;
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
		MethodCommand other = (MethodCommand) obj;
		if (executorMethod == null) {
			if (other.executorMethod != null)
				return false;
		} else if (!executorMethod.equals(other.executorMethod))
			return false;
		if (executorObject == null) {
			if (other.executorObject != null)
				return false;
		} else if (!executorObject.equals(other.executorObject))
			return false;
		if (tabCompleteMethod == null) {
			if (other.tabCompleteMethod != null)
				return false;
		} else if (!tabCompleteMethod.equals(other.tabCompleteMethod))
			return false;
		return true;
	}

	@Override
	protected void executeCommand(BlockCommandSender sender, String[] args) {
		try {
			executorMethod.invoke(executorObject, sender, this, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void executeCommand(CommandSender sender, String[] args) {
		try {
			executorMethod.invoke(executorObject, sender, this, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void executeCommand(ConsoleCommandSender sender, String[] args) {
		try {
			executorMethod.invoke(executorObject, sender, this, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void executeCommand(Player sender, String[] args) {
		try {
			executorMethod.invoke(executorObject, sender, this, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void executeCommand(RemoteConsoleCommandSender sender, String[] args) {
		try {
			executorMethod.invoke(executorObject, sender, this, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public Method getExecutorMethod() {
		return executorMethod;
	}

	public Object getExecutorObject() {
		return executorObject;
	}

	public Method getTabCompleteMethod() {
		return tabCompleteMethod;
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
		result = prime * result + (executorMethod == null? 0 : executorMethod.hashCode());
		result = prime * result + (executorObject == null? 0 : executorObject.hashCode());
		result = prime * result + (tabCompleteMethod == null? 0 : tabCompleteMethod.hashCode());
		return result;
	}

	public void setExecutorMethod(Method method) {
		this.executorMethod = method;
	}

	public void setExecutorObject(Object object) {
		this.executorObject = object;
	}

	public void setTabCompleteMethod(Method method) {
		this.tabCompleteMethod = method;
	}

	public List<String> superTabComplete(CommandSender sender, String alias, String[] args) {
		return super.tabComplete(sender, alias, args);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {

		if (tabCompleteMethod != null)
			try {
				Object result = tabCompleteMethod.invoke(executorObject, sender, this, alias, args);
				if (result != null && result instanceof List<?>)
					return (List<String>) result;
			} catch (Exception e) {
			}
		return super.tabComplete(sender, alias, args);
	}
}
