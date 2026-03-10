package server.world.generation;

import java.util.ArrayList;
import java.util.List;

import common.game.block.BlockRegistry;
import common.game.block.BlockType;

/** Parses flat world generation presets. Format example: "1*bedrock,60*dirt,1*grass_block" */
public class FlatPresetParser {

  /**
   * Parses a comma-separated preset string into a list of {@link FlatLayer}s. * @param preset The
   * preset string (e.g., "1*stone,2*dirt")
   *
   * @return A list of layers containing block types and their heights.
   * @throws NumberFormatException if the height is not a valid integer.
   * @throws IllegalArgumentException if the block type is unknown.
   */
  public static List<FlatLayer> parse(String preset) {
    List<FlatLayer> layers = new ArrayList<>();
    if (preset == null || preset.isEmpty()) return layers;

    String[] parts = preset.split(",");

    for (String part : parts) {
      String[] layerParts = part.split("\\*");

      int height;
      String blockName;

      // Handle both "5*stone" and "stone" (defaulting to 1)
      if (layerParts.length == 2) {
        height = Integer.parseInt(layerParts[0].trim());
        blockName = layerParts[1].trim().toUpperCase();
      } else {
        height = 1;
        blockName = layerParts[0].trim().toUpperCase();
      }

      try {
        BlockType block = BlockRegistry.get(blockName);
        layers.add(new FlatLayer(block, height));
      } catch (IllegalArgumentException e) {
        System.err.println("[FlatPresetParser] Unknown block type: " + blockName);
        // Optional: Fallback to a default block like STONE or AIR
      }
    }

    return layers;
  }
}
