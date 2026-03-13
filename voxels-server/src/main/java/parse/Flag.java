package parse;

public abstract class Flag {

  private String name;

  public abstract boolean parseArgument(String argument);

  public abstract boolean matchesFlagPattern(String argument);

  public boolean argumentStartsWithFlag(String argument) {
    return argument.startsWith(getName());
  }

  public Flag(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
