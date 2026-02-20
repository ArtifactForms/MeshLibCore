package engine.scene.audio;

public class SoundLoader {
  public static Sound load(String filePath) {
    return new Sound(
        SoundLoader.class.getClassLoader().getResource("audio/" + filePath).getPath());
  }
}
