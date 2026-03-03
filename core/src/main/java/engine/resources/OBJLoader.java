package engine.resources;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import engine.rendering.Material;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.SubMesh;
import mesh.util.VertexNormals;

public class OBJLoader implements ModelLoaderStrategy {
  private static final String COMMENT = "#";
  private static final String OBJECT = "o";
  private static final String GEOMETRIC_VERTEX = "v";
  private static final String VERTEX_NORMAL = "vn";
  private static final String TEXTURE_VERTEX = "vt";
  private static final String FACE = "f";
  private static final String MATERIAL_LIB = "mtllib";
  private static final String USE_MATERIAL = "usemtl";

  private Mesh3D mesh;
  private SubMesh currentSubMesh;
  private Model model;
  private String directoryPath;
  private ArrayList<Vector3f> vertexNormals;
  private int parsedFaceCount;

  public OBJLoader() {
    vertexNormals = new ArrayList<Vector3f>();
  }

  @Override
  public Model load(String filePath) {
    try {
      read(filePath);
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (mesh == null) mesh = new Mesh3D();
    model.setMesh(mesh);
    return model;
  }

  private Mesh3D read(String filePath) throws IOException {
    System.out.println("Loading: " + filePath);

    directoryPath = filePath.substring(0, filePath.lastIndexOf('/'));
    model = new Model("");
    vertexNormals.clear();
    parsedFaceCount = 0;
    mesh = new Mesh3D();

    String line;
    BufferedReader reader = new BufferedReader(new FileReader(getPath(filePath)));

    while ((line = reader.readLine()) != null) {
      String currentLine = line.trim();
      if (currentLine.isEmpty()) {
        continue;
      }

      String[] parts = currentLine.split("\\s+");
      String keyword = parts[0];

      switch (keyword) {
        case COMMENT:
          System.out.println("Comment: " + line);
          break;
        case OBJECT:
          System.out.println("Object: " + line);
          break;
        case USE_MATERIAL:
          processUseMaterial(parts);
          break;
        case MATERIAL_LIB:
          loadMaterials(parts);
          break;
        case GEOMETRIC_VERTEX:
          processVertex(parts);
          break;
        case VERTEX_NORMAL:
          processVertexNormal(parts);
          break;
        case TEXTURE_VERTEX:
          processVertexTexture(parts);
          break;
        case FACE:
          processFace(parts);
          break;
        default:
          System.err.println("Unsupported OBJ keyword " + keyword + " Line: " + line);
      }
    }
    reader.close();
    applyNormals();
    return mesh;
  }

  private void applyNormals() {
	  // TODO vertex normals support
//    if (vertexNormals.isEmpty()) {
//      mesh.setVertexNormals(VertexNormals.calculate(mesh));
//    } else {
//      mesh.setVertexNormals(vertexNormals);
//    }
  }

  private void loadMaterials(String[] parts) throws IOException {
    System.out.println(directoryPath);

    MaterialLoader materialLoader = new MaterialLoader();
    Map<String, Material> materials =
        materialLoader.loadMaterials(getPath(directoryPath + "/" + parts[1]));
    for (Material material : materials.values()) {
      model.addMaterial(material);
    }
  }

  private void processUseMaterial(String[] parts) {
    if (parts.length < 2) {
      return;
    }

    SubMesh subMesh = new SubMesh();
    subMesh.setMaterialName(parts[1]);
    model.addSubMesh(subMesh);
    currentSubMesh = subMesh;
  }

  private void processFace(String[] parts) {
    int vertexCount = parts.length - 1;
    if (vertexCount < 3) {
      return;
    }

    int[] vertexIndices = new int[vertexCount];
    int[] uvIndices = new int[vertexCount];
    Arrays.fill(uvIndices, -1);
    boolean hasAnyUv = false;

    for (int i = 1; i < parts.length; i++) {
      String[] faceParts = parts[i].split("/");

      vertexIndices[i - 1] = resolveVertexIndex(faceParts[0], mesh.getVertexCount());

      if (faceParts.length > 1 && !faceParts[1].isEmpty()) {
        uvIndices[i - 1] = resolveVertexIndex(faceParts[1], mesh.getSurfaceLayer().getUVCount());
        hasAnyUv = true;
      }
    }

    addFace(new Face3D(vertexIndices), hasAnyUv ? uvIndices : null);
  }

  private int resolveVertexIndex(String token, int size) {
    int index = Integer.parseInt(token);
    if (index > 0) {
      return index - 1;
    }
    return size + index;
  }

  private void addFace(Face3D face, int[] uvIndices) {
    if (uvIndices != null) {
      mesh.getSurfaceLayer().setFaceUVIndices(parsedFaceCount, uvIndices);
    }
    parsedFaceCount++;

    if (currentSubMesh != null) {
      currentSubMesh.addFace(face);
    } else {
      mesh.add(face);
    }
  }

  private void processVertex(String[] parts) {
    mesh.addVertex(parse(parts[1]), parse(parts[2]), parse(parts[3]));
  }

  private void processVertexNormal(String[] parts) {
    vertexNormals.add(new Vector3f(parse(parts[1]), parse(parts[2]), parse(parts[3])));
  }

  private void processVertexTexture(String[] parts) {
    mesh.addUvCoordinate(parse(parts[1]), parse(parts[2]));
  }

  private float parse(String str) {
    return Float.parseFloat(str);
  }

  private String getPath(String filePath) {
    return OBJLoader.class.getClassLoader().getResource("models/" + filePath).getPath();
  }
}
