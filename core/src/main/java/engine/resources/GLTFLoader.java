package engine.resources;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import engine.rendering.Material;
import math.Color;
import math.Vector2f;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.SubMesh;

public class GLTFLoader implements ModelLoaderStrategy {

  private Mesh3D mesh;
  private Model model;

  // =========================================================
  // Entry point
  // =========================================================

  @Override
  public Model load(String filePath) {
    try {
      InputStream stream =
          GLTFLoader.class.getClassLoader().getResourceAsStream("models/" + filePath);

      if (stream == null) {
        throw new RuntimeException("GLTF file not found: " + filePath);
      }

      Gson gson = new Gson();
      JsonObject gltf =
          gson.fromJson(new java.io.InputStreamReader(stream), JsonObject.class);

      parseGLTF(gltf);

    } catch (Exception e) {
      e.printStackTrace();
    }

    model.setMesh(mesh);
    return model;
  }

  // =========================================================
  // Main parser
  // =========================================================

  private void parseGLTF(JsonObject gltf) {
    model = new Model("");
    mesh = new Mesh3D();

    JsonArray accessors = gltf.getAsJsonArray("accessors");
    JsonArray bufferViews = gltf.getAsJsonArray("bufferViews");
    JsonArray buffers = gltf.getAsJsonArray("buffers");
    JsonArray meshesJson = gltf.getAsJsonArray("meshes");

    // ---------------------------------------------------------
    // Buffers
    // ---------------------------------------------------------
    List<byte[]> bufferData = new ArrayList<>();
    for (int i = 0; i < buffers.size(); i++) {
      String uri = buffers.get(i).getAsJsonObject().get("uri").getAsString();
      bufferData.add(decodeBuffer(uri));
    }

    // ---------------------------------------------------------
    // Materials + textures
    // ---------------------------------------------------------
    loadMaterials(gltf);

    // ---------------------------------------------------------
    // Meshes
    // ---------------------------------------------------------
    for (int m = 0; m < meshesJson.size(); m++) {
      JsonObject meshObj = meshesJson.get(m).getAsJsonObject();
      JsonArray primitives = meshObj.getAsJsonArray("primitives");

      for (int p = 0; p < primitives.size(); p++) {
        JsonObject prim = primitives.get(p).getAsJsonObject();
        SubMesh subMesh = new SubMesh();

        // ---------- Material binding (SAFE)
        Material subMat = model.getMaterials().get(0);
        if (prim.has("material")) {
          int idx = prim.get("material").getAsInt();
          if (idx >= 0 && idx < model.getMaterials().size()) {
            subMat = model.getMaterials().get(idx);
          }
        }
        subMesh.setMaterialName(subMat.getName());

        JsonObject attr = prim.getAsJsonObject("attributes");

        List<Vector3f> positions =
            readVec3(attr.get("POSITION").getAsInt(), accessors, bufferViews, bufferData);

        List<Vector3f> normals =
            attr.has("NORMAL")
                ? readVec3(attr.get("NORMAL").getAsInt(), accessors, bufferViews, bufferData)
                : new ArrayList<>();

        List<Vector2f> uvs =
            attr.has("TEXCOORD_0")
                ? readVec2(attr.get("TEXCOORD_0").getAsInt(), accessors, bufferViews, bufferData)
                : new ArrayList<>();

        int[] indices =
            prim.has("indices")
                ? readIndices(prim.get("indices").getAsInt(), accessors, bufferViews, bufferData)
                : null;

        int baseVertex = mesh.getVertexCount();

        for (int i = 0; i < positions.size(); i++) {
          Vector3f v = positions.get(i);
          mesh.addVertex(v.x, v.y, v.z);

//          if (!normals.isEmpty()) mesh.addNormal(normals.get(i));
//          if (!uvs.isEmpty()) mesh.addUV(uvs.get(i));
        }

        if (indices != null) {
          for (int i = 0; i < indices.length; i += 3) {
            subMesh.addFace(
                new Face3D(
                    baseVertex + indices[i],
                    baseVertex + indices[i + 1],
                    baseVertex + indices[i + 2]));
          }
        }

        model.addSubMesh(subMesh);
      }
    }
  }

  // =========================================================
  // Materials + textures
  // =========================================================

  private void loadMaterials(JsonObject gltf) {

      JsonArray materialsJson =
          gltf.has("materials") ? gltf.getAsJsonArray("materials") : null;
      JsonArray textures =
          gltf.has("textures") ? gltf.getAsJsonArray("textures") : null;
      JsonArray images =
          gltf.has("images") ? gltf.getAsJsonArray("images") : null;

      // ---------------------------------------------------------
      // Default material (ALWAYS index 0)
      // ---------------------------------------------------------
      Material defaultMat = new Material(Color.WHITE);
      defaultMat.setName("DefaultMaterial");
      model.addMaterial(defaultMat);

      if (materialsJson == null) return;

      // ---------------------------------------------------------
      // Materials
      // ---------------------------------------------------------
      for (int i = 0; i < materialsJson.size(); i++) {

        JsonObject mj = materialsJson.get(i).getAsJsonObject();
        Material mat = new Material(Color.WHITE);

        mat.setName(mj.has("name") ? mj.get("name").getAsString() : "Material_" + i);

        if (mj.has("pbrMetallicRoughness")) {
          JsonObject pbr = mj.getAsJsonObject("pbrMetallicRoughness");

          // -----------------------------------------------------
          // baseColorFactor
          // -----------------------------------------------------
          if (pbr.has("baseColorFactor")) {
            JsonArray f = pbr.getAsJsonArray("baseColorFactor");
            mat.setColor(new Color(
                f.get(0).getAsFloat(),
                f.get(1).getAsFloat(),
                f.get(2).getAsFloat()));
            mat.setOpacity(f.get(3).getAsFloat());
          }

          // -----------------------------------------------------
          // baseColorTexture (SAFE, SPEC-COMPLIANT)
          // -----------------------------------------------------
          if (pbr.has("baseColorTexture") && textures != null && images != null) {

            JsonObject baseTex = pbr.getAsJsonObject("baseColorTexture");
            int texIndex = baseTex.get("index").getAsInt();

            if (texIndex >= 0 && texIndex < textures.size()) {
              JsonObject texture = textures.get(texIndex).getAsJsonObject();

              if (texture.has("source")) {
                int imgIndex = texture.get("source").getAsInt();

                if (imgIndex >= 0 && imgIndex < images.size()) {
                  JsonObject image = images.get(imgIndex).getAsJsonObject();

                  // CASE 1: Image has URI (PNG/JPG or embedded)
                  if (image.has("uri")) {
                    String uri = image.get("uri").getAsString();
                    mat.setDiffuseTexture(
                        TextureManager.getInstance().loadTexture("models/" + uri)
                    );
                  }

                  // CASE 2: Image stored in bufferView (GLB-style)
                  else if (image.has("bufferView")) {
                    System.out.println(
                        "[GLTF] Image uses bufferView (skipped): " + mat.getName()
                    );
                    // Totally valid glTF – safe to skip for now
                  }
                }
              }
            }
          }

          // -----------------------------------------------------
          // Metallic / Roughness
          // -----------------------------------------------------
          float metallic =
              pbr.has("metallicFactor") ? pbr.get("metallicFactor").getAsFloat() : 0f;

          float roughness =
              pbr.has("roughnessFactor") ? pbr.get("roughnessFactor").getAsFloat() : 1f;

          mat.setSpecular(new float[] {metallic, metallic, metallic});
          mat.setShininess(Math.max(1f, (1f - roughness) * 64f));
        }

        model.addMaterial(mat);
      }
    }

  // =========================================================
  // Accessors
  // =========================================================

  private List<Vector3f> readVec3(
      int accessorIndex, JsonArray accessors, JsonArray bufferViews, List<byte[]> buffers) {

    JsonObject acc = accessors.get(accessorIndex).getAsJsonObject();
    JsonObject bv = bufferViews.get(acc.get("bufferView").getAsInt()).getAsJsonObject();

    int bufferIndex = bv.get("buffer").getAsInt();
    int offset =
        (bv.has("byteOffset") ? bv.get("byteOffset").getAsInt() : 0)
            + (acc.has("byteOffset") ? acc.get("byteOffset").getAsInt() : 0);

    int count = acc.get("count").getAsInt();
    byte[] data = buffers.get(bufferIndex);

    List<Vector3f> out = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      int o = offset + i * 12;
      out.add(new Vector3f(
          Float.intBitsToFloat(readInt(data, o)),
          Float.intBitsToFloat(readInt(data, o + 4)),
          Float.intBitsToFloat(readInt(data, o + 8))));
    }
    return out;
  }

  private List<Vector2f> readVec2(
      int accessorIndex, JsonArray accessors, JsonArray bufferViews, List<byte[]> buffers) {

    JsonObject acc = accessors.get(accessorIndex).getAsJsonObject();
    JsonObject bv = bufferViews.get(acc.get("bufferView").getAsInt()).getAsJsonObject();

    int bufferIndex = bv.get("buffer").getAsInt();
    int offset =
        (bv.has("byteOffset") ? bv.get("byteOffset").getAsInt() : 0)
            + (acc.has("byteOffset") ? acc.get("byteOffset").getAsInt() : 0);

    int count = acc.get("count").getAsInt();
    byte[] data = buffers.get(bufferIndex);

    List<Vector2f> out = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      int o = offset + i * 8;
      out.add(new Vector2f(
          Float.intBitsToFloat(readInt(data, o)),
          Float.intBitsToFloat(readInt(data, o + 4))));
    }
    return out;
  }

  private int[] readIndices(
      int accessorIndex, JsonArray accessors, JsonArray bufferViews, List<byte[]> buffers) {

    JsonObject acc = accessors.get(accessorIndex).getAsJsonObject();
    JsonObject bv = bufferViews.get(acc.get("bufferView").getAsInt()).getAsJsonObject();

    int bufferIndex = bv.get("buffer").getAsInt();
    int offset =
        (bv.has("byteOffset") ? bv.get("byteOffset").getAsInt() : 0)
            + (acc.has("byteOffset") ? acc.get("byteOffset").getAsInt() : 0);

    int count = acc.get("count").getAsInt();
    byte[] data = buffers.get(bufferIndex);

    int[] out = new int[count];
    for (int i = 0; i < count; i++) {
      int o = offset + i * 2;
      out[i] = (data[o] & 0xFF) | ((data[o + 1] & 0xFF) << 8);
    }
    return out;
  }

  // =========================================================
  // Utils
  // =========================================================

  private byte[] decodeBuffer(String uri) {
    try {
      if (uri.startsWith("data:")) {
        return Base64.getDecoder().decode(uri.substring(uri.indexOf(',') + 1));
      }

      InputStream stream =
          GLTFLoader.class.getClassLoader().getResourceAsStream("models/" + uri);

      if (stream == null) throw new RuntimeException("Missing buffer: " + uri);
      return stream.readAllBytes();

    } catch (Exception e) {
      throw new RuntimeException("Failed to load buffer: " + uri, e);
    }
  }

  private int readInt(byte[] b, int o) {
    return (b[o] & 0xFF)
        | ((b[o + 1] & 0xFF) << 8)
        | ((b[o + 2] & 0xFF) << 16)
        | ((b[o + 3] & 0xFF) << 24);
  }
}
