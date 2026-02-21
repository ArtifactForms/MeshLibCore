package engine.scene.nodes;

import engine.components.BillboardComponent;
import engine.components.Geometry;
import engine.gfx.UVRect;
import engine.rendering.Material;
import engine.resources.Texture;
import engine.scene.SceneNode;
import math.Color;
import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.primitives.PlaneCreatorUV;
import mesh.modifier.transform.RotateXModifier;
import mesh.modifier.transform.RotateYModifier;
import mesh.modifier.transform.RotateZModifier;
import mesh.modifier.transform.ScaleModifier;
import mesh.next.surface.SurfaceLayer;

public class Billboard extends SceneNode {

  private final float width;
  private final float height;
  private final Texture texture;

  private UVRect uv;
  private Mesh3D mesh;

  public Billboard(Texture texture, float size) {
    this(texture, UVRect.FULL, size);
  }

  public Billboard(Texture texture, UVRect uv, float size) {
    this(texture, uv, size, size);
  }

  public Billboard(Texture texture, float width, float height) {
    this(texture, UVRect.FULL, width, height);
  }

  public Billboard(Texture texture, UVRect uv, float width, float height) {
    super("Billboard");
    this.texture = texture;
    this.width = width;
    this.height = height;
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
    Mesh3D mesh = new PlaneCreatorUV(0.5f).create();

    // Rotate plane upright for billboard usage
    new RotateXModifier(Mathf.HALF_PI).modify(mesh);
    new RotateZModifier(Mathf.HALF_PI).modify(mesh);
    new RotateYModifier(-Mathf.PI).modify(mesh);
    new ScaleModifier(width, height, 1).modify(mesh);

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
    SurfaceLayer surfaceLayer = mesh.getSurfaceLayer();

    surfaceLayer.getUvAt(0).set(uv.uMin, uv.vMin);
    surfaceLayer.getUvAt(1).set(uv.uMax, uv.vMin);
    surfaceLayer.getUvAt(2).set(uv.uMax, uv.vMax);
    surfaceLayer.getUvAt(3).set(uv.uMin, uv.vMax);
    
    surfaceLayer.setFaceUVIndices(0, new int[] {0, 1, 2, 3});
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
