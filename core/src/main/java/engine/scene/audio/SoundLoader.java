package engine.scene.audio;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class SoundLoader {

  private static final String EXTERNAL_PATH = "resources/audio/";

  private static final String[] INTERNAL_PATHS = {"/audio/", "/resources/audio/"};

  public static Sound load(String filePath) {

    // 1. Prefer the external audio file next to the JAR
    File externalFile = new File(EXTERNAL_PATH + filePath);

    if (externalFile.isFile()) {
      try {
        InputStream stream = new BufferedInputStream(new FileInputStream(externalFile));

        return new Sound(stream);

      } catch (Exception e) {
        throw new RuntimeException(
            "Failed to load external audio file: " + externalFile.getPath(), e);
      }
    }

    // 2. Fallback: Load the audio file from the classpath
    for (String path : INTERNAL_PATHS) {

      InputStream raw = SoundLoader.class.getResourceAsStream(path + filePath);

      if (raw != null) {
        return new Sound(new BufferedInputStream(raw));
      }
    }

    throw new RuntimeException("Sound file not found: " + filePath);
  }
}
