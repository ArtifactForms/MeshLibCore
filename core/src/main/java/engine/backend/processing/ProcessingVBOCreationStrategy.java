package engine.backend.processing;

import engine.vbo.VBO;
import engine.vbo.VBOCreationStrategy;
import processing.core.PGraphics;

public class ProcessingVBOCreationStrategy implements VBOCreationStrategy {

  private PGraphics graphics;

  public ProcessingVBOCreationStrategy(PGraphics graphics) {
    this.graphics = graphics;
  }

  @Override
  public VBO create() {
    return new VBOProcessing(graphics);
  }
}
