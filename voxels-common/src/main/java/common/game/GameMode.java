package common.game;

import java.util.HashMap;
import java.util.Map;

public enum GameMode {
  SURVIVAL(0),
  CREATIVE(1);

  private static final Map<Integer, GameMode> BY_ID = new HashMap<>();

  static {
    for (GameMode mode : values()) {
      BY_ID.put(mode.id, mode);
    }
  }

  private final int id;

  GameMode(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public static GameMode fromId(int id) {
    return BY_ID.get(id);
  }
}
