package engine.scene.audio;

import java.util.HashMap;
import java.util.Map;

public class SoundManager {
  private static final Map<String, Sound> sounds = new HashMap<>();

  public static void addSound(String name, String filePath) {
    sounds.put(name, SoundLoader.load(filePath));
  }

  public static void playSound(String name) {
    Sound sound = sounds.get(name);
    if (sound != null) {
      sound.play();
    } else {
      sendErrorMessage(name);
    }
  }

  public static void loopSound(String name) {
    Sound sound = sounds.get(name);
    if (sound != null) {
      sound.loop();
    } else {
      sendErrorMessage(name);
    }
  }

  public static void stopSound(String name) {
    Sound sound = sounds.get(name);
    if (sound != null) {
      sound.stop();
    } else {
      sendErrorMessage(name);
    }
  }

  public static Sound getSound(String name) {
    Sound sound = sounds.get(name);
    if (sound == null) {
      sendErrorMessage(name);
    }
    return sound;
  }

  private static void sendErrorMessage(String name) {
    System.err.println("Sound '" + name + "' not found!");
  }
}
