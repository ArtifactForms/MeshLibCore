package parse;

public class FlagWithoutArguments extends Flag {

  private String value;

  public FlagWithoutArguments(String name) {
    super(name);
  }

  @Override
  public boolean parseArgument(String argument) {
    value = new String(argument);
    return true;
  }

  @Override
  public boolean matchesFlagPattern(String argument) {
    return argument.equals(getName());
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "FlagWithoutArguments [value=" + value + "]";
  }
}
