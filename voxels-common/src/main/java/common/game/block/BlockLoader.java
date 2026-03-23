package common.game.block;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.*;
import java.net.URI;
import java.nio.file.*;
import java.util.Collections;
import java.util.stream.Stream;

public class BlockLoader {
  private static final Gson gson = new Gson();

  public static void load() {
    try {
      // Sucht den Ordner im Klassenpfad (funktioniert in IDE & JAR)
      var resource = BlockLoader.class.getResource("/assets/blocks");
      if (resource == null) {
        System.err.println("Pfad /assets/blocks nicht gefunden!");
        return;
      }

      URI uri = resource.toURI();
      Path myPath;

      if (uri.getScheme().equals("jar")) {
        FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
        myPath = fileSystem.getPath("/assets/blocks");
      } else {
        myPath = Paths.get(uri);
      }

      try (Stream<Path> walk = Files.walk(myPath, 1)) {
        walk.filter(Files::isRegularFile)
            .filter(p -> p.toString().endsWith(".json"))
            .forEach(BlockLoader::loadSingleBlock);
      }

      //      BlockRegistry.freeze();
      System.out.println("BlockRegistry erfolgreich geladen: " + BlockRegistry.size() + " Blöcke.");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void loadSingleBlock(Path path) {
    try (BufferedReader reader = Files.newBufferedReader(path)) {
      JsonObject json = gson.fromJson(reader, JsonObject.class);

      // 1. Get identifier and check if it already exists
      String name = json.get("name").getAsString();
      BlockType type = BlockRegistry.get(name);

      if (type == null) {
        // Block is not registered in Blocks.java yet, create it using the ID from JSON
        if (!json.has("id")) {
          throw new IllegalArgumentException("Missing 'id' field in JSON for new block: " + name);
        }
        short id = json.get("id").getAsShort();
        type = BlockRegistry.register(id, name);
      } else {
        // Block exists (from Blocks.java).
        // Optional: Verify that the ID in JSON matches the hardcoded ID to prevent mismatches
        if (json.has("id")) {
          short jsonId = json.get("id").getAsShort();
          if (type.getId() != jsonId) {
            System.err.println(
                "[WARN] ID mismatch for "
                    + name
                    + ": Code has "
                    + type.getId()
                    + ", JSON has "
                    + jsonId);
          }
        }
      }

      // 2. Set/Override flags from JSON
      if (json.has("solid")) type.setSolid(json.get("solid").getAsBoolean());
      if (json.has("opaque")) type.setOpaque(json.get("opaque").getAsBoolean());
      if (json.has("light")) type.setLightEmission(json.get("light").getAsInt());

      // 3. Shape / Render logic
      if (json.has("shape")) {
        BlockShape shape = BlockShape.valueOf(json.get("shape").getAsString().toUpperCase());
        type.setShape(shape);
      }

      // 4. Color data
      if (json.has("color")) {
        JsonObject c = json.getAsJsonObject("color");
        type.setMapColor(c.get("r").getAsInt(), c.get("g").getAsInt(), c.get("b").getAsInt());
      }

    } catch (Exception e) {
      System.err.println("Error loading block asset: " + path.getFileName());
      e.printStackTrace();
    }
  }
}
