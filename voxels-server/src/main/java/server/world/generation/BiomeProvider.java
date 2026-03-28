package server.world.generation;

// Hilfsklasse für die Biom-Bestimmung
public class BiomeProvider {
  public static BiomeType getBiome(float temperature, float moisture) {
    // KALT
    if (temperature < 0.35f) {
      if (moisture < 0.4f) return BiomeType.SNOW; // Tundra-artig
      return BiomeType.SNOW; // Hier könntest du später SNOWY_TAIGA ergänzen
    }

    // GEMÄSSIGT
    if (temperature < 0.7f) {
      if (moisture < 0.35f) return BiomeType.PLAINS;
      if (moisture < 0.7f) return BiomeType.FOREST;
      return BiomeType.PLAINS; // Später: SWAMP
    }

    // HEISS
    if (moisture < 0.3f) return BiomeType.DESERT;
    return BiomeType.FOREST; // Später: JUNGLE / SAVANNA
  }
}
