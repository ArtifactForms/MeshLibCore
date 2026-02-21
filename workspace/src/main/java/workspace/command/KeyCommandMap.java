package workspace.command;

import java.util.HashMap;

public class KeyCommandMap {

	private HashMap<Character, KeyCommand> commands;

	public KeyCommandMap() {
		this.commands = new HashMap<Character, KeyCommand>();
	}

	public void register(KeyCommand command) {
		commands.put(command.getKey(), command);
	}

	public void execute(char key) {
		if (!commands.containsKey(key))
			return;

		KeyCommand command = commands.get(key);

		if (command.isEnabled())
			command.execute();
	}

	public KeyCommand getCommand(char key) {
		return commands.get(key);
	}

}
