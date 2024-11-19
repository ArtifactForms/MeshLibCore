package workspace.command;

public interface KeyCommand {

    void execute();

    String getName();

    char getKey();

    void setEnabled(boolean enabled);

    boolean isEnabled();

}
