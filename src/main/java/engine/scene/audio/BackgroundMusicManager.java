package engine.scene.audio;

public class BackgroundMusicManager {
  private static Sound currentMusic;

  public static void playMusic(String name) {
    if (currentMusic != null) {
      currentMusic.stop();
    }
    currentMusic = SoundManager.getSound(name);
    if (currentMusic != null) {
      currentMusic.loop();
    }
  }
}
