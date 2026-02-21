package workspace.command;

public abstract class AbstractKeyCommand implements KeyCommand {

	private char key;

	private boolean enabled;

	private String name;

	public AbstractKeyCommand() {
		setEnabled(true);
	}

	@Override
	public abstract void execute();

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public char getKey() {
		return key;
	}

	public void setKey(char key) {
		this.key = key;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
