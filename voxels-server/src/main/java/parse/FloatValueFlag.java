package parse;

public class FloatValueFlag extends Flag {

  private static final String REGEX = "[-]?[\\d]{1,}.{0,1}[\\d]{0,}";

  private float value;

  public FloatValueFlag(String name) {
    super(name);
  }

  @Override
  public boolean parseArgument(String argument) {
    if (!matchesFlagPattern(argument)) return false;

    String result = argument.replace(getName(), "");
    try {
      value = Float.parseFloat(result);
    } catch (NumberFormatException e) {
      return false;
    }

    return true;
  }

  @Override
  public boolean matchesFlagPattern(String argument) {
    return argument.matches(getName() + REGEX);
  }

  public float getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "FloatValueFlag [value=" + value + "]";
  }
}
