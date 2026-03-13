package parse;

public class IntegerValueFlag extends Flag {

  private static final String REGEX = "[-]?[\\d]{1,}";

  private int value;

  public IntegerValueFlag(String name) {
    super(name);
  }

  @Override
  public boolean parseArgument(String argument) {
    if (!matchesFlagPattern(argument)) return false;

    String result = argument.replace(getName(), "");
    try {
      value = Integer.parseInt(result);
    } catch (NumberFormatException e) {
      return false;
    }

    return true;
  }

  @Override
  public boolean matchesFlagPattern(String argument) {
    return argument.matches(getName() + REGEX);
  }

  public int getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "IntegerValueFlag [value=" + value + "]";
  }
}
