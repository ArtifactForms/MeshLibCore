package demos.voxels;

import java.util.ArrayList;

import engine.backend.processing.BufferedShape;
import engine.components.StaticGeometry;
import engine.rendering.Material;
import engine.resources.FilterMode;
import engine.resources.Texture;
import math.Vector2f;

public class ChunkMesher {

    private static final int TOP = 0, BOTTOM = 5, FRONT = 1, BACK = 4, RIGHT = 2, LEFT = 3;

    private static float blockSize = 1.0f;
    private static float radius = blockSize * 0.5f;

    private Chunk chunk;
    private ChunkManager chunkManager;
    private BufferedShape shape;
    private StaticGeometry geometry;

    private static ArrayList<Vector2f> uvs;
    public static Material sharedMaterial;
    private static TextureAtlas2 textureAtlas;

    static {
        sharedMaterial = new Material();
        textureAtlas = new TextureAtlas2();
        Texture texture = textureAtlas.getTexture();
        texture.setFilterMode(FilterMode.POINT);
        sharedMaterial.setDiffuseTexture(texture);
        uvs = textureAtlas.getUVCoordinates();
    }

    public ChunkMesher(Chunk chunk, ChunkManager chunkManager) {
        this.chunk = chunk;
        this.chunkManager = chunkManager;
    }

    // ==============================
    // 🟢 MAIN
    // ==============================
    public StaticGeometry createMesh() {
        shape = new BufferedShape(sharedMaterial);
        shape.begin(BufferedShape.QUADS);

        for (int x = 0; x < Chunk.WIDTH; x++) {
            for (int z = 0; z < Chunk.DEPTH; z++) {
                int heightValue = chunk.getHeightValueAt(x, z);
                for (int y = 0; y <= heightValue; y++) {
                    createBlock(x, y, z);
                }
            }
        }

        shape.end();
        geometry = new StaticGeometry(shape.getVBO(), sharedMaterial);
        return geometry;
    }

    // ==============================
    // BLOCK LOGIC
    // ==============================
    private boolean isSolid(int x, int y, int z) {
        if (y < 0) return true;

        if (x >= 0 && x < Chunk.WIDTH && z >= 0 && z < Chunk.DEPTH) {
            return chunk.isBlockSolid(x, y, z);
        }

        int neighborChunkX = chunk.getChunkX() + (x < 0 ? -1 : x >= Chunk.WIDTH ? 1 : 0);
        int neighborChunkZ = chunk.getChunkZ() + (z < 0 ? -1 : z >= Chunk.DEPTH ? 1 : 0);

        Chunk neighborChunk = chunkManager.getChunk(neighborChunkX, neighborChunkZ);
        if (neighborChunk == null || !neighborChunk.isDataReady()) return true;

        int neighborX = (x + Chunk.WIDTH) % Chunk.WIDTH;
        int neighborZ = (z + Chunk.DEPTH) % Chunk.DEPTH;

        return neighborChunk.isBlockSolid(neighborX, y, neighborZ);
    }

    private int getBlockData(int x, int y, int z) {
        if (chunk.isWithinBounds(x, y, z)) return chunk.getBlockData(x, y, z);

        int neighborChunkX = chunk.getChunkX() + (x < 0 ? -1 : x >= Chunk.WIDTH ? 1 : 0);
        int neighborChunkZ = chunk.getChunkZ() + (z < 0 ? -1 : z >= Chunk.DEPTH ? 1 : 0);

        Chunk neighborChunk = chunkManager.getChunk(neighborChunkX, neighborChunkZ);
        if (neighborChunk != null && neighborChunk.isDataReady()) {
            int neighborX = (x + Chunk.WIDTH) % Chunk.WIDTH;
            int neighborZ = (z + Chunk.DEPTH) % Chunk.DEPTH;
            if (neighborChunk.isWithinBounds(neighborX, y, neighborZ))
                return neighborChunk.getBlockData(neighborX, y, neighborZ);
        }
        return BlockType.AIR.getId();
    }

    public boolean shouldRender(int id, int x, int y, int z) {
        int idAbove = getBlockData(x, y + 1, z);
        int idCurrent = getBlockData(x, y, z);

        if (id != BlockType.WATER.getId() && idCurrent == BlockType.WATER.getId()) return true;
        if (idCurrent != BlockType.WATER.getId() && id == BlockType.WATER.getId()) return true;
        if (id == BlockType.WATER.getId() && idAbove == BlockType.WATER.getId()) return false;

        return !isSolid(x, y, z);
    }

    private void createBlock(int x, int y, int z) {
        int blockId = chunk.getBlockData(x, y, z);
        if (blockId == BlockType.AIR.getId()) return;

        if (blockId == BlockType.GRASS.getId()) {
            createBillBoard(x, y, z);
        } else {
            createBaseBlock(x, y, z);
        }
    }

    private void createBaseBlock(int x, int y, int z) {
        int blockId = chunk.getBlockData(x, y, z);
        if (shouldRender(blockId, x, y + 1, z)) addFace(TOP, x, y, z);
        if (shouldRender(blockId, x, y - 1, z)) addFace(BOTTOM, x, y, z);
        if (shouldRender(blockId, x, y, z + 1)) addFace(FRONT, x, y, z);
        if (shouldRender(blockId, x, y, z - 1)) addFace(BACK, x, y, z);
        if (shouldRender(blockId, x + 1, y, z)) addFace(RIGHT, x, y, z);
        if (shouldRender(blockId, x - 1, y, z)) addFace(LEFT, x, y, z);
    }

    private void addFace(int face, int x, int y, int z) {
        Vector2f[] uv = textureAtlas.getUVCoordinates(chunk.getBlockData(x, y, z), face);
        float px = radius + x, nx = -radius + x;
        float py = -radius - y, ny = radius - y;
        float pz = radius + z, nz = -radius + z;

        switch (face) {
            case TOP -> {
                shape.vertex(px, py, nz, uv[0].x, uv[0].y);
                shape.vertex(nx, py, nz, uv[1].x, uv[1].y);
                shape.vertex(nx, py, pz, uv[2].x, uv[2].y);
                shape.vertex(px, py, pz, uv[3].x, uv[3].y);
            }
            case BOTTOM -> {
                shape.vertex(px, ny, nz, uv[0].x, uv[0].y);
                shape.vertex(px, ny, pz, uv[1].x, uv[1].y);
                shape.vertex(nx, ny, pz, uv[2].x, uv[2].y);
                shape.vertex(nx, ny, nz, uv[3].x, uv[3].y);
            }
            case FRONT -> {
                shape.vertex(px, py, pz, uv[0].x, uv[0].y);
                shape.vertex(nx, py, pz, uv[1].x, uv[1].y);
                shape.vertex(nx, ny, pz, uv[2].x, uv[2].y);
                shape.vertex(px, ny, pz, uv[3].x, uv[3].y);
            }
            case BACK -> {
                shape.vertex(nx, py, nz, uv[0].x, uv[0].y);
                shape.vertex(px, py, nz, uv[1].x, uv[1].y);
                shape.vertex(px, ny, nz, uv[2].x, uv[2].y);
                shape.vertex(nx, ny, nz, uv[3].x, uv[3].y);
            }
            case RIGHT -> {
                shape.vertex(px, py, nz, uv[0].x, uv[0].y);
                shape.vertex(px, py, pz, uv[1].x, uv[1].y);
                shape.vertex(px, ny, pz, uv[2].x, uv[2].y);
                shape.vertex(px, ny, nz, uv[3].x, uv[3].y);
            }
            case LEFT -> {
                shape.vertex(nx, py, pz, uv[0].x, uv[0].y);
                shape.vertex(nx, py, nz, uv[1].x, uv[1].y);
                shape.vertex(nx, ny, nz, uv[2].x, uv[2].y);
                shape.vertex(nx, ny, pz, uv[3].x, uv[3].y);
            }
        }
    }

    private void createBillBoard(int x, int y, int z) {
        int blockId = chunk.getBlockData(x, y, z);
        int[] uvIndices = textureAtlas.getUVIndices(blockId, 0);
        Vector2f uv0 = uvs.get(uvIndices[0]);
        Vector2f uv1 = uvs.get(uvIndices[1]);
        Vector2f uv2 = uvs.get(uvIndices[2]);
        Vector2f uv3 = uvs.get(uvIndices[3]);

        shape.vertex(radius + x, -radius - y, radius + z, uv0.x, uv0.y);
        shape.vertex(-radius + x, -radius - y, -radius + z, uv1.x, uv1.y);
        shape.vertex(-radius + x, radius - y, -radius + z, uv2.x, uv2.y);
        shape.vertex(radius + x, radius - y, radius + z, uv3.x, uv3.y);

        shape.vertex(radius + x, -radius - y, -radius + z, uv0.x, uv0.y);
        shape.vertex(-radius + x, -radius - y, radius + z, uv1.x, uv1.y);
        shape.vertex(-radius + x, radius - y, radius + z, uv2.x, uv2.y);
        shape.vertex(radius + x, radius - y, -radius + z, uv3.x, uv3.y);
    }
}