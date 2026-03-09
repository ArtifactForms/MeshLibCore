package client.world;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import common.world.ChunkData;
import common.world.ChunkStatus;
import engine.components.StaticGeometry;
import engine.rendering.Graphics;
import math.Vector3f;

public class Chunk extends ChunkData {
  private static final ExecutorService executorService =
      Executors.newFixedThreadPool(Math.max(1, Runtime.getRuntime().availableProcessors() - 1));

  private final Vector3f worldPosition = new Vector3f();
  private volatile StaticGeometry currentGeometry;
  private Future<StaticGeometry> meshFuture;

  private volatile ChunkStatus status = ChunkStatus.EMPTY;
  private volatile int generation = 0;
  private volatile boolean needsRebuild = false;

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

    this.clear(); // Delete all old block data
    this.currentGeometry = null;
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
              StaticGeometry mesh = mesher.createMesh();
              return (taskGen == generation) ? mesh : null;
            });
  }

  public void updateMesh() {
    if (meshFuture == null || !meshFuture.isDone()) return;
    try {
      StaticGeometry newMesh = meshFuture.get();
      if (newMesh != null) {
        //                if (this.currentGeometry != null) this.currentGeometry.dispose();
        this.currentGeometry = newMesh;
        this.status = ChunkStatus.MESH_READY;
        this.needsRebuild = false;
      } else {
        this.status = ChunkStatus.DATA_READY;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    meshFuture = null;
  }

  public void render(Graphics g) {
    if (currentGeometry != null) {
      g.pushMatrix();
      g.translate(worldPosition.x, worldPosition.y, worldPosition.z);
      currentGeometry.render(g);
      g.popMatrix();
    }
  }

  public void markDirty() {
    this.needsRebuild = true;
  }

  public boolean isDataReady() {
    return status != ChunkStatus.EMPTY;
  }

  public void setDataReady() {
    if (status == ChunkStatus.EMPTY) status = ChunkStatus.DATA_READY;
  }

  public ChunkStatus getStatus() {
    return status;
  }

  public boolean needsRebuild() {
    return needsRebuild;
  }
}