package engine.scene.audio;

import java.util.HashMap;
import java.util.Map;

public final class SoundManager {

  private static final Map<String, Sound> sounds = new HashMap<>();
  private static final Map<String, SoundEffect> effects = new HashMap<>();

  private SoundManager() {}

  /* ================= MUSIC / LOOPING ================= */

  public static void addSound(String key, String filePath) {
    sounds.put(key, SoundLoader.load(filePath));
  }

  public static void playSound(String key) {
    Sound sound = sounds.get(key);
    if (sound != null) {
      sound.play();
    } else {
      error(key);
    }
  }

  public static void loopSound(String key) {
    Sound sound = sounds.get(key);
    if (sound != null) {
      sound.loop();
    } else {
      error(key);
    }
  }

  public static void stopSound(String key) {
    Sound sound = sounds.get(key);
    if (sound != null) {
      sound.stop();
    } else {
      error(key);
    }
  }

  /* ================= ONE-SHOT EFFECTS ================= */

  public static void addEffect(String key, String filePath, int poolSize) {
    effects.put(key, new SoundEffect(filePath, poolSize));
  }

  public static void playEffect(String key) {
    SoundEffect effect = effects.get(key);
    if (effect != null) {
      effect.play();
    } else {
      error(key);
    }
  }

  private static void error(String key) {
    System.err.println("Sound '" + key + "' not found!");
  }
}
