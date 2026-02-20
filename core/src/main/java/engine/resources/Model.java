package engine.resources;

import java.util.ArrayList;
import java.util.List;

import engine.render.Material;
import mesh.Mesh3D;
import mesh.SubMesh;

public class Model {
  private String name;
  private Mesh3D mesh;
  private List<SubMesh> meshes;
  private List<Material> materials;

  public Model(String name) {
    if (name == null) {
      throw new IllegalArgumentException("Name cannot be null");
    }
    this.name = name;
    mesh = new Mesh3D();
    meshes = new ArrayList<SubMesh>();
    materials = new ArrayList<Material>();
  }

  public void addSubMesh(SubMesh mesh) {
    if (mesh == null) {
      throw new IllegalArgumentException("Mesh cannot be null.");
    }
    meshes.add(mesh);
  }

  public void addMaterial(Material material) {
    if (material == null) {
      throw new IllegalArgumentException("Material cannot be null.");
    }
    //    material.setUseLighting(false);
    materials.add(material);
  }

  public String getName() {
    return name;
  }

  public Mesh3D getMesh() {
    return mesh;
  }

  public void setMesh(Mesh3D mesh) {
    this.mesh = mesh;
  }

  public List<SubMesh> getSubMeshes() {
    return meshes;
  }

  public void setUseLighting(boolean useLighting) {
    for (Material material : materials) {
      material.setUseLighting(useLighting);
    }
  }

  public List<Material> getMaterials() {
    return materials;
  }

  public Material getMaterial(String name) {
    for (int i = 0; i < materials.size(); i++) {
      Material material = materials.get(i);
      if (material.getName().equals(name)) return material;
    }
    // Exception??
    return null;
  }
}
