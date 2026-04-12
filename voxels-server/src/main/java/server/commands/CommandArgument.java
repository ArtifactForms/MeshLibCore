package server.commands;

public class CommandArgument {

  private final String name;

  private final boolean required;

  public CommandArgument(String name, boolean required) {
    this.name = name;
    this.required = required;
  }

  public String getName() {
    return name;
  }

  public boolean isRequired() {
    return required;
  }
}
