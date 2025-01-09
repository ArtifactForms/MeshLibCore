package engine.scene.audio;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import math.Mathf;

public class Sound {
  private Clip clip;
  private FloatControl panControl;

  public Sound(String filePath) {
    try {
      AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(filePath));
      clip = AudioSystem.getClip();
      clip.open(audioStream);

      // Check if the clip supports panning
      if (clip.isControlSupported(FloatControl.Type.PAN)) {
        panControl = (FloatControl) clip.getControl(FloatControl.Type.PAN);
      }
    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
      e.printStackTrace();
    }
  }

  public void play() {
    if (clip != null) {
      clip.setFramePosition(0); // Rewind to the start
      clip.start();
    }
  }

  public void loop() {
    if (clip != null) {
      clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
  }

  public void stop() {
    if (clip != null) {
      clip.stop();
    }
  }

  public void setVolume(float volume) {
    if (clip != null) {
      FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
      float value = 20f * (float) Mathf.log10(volume);
      gainControl.setValue(Mathf.clamp(value, gainControl.getMinimum(), gainControl.getMaximum()));
    }
  }

  /**
   * Set the pan (left-right balance) for the sound.
   *
   * @param pan The pan value between -1.0 (left) and 1.0 (right).
   */
  public void setPan(float pan) {
    if (panControl != null) {
      panControl.setValue(Mathf.clamp(pan, panControl.getMinimum(), panControl.getMaximum()));
    }
  }
}
