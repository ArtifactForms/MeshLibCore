package engine.scene.audio;

import java.util.List;

public class AudioSystem {

  private AudioListener listener;

  public void update(List<AudioSource> audioSources) {
    for (AudioSource audioSource : audioSources) {
      audioSource.update(listener);
    }
  }

  public AudioListener getListener() {
    return listener;
  }

  public void setListener(AudioListener listener) {
    this.listener = listener;
  }
}
