package engine.scene.audio;

import java.io.BufferedInputStream;
import java.io.InputStream;

public class SoundLoader {

  public static Sound load(String filePath) {

    String path = "audio/" + filePath;

    InputStream raw =
        SoundLoader.class.getClassLoader().getResourceAsStream(path);

    if (raw == null) {
      System.err.println("Sound nicht gefunden: " + path);
      return null;
    }

    BufferedInputStream buffered = new BufferedInputStream(raw);

    return new Sound(buffered);
  }
}