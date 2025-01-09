package engine.vbo;

public class VBOFactory {

  private static VBOFactory instance;
  private static VBOCreationStrategy strategy;

  private VBOFactory() {}

  public static VBOFactory getInstance() {
    if (instance == null) {
      instance = new VBOFactory();
    }
    return instance;
  }

  public void setVBOCreationStrategy(VBOCreationStrategy strategy) {
    VBOFactory.strategy = strategy;
  }

  public VBO create() {
    if (strategy == null) {
      System.err.println("No VBOCreationStrategy set!");
      return null;
    }
    return strategy.create();
  }
}
