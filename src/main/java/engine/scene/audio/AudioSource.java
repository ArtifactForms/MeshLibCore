package engine.scene.audio;

import engine.components.AbstractComponent;
import math.Mathf;
import math.Vector3f;

/**
 * Represents an audio source attached to a scene node. The position of the audio source is
 * determined by the position of the node, and the volume is adjusted based on the listener's
 * distance from the source.
 */
public class AudioSource extends AbstractComponent {

  private Sound sound;
  private float maxDistance; // Max distance for sound attenuation
  private float volume; // Volume of the sound
  private boolean loop; // Whether the sound should loop

  /**
   * Constructs an {@code AudioSource} with a sound and a maximum distance for attenuation.
   *
   * @param sound the sound to be played by this audio source.
   * @param maxDistance the maximum distance at which the sound will be audible.
   */
  public AudioSource(Sound sound, float maxDistance) {
    this.sound = sound;
    this.maxDistance = maxDistance;
    this.volume = 1.0f;
  }

  /** Plays the sound, either looping or once depending on the loop setting. */
  public void play() {
    if (sound == null) {
      return;
    }
    if (loop) {
      sound.loop();
    } else {
      sound.play();
    }
  }

  /** Stops the currently playing sound. */
  public void stop() {
    if (sound != null) {
      sound.stop();
    }
  }

  /**
   * Updates the volume of the sound based on the distance to the listener.
   *
   * @param audioListener The current listener (player, camera) in 3D space.
   */
  public void update(AudioListener audioListener) {
    if (sound == null) return;

    // Get the position of the audio source from the owner node's transform
    Vector3f sourcePosition = getOwner().getTransform().getPosition();

    // Calculate the distance between the audio source and the listener
    float distance = audioListener.getPosition().distance(sourcePosition);

    //    // Update the volume based on the distance
    float newVolume = calculateVolume(distance);
    // Set the new volume
    sound.setVolume(newVolume);

    // Apply a basic distance attenuation (volume decreases as distance increases)
    //    float distanceFactor = Math.max(0, 1 - distance / 100.0f);  // Example attenuation factor
    //    sound.setVolume(volume * distanceFactor);

    // Apply directional effects based on the listener's orientation
    Vector3f directionToSound = sourcePosition.subtract(audioListener.getPosition()).normalize();
    float dotProduct = directionToSound.dot(audioListener.getForward()); // Directional panning
    applyPanning(dotProduct);
  }

  private void applyPanning(float dotProduct) {
    // Adjust panning based on the listener's orientation (dot product of listener's forward
    // direction)
    // Positive values (dot > 0) = sound in front of listener
    // Negative values (dot < 0) = sound behind the listener

    // Calculate the pan value: between -1 (left) and 1 (right)
    // A simple approach is to map the dot product (-1 to 1) directly to pan
    float pan = Mathf.clamp(dotProduct, -1.0f, 1.0f);

    // Apply the pan to the sound object
//    sound.setPan(pan);
  }

  // TODO Other methods for controlling playback, volume, pitch, etc.

  /**
   * Calculates the volume of the sound based on the distance to the listener. The volume decreases
   * with distance according to a linear attenuation model.
   *
   * @param distance the distance from the listener to the sound source.
   * @return the new volume for the sound.
   */
  private float calculateVolume(float distance) {
    if (distance > maxDistance) return 0.0f;
    return 1.0f - (distance / maxDistance);
  }

  /**
   * Sets whether the audio should loop when played.
   *
   * @param loop {@code true} if the sound should loop, {@code false} otherwise.
   */
  public void setLoop(boolean loop) {
    this.loop = loop;
  }

  /**
   * Sets the volume of the audio source.
   *
   * @param volume the new volume level (between 0.0f and 1.0f).
   */
  public void setVolume(float volume) {
    this.volume = volume;
    if (sound != null) {
      sound.setVolume(volume);
    }
  }

  /**
   * Gets the current volume of the audio source.
   *
   * @return the volume level of the sound.
   */
  public float getVolume() {
    return volume;
  }

  /**
   * Gets the maximum distance for sound attenuation.
   *
   * @return the maximum distance for the sound.
   */
  public float getMaxDistance() {
    return maxDistance;
  }

  @Override
  public void onUpdate(float tpf) {
    // Can be used for additional updates in the future.
  }

  @Override
  public void onAttach() {
    // Logic to handle attachment if needed.
  }

  @Override
  public void onDetach() {
    // Logic to handle detachment if needed.
  }
}
