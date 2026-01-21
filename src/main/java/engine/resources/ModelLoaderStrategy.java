package engine.resources;

import java.io.IOException;

public interface ModelLoaderStrategy {
  Model load(String filePath);
}
