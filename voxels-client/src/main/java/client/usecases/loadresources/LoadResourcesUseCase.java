package client.usecases.loadresources;

import client.resources.Resources;
import engine.scene.audio.SoundManager;

public class LoadResourcesUseCase implements LoadResources {

  @Override
  public void execute() {
    preloadSounds();
  }

  private void preloadSounds() {
    // Pre-allocate audio buffers
    SoundManager.addSound(Resources.BACKGROUND_MUSIC_KEY, Resources.BACKGROUND_MUSIC);
    SoundManager.addEffect(Resources.BLOCK_BREAK_FX_KEY, Resources.BLOCK_BREAK_FX_PATH, 5);
    SoundManager.addEffect(Resources.BLOCK_PLACE_FX_KEY, Resources.BLOCK_PLACE_FX_PATH, 5);
    SoundManager.addEffect(Resources.FOOT_STEP_GRASS_1_KEY, Resources.FOOT_STEP_GRASS_1_PATH, 3);
    SoundManager.addEffect(Resources.FOOT_STEP_GRASS_2_KEY, Resources.FOOT_STEP_GRASS_2_PATH, 3);
    SoundManager.addEffect(Resources.FOOT_STEP_GRASS_3_KEY, Resources.FOOT_STEP_GRASS_3_PATH, 3);
    SoundManager.addEffect(Resources.FOOT_STEP_GRASS_4_KEY, Resources.FOOT_STEP_GRASS_4_PATH, 3);
  }
}
