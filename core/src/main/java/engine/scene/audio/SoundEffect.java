package engine.scene.audio;

import java.util.ArrayList;
import java.util.List;

public class SoundEffect {

  private final List<Sound> pool;
  private int index = 0;

  public SoundEffect(String filePath, int poolSize) {
    pool = new ArrayList<>(poolSize);

    for (int i = 0; i < poolSize; i++) {
      pool.add(SoundLoader.load(filePath));
    }
  }

  public void play() {
    Sound sound = pool.get(index);
    index = (index + 1) % pool.size();
    sound.play();
  }

  public void stopAll() {
    for (Sound sound : pool) {
      sound.stop();
    }
  }
}
