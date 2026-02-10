package engine.gfx;

public record Frame(int row, int col) {

  public static Frame at(int row, int col) {
    return new Frame(row, col);
  }
}