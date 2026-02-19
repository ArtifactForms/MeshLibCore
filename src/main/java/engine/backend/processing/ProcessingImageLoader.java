package engine.backend.processing;

import engine.resources.ImageLoader;
import processing.core.PApplet;

public class ProcessingImageLoader implements ImageLoader {
  PApplet parent;

  public ProcessingImageLoader(PApplet parent) {
    this.parent = parent;
  }

  @Override
  public Object loadImage(String path) {
    return parent.loadImage(
        ProcessingImageLoader.class.getClassLoader().getResource("images/" + path).getPath());
  }
}
