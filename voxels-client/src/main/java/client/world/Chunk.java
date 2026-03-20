package client.world;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import common.world.ChunkData;
import common.world.ChunkStatus;
import engine.components.StaticGeometry;
import engine.rendering.Graphics;
import math.Bounds;
import math.Vector3f;

public class Chunk extends ChunkData {
  private static final ExecutorService executorService = Executors.newFixedThreadPool(4);

  private final Vector3f worldPosition = new Vector3f();

  private volatile StaticGeometry opaqueGeometry;
  private volatile StaticGeometry waterGeometry;
  private volatile StaticGeometry decorGeometry;

  private Future<ChunkMesher.MeshResult> meshFuture;

  private volatile int generation = 0;
  private volatile boolean needsRebuild = false;
  private volatile ChunkStatus status = ChunkStatus.EMPTY;

  public Chunk(int x, int z) {
    super(x, z);
    this.worldPosition.set(x * 16, 0, z * 16);
  }

  public void setupForPooling(int x, int z) {
    this.generation++;
    if (meshFuture != null) {
      meshFuture.cancel(true);
      meshFuture = null;
    }

    this.chunkX = x;
    this.chunkZ = z;
    this.worldPosition.set(x * 16, 0, z * 16);

    this.clear();
    this.opaqueGeometry = null;
    this.waterGeometry = null;
    this.decorGeometry = null;
    this.status = ChunkStatus.EMPTY;
    this.needsRebuild = false;
  }

  public void scheduleMeshGeneration(ChunkManager manager) {
    if (status == ChunkStatus.MESH_GENERATING || !isDataReady()) return;
    status = ChunkStatus.MESH_GENERATING;
    final int taskGen = generation;

    meshFuture =
        executorService.submit(
            () -> {
              ChunkMesher mesher = new ChunkMesher(this, manager);
              ChunkMesher.MeshResult result = mesher.createMesh();
              return (taskGen == generation) ? result : null;
            });
  }

  public void updateMesh() {
    if (meshFuture == null || !meshFuture.isDone()) return;
    try {
      ChunkMesher.MeshResult result = meshFuture.get();
      if (result != null) {
        this.opaqueGeometry = result.opaque;
        this.waterGeometry = result.water;
        this.decorGeometry = result.decor;
        this.status = ChunkStatus.MESH_READY;
        this.needsRebuild = false;
      }
    } catch (Exception e) {
      e.printStackTrace();
      this.status = ChunkStatus.DATA_READY;
    }
    meshFuture = null;
  }

  public void renderOpaque(Graphics g) {
    if (opaqueGeometry != null) {
      g.pushMatrix();
      g.translate(worldPosition.x, worldPosition.y, worldPosition.z);
      opaqueGeometry.render(g);
      g.popMatrix();
    }
  }

  public void renderWater(Graphics g) {
    if (waterGeometry != null) {
      g.pushMatrix();
      g.translate(worldPosition.x, worldPosition.y, worldPosition.z);
      waterGeometry.render(g);
      g.popMatrix();
    }
  }

  public void renderDecor(Graphics g) {
    if (decorGeometry != null) {
      g.pushMatrix();
      g.translate(worldPosition.x, worldPosition.y, worldPosition.z);
      decorGeometry.render(g);
      g.popMatrix();
    }
  }

  public void render(Graphics g) {
    renderOpaque(g);
    renderWater(g);
  }

  public void markDirty() {
    this.needsRebuild = true;
  }

  public void setDataReady() {
    this.status = ChunkStatus.DATA_READY;
  }

  public boolean isDataReady() {
    return status != ChunkStatus.EMPTY;
  }

  public ChunkStatus getStatus() {
    return status;
  }

  public boolean needsRebuild() {
    return needsRebuild;
  }

  public Vector3f getWorldPosition() {
    return worldPosition;
  }

  public Bounds getMeshBounds() {
    // Für Culling nutzen wir meist das opake Mesh
    if (opaqueGeometry != null) {
      return opaqueGeometry.getLocalBounds();
    }
    return null;
  }
}
