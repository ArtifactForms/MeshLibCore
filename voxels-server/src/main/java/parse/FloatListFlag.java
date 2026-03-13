package parse;

import java.util.Arrays;

public class FloatListFlag extends Flag {

  private static final String REGEX = "[\\d]{1,}(,\\d{1,}){0,}";

  //	[-]?[\\d]{1,}.{0,1}[\\d]{0,}

  private float[] values;

  public FloatListFlag(String name) {
    super(name);
  }

  @Override
  public boolean parseArgument(String argument) {
    if (!matchesFlagPattern(argument)) return false;

    String result = argument.replace(getName(), "");
    String[] tokens = result.split(",");
    values = new float[tokens.length];

    for (int i = 0; i < tokens.length; i++) {
      try {
        values[i] = Float.parseFloat(tokens[i]);
      } catch (NumberFormatException e) {
        return false;
      }
    }

    return true;
  }

  @Override
  public boolean matchesFlagPattern(String argument) {
    //		return argument.matches(getName() + REGEX);
    return true;
  }

  public float[] getValues() {
    return values;
  }

  @Override
  public String toString() {
    return "FloatListFlag [values=" + Arrays.toString(values) + "]";
  }
}
