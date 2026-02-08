package engine.scene.nodes;

import engine.components.BillboardComponent;
import engine.components.Geometry;
import engine.render.Material;
import engine.resources.Texture;
import engine.scene.SceneNode;
import math.Color;
import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.primitives.PlaneCreatorUV;
import mesh.modifier.transform.RotateXModifier;
import mesh.modifier.transform.RotateYModifier;
import mesh.modifier.transform.RotateZModifier;
import mesh.uv.UVRect;

public class Billboard extends SceneNode {

  private final float size;
  private final Texture texture;

  private UVRect uv;
  private Mesh3D mesh;

  public Billboard(Texture texture, float size) {
    this(texture, UVRect.FULL, size);
  }

  public Billboard(Texture texture, UVRect uv, float size) {
    super("Billboard");
    this.texture = texture;
    this.size = size;
    this.uv = uv != null ? uv : UVRect.FULL;

    this.mesh = createPlaneMesh();

    addComponent(new Geometry(mesh, createMaterial()));
    addComponent(new BillboardComponent());
  }

  private Material createMaterial() {
    Material material = new Material(Color.WHITE);
    material.setDiffuseTexture(texture);
    material.setUseLighting(false);
    return material;
  }

  private Mesh3D createPlaneMesh() {
    Mesh3D mesh = new PlaneCreatorUV(size).create();

    // Rotate plane upright for billboard usage
    new RotateXModifier(Mathf.HALF_PI).modify(mesh);
    new RotateZModifier(Mathf.HALF_PI).modify(mesh);
    new RotateYModifier(-Mathf.PI).modify(mesh);

    applyUv(mesh, uv);
    return mesh;
  }

  /**
   * Applies the given UVRect to the mesh.
   *
   * <p>Vertex order must match {@link PlaneCreatorUV}:
   *
   * <pre>
   * 0 ---- 1
   * |      |
   * 3 ---- 2
   * </pre>
   */
  private static void applyUv(Mesh3D mesh, UVRect uv) {
    mesh.getUvAt(0).set(uv.uMin, uv.vMin);
    mesh.getUvAt(1).set(uv.uMax, uv.vMin);
    mesh.getUvAt(2).set(uv.uMax, uv.vMax);
    mesh.getUvAt(3).set(uv.uMin, uv.vMax);
  }

  public Billboard at(float x, float y, float z) {
    getTransform().setPosition(x, y, z);
    return this;
  }

  public void setUvRect(UVRect uv) {
    if (uv == null) {
      uv = UVRect.FULL;
    }

    this.uv = uv;

    if (mesh == null) return;
    applyUv(mesh, uv);
  }
}
