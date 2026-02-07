package demos.jam26.assets;

import demos.jam26.level.TileMap;
import engine.resources.FilterMode;
import engine.resources.Texture;
import engine.resources.TextureManager;
import engine.resources.TextureWrapMode;
import mesh.UVRect;

public final class AssetRefs {

  /* =========================
   * Sounds
   * ========================= */

  public static final String SOUND_EXIT_KEY = "Exit";
  public static final String SOUND_EXIT_PATH = "/dungeon/exit.wav";

  public static final String SOUND_BACKGROUND_KEY = "Background";
  public static final String SOUND_BACKGROUND_PATH = "/dungeon/background.wav";

  public static final String SOUND_PLAYER_DEAD_KEY = "Dead";
  public static final String SOUND_PLAYER_DEAD_PATH = "/dungeon/dead.wav";
  
  public static final String SOUND_PLAYER_HIT_KEY = "Player-Hit";
  public static final String SOUND_PLAYER_HIT_PATH = "/dungeon/ough.wav";


  public static final String SOUND_HEALTH_PICK_UP_KEY = "PickupHealth";
  public static final String SOUND_HEALTH_PICK_UP_PATH = "/dungeon/powerUp.wav";

  public static final String SOUND_ENEMY_HIT_SHRIEK_KEY = "Shriek";
  public static final String SOUND_ENEMY_HIT_SHRIEK_PATH = "/dungeon/monster_shriek.wav";
  
  public static final String SOUND_SHOOT_KEY = "Shoot";
  public static final String SOUND_SHOOT_PATH = "dungeon/plasma_gun_shoot.wav";

  /* =========================
   * Images (non-atlas)
   * ========================= */

  public static final String IMAGE_WEAPON_CROSS_BOW_HUD_PATH = "/dungeon/weapon3.png";
  public static final String IMAGE_WEAPON_CLAW_HUD_PATH = "/dungeon/weapon5.png";
  public static final String IMAGE_MENU_PATH = "/dungeon/menu2.png";
  public static final String IMAGE_LEVEL_PATH = "/images/dungeon/level-test.png";
  public static final String IMAGE_LEVEL_OVERLAY_PATH = "/images/dungeon/level-test-overlay.png";

  /* =========================
   * UI Text
   * ========================= */

  public static final String TITLE_TEXT_GAME_OVER = "GAME OVER";
  public static final String TITLE_TEXT_LEVEL_COMPLETE = "LEVEL COMPLETE";

  /* =========================
   * Texture Atlas
   * ========================= */

  public static final String TEXTURE_ATLAS_PATH = "/dungeon/atlas2.png";

  public static final Texture ATLAS_TEXTURE;
  public static final TextureAtlas ATLAS;

  /* =========================
   * Atlas Assets
   * ========================= */

  public static final Texture EXIT_TEXTURE;
  public static final UVRect EXIT_UV;

  public static final Texture HEALTH_TEXTURE;
  public static final UVRect HEALTH_UV;

  public static final Texture ENEMY_EYE_IDLE_TEXTURE;
  public static final UVRect ENEMY_EYE_IDLE_UV;

  public static final UVRect[] EYE_DEATH_FRAMES;

  static {
    ATLAS_TEXTURE = loadAtlasTexture();
    ATLAS = new TextureAtlas(ATLAS_TEXTURE, TileMap.TILE_SIZE);

    EXIT_TEXTURE = ATLAS_TEXTURE;
    EXIT_UV = ATLAS.getUV(4, 0);

    HEALTH_TEXTURE = ATLAS_TEXTURE;
    HEALTH_UV = ATLAS.getUV(5, 1);

    ENEMY_EYE_IDLE_TEXTURE = ATLAS_TEXTURE;
    ENEMY_EYE_IDLE_UV = ATLAS.getUV(1, 0);

    EYE_DEATH_FRAMES = buildEyeDeathFrames();
  }

  private static Texture loadAtlasTexture() {
    Texture texture = TextureManager.getInstance().loadTexture(TEXTURE_ATLAS_PATH);
    texture.setFilterMode(FilterMode.POINT);
    texture.setTextureWrapMode(TextureWrapMode.REPEAT);
    return texture;
  }

  private static UVRect[] buildEyeDeathFrames() {
    UVRect[] frames = new UVRect[7];
    for (int i = 0; i < frames.length; i++) {
      frames[i] = ATLAS.getUV(1, i);
    }
    return frames;
  }

  private AssetRefs() {
    // no instances
  }
}
