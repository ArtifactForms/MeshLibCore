package parse;

public class StringValueFlag extends Flag {

  private String value;

  public StringValueFlag(String name) {
    super(name);
  }

  public String getValue() {
    return value;
  }

  @Override
  public boolean parseArgument(String argument) {
    if (!matchesFlagPattern(argument)) return false;

    value = argument.replaceFirst("sl", "");

    return true;
  }

  @Override
  public boolean matchesFlagPattern(String argument) {
    return argument.startsWith(getName());
  }
}
