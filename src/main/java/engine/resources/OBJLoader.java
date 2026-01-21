package engine.resources;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import engine.render.Material;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.SubMesh;
import mesh.util.VertexNormals;

public class OBJLoader implements ModelLoaderStrategy {
  private static final String BLANK = " ";
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
  private String[] parts;
  private ArrayList<Vector3f> vertexNormals;

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
    mesh = new Mesh3D();

    String line;
    BufferedReader reader = new BufferedReader(new FileReader(getPath(filePath)));

    while ((line = reader.readLine()) != null) {
      parts = line.split(BLANK);
      String keyword = parts[0];

      switch (keyword) {
        case COMMENT:
          System.out.println("Comment: " + line);
          break;
        case OBJECT:
          System.out.println("Object: " + line);
          break;
        case USE_MATERIAL:
          processUseMaterial();
          break;
        case MATERIAL_LIB:
          loadMaterials();
          break;
        case GEOMETRIC_VERTEX:
          processVertex();
          break;
        case VERTEX_NORMAL:
          processVertexNormal();
          break;
        case TEXTURE_VERTEX:
          processVertexTexture();
          break;
        case FACE:
          processFace(line);
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
    if (vertexNormals.isEmpty()) {
      VertexNormals vertexNormals = new VertexNormals(mesh);
      mesh.setVertexNormals(vertexNormals.getVertexNormals());
    } else {
      mesh.setVertexNormals(vertexNormals);
    }
  }

  private void loadMaterials() throws IOException {
    System.out.println(directoryPath);

    MaterialLoader materialLoader = new MaterialLoader();
    Map<String, Material> materials =
        materialLoader.loadMaterials(getPath(directoryPath + "/" + parts[1]));
    for (Material material : materials.values()) {
      model.addMaterial(material);
    }
  }

  private void processUseMaterial() {
    SubMesh subMesh = new SubMesh();
    subMesh.setMaterialName(parts[1]);
    model.addSubMesh(subMesh);
    currentSubMesh = subMesh;
  }

  private void processFace(String line) {
    // TODO FIXME support //
    if (line.contains("/")) {
      processFaceVertexUvFormat();
    } else {
      processFaceVertexFormat();
    }
  }

  private void processFaceVertexUvFormat() {
    int[] indices = new int[parts.length - 1];
    int[] uvIndices = new int[parts.length - 1];
    for (int i = 1; i < parts.length; i++) {
      String part = parts[i];
      String[] parts2 = part.split("/");
      indices[i - 1] = Integer.parseInt(parts2[0]) - 1;
      if (parts2.length > 1 && !parts2[1].isEmpty()) {
        uvIndices[i - 1] = Integer.parseInt(parts2[1]) - 1;
      } else {
        uvIndices[i - 1] = -1; // No UV index, handle this properly
      }
    }
    addFace(new Face3D(indices, uvIndices));
  }

  private void processFaceVertexFormat() {
    int[] indices = new int[parts.length - 1];
    for (int i = 1; i < parts.length; i++) {
      indices[i - 1] = Integer.parseInt(parts[i]) - 1;
    }
    addFace(new Face3D(indices));
  }

  private void addFace(Face3D face) {
    if (currentSubMesh != null) {
      currentSubMesh.addFace(face);
    } else {
      mesh.add(face);
    }
  }

  private void processVertex() {
    mesh.addVertex(parse(parts[1]), parse(parts[2]), parse(parts[3]));
  }

  private void processVertexNormal() {
    vertexNormals.add(new Vector3f(parse(parts[1]), parse(parts[2]), parse(parts[3])));
  }

  private void processVertexTexture() {
    mesh.addUvCoordinate(parse(parts[1]), parse(parts[2]));
  }

  private float parse(String str) {
    return Float.parseFloat(str);
  }

  private String getPath(String filePath) {
    return OBJLoader.class.getClassLoader().getResource("models/" + filePath).getPath();
  }
}
