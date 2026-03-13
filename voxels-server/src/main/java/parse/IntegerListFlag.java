package parse;

import java.util.Arrays;

public class IntegerListFlag extends Flag {

  private static final String REGEX = "[\\d]{1,}(,\\d{1,}){0,}";

  private int[] values;

  public IntegerListFlag(String name) {
    super(name);
  }

  @Override
  public boolean parseArgument(String argument) {
    if (!matchesFlagPattern(argument)) return false;

    String result = argument.replace(getName(), "");
    String[] tokens = result.split(",");
    values = new int[tokens.length];

    for (int i = 0; i < tokens.length; i++) {
      try {
        values[i] = Integer.parseInt(tokens[i]);
      } catch (NumberFormatException e) {
        return false;
      }
    }

    return true;
  }

  @Override
  public boolean matchesFlagPattern(String argument) {
    return argument.matches(getName() + REGEX);
  }

  public int[] getValues() {
    return values;
  }

  @Override
  public String toString() {
    return "IntegerListFlag [values=" + Arrays.toString(values) + "]";
  }
}
