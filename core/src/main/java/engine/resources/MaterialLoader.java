package engine.resources;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import engine.rendering.Material;

public class MaterialLoader {
  private static final String KEY_NEW_MATERIAL = "newmtl";
  private static final String KEY_SHININESS = "Ns";
  private static final String KEY_AMBIENT_COLOR = "Ka";
  private static final String KEY_DIFFUSE_COLOR = "Kd";
  private static final String KEY_SPECULAR_COLOR = "Ks";
  private static final String KEY_DISSOLVE = "d"; // opacity
  private static final String KEY_DIFFUSE_MAP = "map_Kd";
  private static final String KEY_OPACITY_MAP = "map_d";

  private Map<String, Material> materials = new HashMap<>();
  private String directoryPath;

  public Map<String, Material> loadMaterials(String filePath) throws IOException {
    directoryPath = filePath.substring(0, filePath.lastIndexOf('/'));
    BufferedReader reader = new BufferedReader(new FileReader(filePath));
    Material currentMaterial = null;

    String line;
    while ((line = reader.readLine()) != null) {
      String[] parts = line.split("\\s+", 2);
      String keyword = parts[0];

      switch (keyword) {
        case KEY_NEW_MATERIAL:
          currentMaterial = new Material();
          currentMaterial.setName(parts[1]);
          materials.put(parts[1], currentMaterial);
          break;
        case KEY_DIFFUSE_COLOR:
          currentMaterial.setDiffuse(parseColor(parts[1]));
          break;
        case KEY_SPECULAR_COLOR:
          currentMaterial.setSpecular(parseColor(parts[1]));
          break;
        case "Ns":
          currentMaterial.setShininess(Float.parseFloat(parts[1]));
          break;
        case KEY_DISSOLVE:
          currentMaterial.setOpacity(Float.parseFloat(parts[1]));
          break;
        case KEY_DIFFUSE_MAP:
          String file = parts[1];

          System.out.println(parts[1]);

          try {
            BufferedImage image;
            image = ImageIO.read(new File(directoryPath + "/" + file));
            Texture texture = TextureManager.getInstance().createTexture(image);

            currentMaterial.setDiffuseTexture(texture);
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }

          break;

        case KEY_OPACITY_MAP:
          String file1 = parts[1];

          System.out.println(parts[1]);

          try {
            BufferedImage image;
            image = ImageIO.read(new File(directoryPath + "/" + file1));
            Texture opactityMap = TextureManager.getInstance().createTexture(image);

            currentMaterial.setOpacityMap(opactityMap);
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          break;
      }
    }

    reader.close();
    return materials;
  }

  private float[] parseColor(String line) {
    String[] values = line.split(" ");
    float red = Float.parseFloat(values[0]);
    float green = Float.parseFloat(values[1]);
    float blue = Float.parseFloat(values[2]);
    return new float[] {red, green, blue};
  }
}
