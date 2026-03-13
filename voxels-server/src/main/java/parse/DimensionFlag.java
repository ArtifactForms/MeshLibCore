package parse;

public class DimensionFlag extends Flag {

  private static final String REGEX_DIMENSION = "[\\d]{1,}(,\\d{1,}){2}";

  private int width;
  private int height;
  private int depth;

  public DimensionFlag(String name) {
    super(name);
  }

  @Override
  public boolean parseArgument(String argument) {
    if (!matchesFlagPattern(argument)) {
      return false;
    }

    String result = argument.replace(getName(), "");
    return setFromStringValues(result.split(","));
  }

  @Override
  public boolean matchesFlagPattern(String argument) {
    return argument.matches(getName() + REGEX_DIMENSION);
  }

  private boolean setFromStringValues(String[] values) {
    try {
      width = Integer.parseInt(values[0]);
      height = Integer.parseInt(values[1]);
      depth = Integer.parseInt(values[2]);
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public int getDepth() {
    return depth;
  }

  @Override
  public String toString() {
    return "DimensionFlag [width=" + width + ", height=" + height + ", depth=" + depth + "]";
  }
}
