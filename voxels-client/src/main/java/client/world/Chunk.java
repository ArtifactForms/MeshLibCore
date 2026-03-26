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

  /**
   * Renders the opaque layer of the chunk relative to the camera to prevent floating point
   * precision jitter.
   */
  public void renderOpaque(Graphics g, Vector3f cameraPos) {
    if (opaqueGeometry == null) return;
    renderLayer(g, cameraPos, opaqueGeometry);
  }

  /** Renders the water layer. */
  public void renderWater(Graphics g, Vector3f cameraPos) {
    if (waterGeometry == null) return;
    renderLayer(g, cameraPos, waterGeometry);
  }

  /** Renders the decoration/cut-out layer. */
  public void renderDecor(Graphics g, Vector3f cameraPos) {
    if (decorGeometry != null) {
      renderLayer(g, cameraPos, decorGeometry);
    }
  }

  /** Helper to apply the relative transformation and draw the geometry. */
  private void renderLayer(Graphics g, Vector3f cameraPos, StaticGeometry geometry) {
    g.pushMatrix();
    // The core fix: (World Position - Camera Position)
    g.translate(
        worldPosition.x - cameraPos.x,
        worldPosition.y - cameraPos.y,
        worldPosition.z - cameraPos.z);
    geometry.render(g);
    g.popMatrix();
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

  public Bounds getMeshBounds(Bounds store) {
    if (store == null) {
      throw new IllegalArgumentException("store must not be null");
    }

    boolean initialized = false;

    if (opaqueGeometry != null) {
      store.set(opaqueGeometry.getLocalBounds());
      initialized = true;
    }

    if (waterGeometry != null) {
      if (!initialized) {
        store.set(waterGeometry.getLocalBounds());
        initialized = true;
      } else {
        mergeBoundsInPlace(store, waterGeometry.getLocalBounds());
      }
    }

    if (decorGeometry != null) {
      if (!initialized) {
        store.set(decorGeometry.getLocalBounds());
        initialized = true;
      } else {
        mergeBoundsInPlace(store, decorGeometry.getLocalBounds());
      }
    }

    // Optional: fallback wenn alles null
    if (!initialized) {
      store.setMin(0, 0, 0);
      store.setMax(0, 0, 0);
    }

    return store;
  }

  private void mergeBoundsInPlace(Bounds target, Bounds other) {
    Vector3f min = target.getMin();
    Vector3f max = target.getMax();

    Vector3f omin = other.getMin();
    Vector3f omax = other.getMax();

    if (omin.x < min.x) min.x = omin.x;
    if (omin.y < min.y) min.y = omin.y;
    if (omin.z < min.z) min.z = omin.z;

    if (omax.x > max.x) max.x = omax.x;
    if (omax.y > max.y) max.y = omax.y;
    if (omax.z > max.z) max.z = omax.z;
  }
}
