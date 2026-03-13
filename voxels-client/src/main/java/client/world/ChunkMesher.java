package client.world;

import java.util.ArrayList;

import client.resources.TextureAtlas;
import client.ui.GameTextures;
import common.game.block.Blocks;
import engine.backend.processing.BufferedShape;
import engine.components.StaticGeometry;
import engine.rendering.Material;
import engine.resources.FilterMode;
import engine.resources.Texture;
import engine.resources.TextureWrapMode;
import math.Vector2f;

public class ChunkMesher {

    private static final int TOP = 0, BOTTOM = 1, FRONT = 2, BACK = 3, RIGHT = 4, LEFT = 5;
    private static float radius = 0.5f; 

    private Chunk chunk;
    private ChunkManager chunkManager;
    
    // Zwei Shapes für die Trennung von opaken und transparenten Objekten
    private BufferedShape opaqueShape;
    private BufferedShape waterShape;
    private BufferedShape currentShape; // Zeiger auf das aktuell aktive Mesh

    private static ArrayList<Vector2f> uvs;
    public static Material sharedMaterial;
    private static TextureAtlas textureAtlas;

    static {
        sharedMaterial = new Material();
        textureAtlas = GameTextures.TEXTURE_ATLAS;
        Texture texture = textureAtlas.getTexture();
        texture.setFilterMode(FilterMode.POINT); 
        texture.setTextureWrapMode(TextureWrapMode.REPEAT);
        sharedMaterial.setDiffuseTexture(texture);
        uvs = textureAtlas.getUVCoordinates();
    }

    // Container-Klasse für die Rückgabe beider Meshes
    public static class MeshResult {
        public final StaticGeometry opaque;
        public final StaticGeometry water;

        public MeshResult(StaticGeometry opaque, StaticGeometry water) {
            this.opaque = opaque;
            this.water = water;
        }
    }

    public ChunkMesher(Chunk chunk, ChunkManager chunkManager) {
        this.chunk = chunk;
        this.chunkManager = chunkManager;
    }

    public MeshResult createMesh() {
        opaqueShape = new BufferedShape(sharedMaterial);
        waterShape = new BufferedShape(sharedMaterial);

        opaqueShape.begin(BufferedShape.QUADS);
        waterShape.begin(BufferedShape.QUADS);

        greedyTopFaces(); 
        generateSideFaces();

        opaqueShape.end();
        waterShape.end();

        return new MeshResult(
            new StaticGeometry(opaqueShape.getVBO(), sharedMaterial),
            new StaticGeometry(waterShape.getVBO(), sharedMaterial)
        );
    }
    
    private void greedyTopFaces() {
        for (int y = 0; y < Chunk.HEIGHT; y++) {
            boolean[][] visited = new boolean[Chunk.WIDTH][Chunk.DEPTH];
            for (int x = 0; x < Chunk.WIDTH; x++) {
                for (int z = 0; z < Chunk.DEPTH; z++) {
                    if (visited[x][z]) continue;
                    
                    int blockId = chunk.getBlockId(x, y, z);
                    if (blockId == Blocks.AIR.getId() || !shouldRender(blockId, x, y + 1, z)) continue;

                    // Entscheide, in welches Mesh dieser Block gehört
                    currentShape = (blockId == Blocks.WATER.getId()) ? waterShape : opaqueShape;

                    int width = 1;
                    while (x + width < Chunk.WIDTH && !visited[x + width][z] && 
                           chunk.getBlockId(x + width, y, z) == blockId && shouldRender(blockId, x + width, y + 1, z)) {
                        width++;
                    }

                    int depth = 1;
                    outer:
                    while (z + depth < Chunk.DEPTH) {
                        for (int k = 0; k < width; k++) {
                            if (visited[x + k][z + depth] || chunk.getBlockId(x + k, y, z + depth) != blockId || 
                                !shouldRender(blockId, x + k, y + 1, z + depth)) break outer;
                        }
                        depth++;
                    }

                    for (int dx = 0; dx < width; dx++)
                        for (int dz = 0; dz < depth; dz++)
                            visited[x + dx][z + dz] = true;

                    addTopQuad(x, y, z, width, depth, blockId);
                }
            }
        }
    }

    private void generateSideFaces() {
        for (int x = 0; x < Chunk.WIDTH; x++) {
            for (int z = 0; z < Chunk.DEPTH; z++) {
                for (int y = 0; y < Chunk.HEIGHT; y++) {
                    int blockId = chunk.getBlockId(x, y, z);
                    if (blockId == Blocks.AIR.getId()) continue;

                    // Entscheide, in welches Mesh dieser Block gehört
                    currentShape = (blockId == Blocks.WATER.getId()) ? waterShape : opaqueShape;

                    if (blockId == Blocks.GRASS.getId()) { 
                        createBillBoard(x, y, z); 
                        continue; 
                    }

                    if (shouldRender(blockId, x, y - 1, z)) addFace(BOTTOM, x, y, z);
                    if (shouldRender(blockId, x, y, z + 1)) addFace(FRONT, x, y, z);
                    if (shouldRender(blockId, x, y, z - 1)) addFace(BACK, x, y, z);
                    if (shouldRender(blockId, x + 1, y, z)) addFace(RIGHT, x, y, z);
                    if (shouldRender(blockId, x - 1, y, z)) addFace(LEFT, x, y, z);
                }
            }
        }
    }

    private void addTopQuad(int x, int y, int z, int width, int depth, int blockId) {
        Vector2f[] uv = textureAtlas.getUVCoordinates(blockId, TOP);
        float minX = x - radius;
        float minZ = z - radius;
        float yPos = -radius - y;

        for (int dx = 0; dx < width; dx++) {
            for (int dz = 0; dz < depth; dz++) {
                int bx = x + dx, bz = z + dz;
                float a0 = aoBrightness(vertexAO(isSolid(bx-1, y+1, bz), isSolid(bx, y+1, bz-1), isSolid(bx-1, y+1, bz-1)));
                float a1 = aoBrightness(vertexAO(isSolid(bx+1, y+1, bz), isSolid(bx, y+1, bz-1), isSolid(bx+1, y+1, bz-1)));
                float a2 = aoBrightness(vertexAO(isSolid(bx-1, y+1, bz+1), isSolid(bx, y+1, bz+1), isSolid(bx-1, y+1, bz+1)));
                float a3 = aoBrightness(vertexAO(isSolid(bx+1, y+1, bz+1), isSolid(bx, y+1, bz+1), isSolid(bx+1, y+1, bz+1)));

                drawCorrectedQuad(minX + dx, yPos, minZ + dz, 1, 1, uv, 1.0f, a0, a1, a2, a3, TOP);
            }
        }
    }

    private void addFace(int face, int x, int y, int z) {
        int blockId = chunk.getBlockId(x, y, z);
        Vector2f[] uv = textureAtlas.getUVCoordinates(blockId, face);
        
        if (face != TOP && face != BOTTOM) {
            uv = new Vector2f[]{new Vector2f(uv[0].x, uv[2].y), new Vector2f(uv[1].x, uv[3].y), new Vector2f(uv[2].x, uv[0].y), new Vector2f(uv[3].x, uv[1].y)};
        } 

        float light = switch (face) { case FRONT, BACK -> 0.85f; case LEFT, RIGHT -> 0.7f; case BOTTOM -> 0.5f; default -> 1.0f; };
        float a0, a1, a2, a3;

        switch (face) {
            case FRONT -> {
                a0 = aoBrightness(vertexAO(isSolid(x+1, y, z+1), isSolid(x, y-1, z+1), isSolid(x+1, y-1, z+1)));
                a1 = aoBrightness(vertexAO(isSolid(x-1, y, z+1), isSolid(x, y-1, z+1), isSolid(x-1, y-1, z+1)));
                a2 = aoBrightness(vertexAO(isSolid(x-1, y, z+1), isSolid(x, y+1, z+1), isSolid(x-1, y+1, z+1)));
                a3 = aoBrightness(vertexAO(isSolid(x+1, y, z+1), isSolid(x, y+1, z+1), isSolid(x+1, y+1, z+1)));
                drawCorrectedQuad(x-radius, -radius-y, z+radius, 1, 1, uv, light, a0, a1, a2, a3, FRONT);
            }
            case BACK -> {
                a0 = aoBrightness(vertexAO(isSolid(x-1, y, z-1), isSolid(x, y-1, z-1), isSolid(x-1, y-1, z-1)));
                a1 = aoBrightness(vertexAO(isSolid(x+1, y, z-1), isSolid(x, y-1, z-1), isSolid(x+1, y-1, z-1)));
                a2 = aoBrightness(vertexAO(isSolid(x+1, y, z-1), isSolid(x, y+1, z-1), isSolid(x+1, y+1, z-1)));
                a3 = aoBrightness(vertexAO(isSolid(x-1, y, z-1), isSolid(x, y+1, z-1), isSolid(x-1, y+1, z-1)));
                drawCorrectedQuad(x-radius, -radius-y, z-radius, 1, 1, uv, light, a0, a1, a2, a3, BACK);
            }
            case RIGHT -> {
                a0 = aoBrightness(vertexAO(isSolid(x+1, y, z-1), isSolid(x+1, y-1, z), isSolid(x+1, y-1, z-1)));
                a1 = aoBrightness(vertexAO(isSolid(x+1, y, z+1), isSolid(x+1, y-1, z), isSolid(x+1, y-1, z+1)));
                a2 = aoBrightness(vertexAO(isSolid(x+1, y, z+1), isSolid(x+1, y+1, z), isSolid(x+1, y+1, z+1)));
                a3 = aoBrightness(vertexAO(isSolid(x+1, y, z-1), isSolid(x+1, y+1, z), isSolid(x+1, y+1, z-1)));
                drawCorrectedQuad(x+radius, -radius-y, z-radius, 1, 1, uv, light, a0, a1, a2, a3, RIGHT);
            }
            case LEFT -> {
                a0 = aoBrightness(vertexAO(isSolid(x-1, y, z+1), isSolid(x-1, y-1, z), isSolid(x-1, y-1, z+1)));
                a1 = aoBrightness(vertexAO(isSolid(x-1, y, z-1), isSolid(x-1, y-1, z), isSolid(x-1, y-1, z-1)));
                a2 = aoBrightness(vertexAO(isSolid(x-1, y, z-1), isSolid(x-1, y+1, z), isSolid(x-1, y+1, z-1)));
                a3 = aoBrightness(vertexAO(isSolid(x-1, y, z+1), isSolid(x-1, y+1, z), isSolid(x-1, y+1, z+1)));
                drawCorrectedQuad(x-radius, -radius-y, z-radius, 1, 1, uv, light, a0, a1, a2, a3, LEFT);
            }
            case BOTTOM -> drawCorrectedQuad(x-radius, radius-y, z-radius, 1, 1, uv, light, 1, 1, 1, 1, BOTTOM);
        }
    }

    private void drawCorrectedQuad(float x, float y, float z, float w, float d, Vector2f[] uv, float light, float a0, float a1, float a2, float a3, int face) {
        boolean flip = (a0 + a3 > a1 + a2); 

        if (face == TOP) {
            if (!flip) {
                drawV(x + w, y, z, uv[0], a1 * light); drawV(x, y, z, uv[1], a0 * light);
                drawV(x, y, z + d, uv[2], a2 * light); drawV(x + w, y, z + d, uv[3], a3 * light);
            } else {
                drawV(x, y, z, uv[1], a0 * light); drawV(x, y, z + d, uv[2], a2 * light);
                drawV(x + w, y, z + d, uv[3], a3 * light); drawV(x + w, y, z, uv[0], a1 * light);
            }
        } else if (face == FRONT) {
            if (!flip) {
                drawV(x + w, y, z, uv[0], a0 * light); drawV(x, y, z, uv[1], a1 * light);
                drawV(x, y + 1, z, uv[2], a2 * light); drawV(x + w, y + 1, z, uv[3], a3 * light);
            } else {
                drawV(x, y, z, uv[1], a1 * light); drawV(x, y + 1, z, uv[2], a2 * light);
                drawV(x + w, y + 1, z, uv[3], a3 * light); drawV(x + w, y, z, uv[0], a0 * light);
            }
        } else if (face == BACK) {
            if (!flip) {
                drawV(x, y, z, uv[0], a0 * light); drawV(x + w, y, z, uv[1], a1 * light);
                drawV(x + w, y + 1, z, uv[2], a2 * light); drawV(x, y + 1, z, uv[3], a3 * light);
            } else {
                drawV(x + w, y, z, uv[1], a1 * light); drawV(x + w, y + 1, z, uv[2], a2 * light);
                drawV(x, y + 1, z, uv[3], a3 * light); drawV(x, y, z, uv[0], a0 * light);
            }
        } else if (face == RIGHT) {
            if (!flip) {
                drawV(x, y, z, uv[0], a0 * light); drawV(x, y, z + d, uv[1], a1 * light);
                drawV(x, y + 1, z + d, uv[2], a2 * light); drawV(x, y + 1, z, uv[3], a3 * light);
            } else {
                drawV(x, y, z + d, uv[1], a1 * light); drawV(x, y + 1, z + d, uv[2], a2 * light);
                drawV(x, y + 1, z, uv[3], a3 * light); drawV(x, y, z, uv[0], a0 * light);
            }
        } else if (face == LEFT) {
            if (!flip) {
                drawV(x, y, z + d, uv[0], a0 * light); drawV(x, y, z, uv[1], a1 * light);
                drawV(x, y + 1, z, uv[2], a2 * light); drawV(x, y + 1, z + d, uv[3], a3 * light);
            } else {
                drawV(x, y, z, uv[1], a1 * light); drawV(x, y + 1, z, uv[2], a2 * light);
                drawV(x, y + 1, z + d, uv[3], a3 * light); drawV(x, y, z + d, uv[0], a0 * light);
            }
        } else if (face == BOTTOM) {
            drawV(x+w, y, z, uv[0], light); drawV(x+w, y, z+d, uv[1], light);
            drawV(x, y, z+d, uv[2], light); drawV(x, y, z, uv[3], light);
        }
    }

    private void drawV(float x, float y, float z, Vector2f uv, float c) {
        currentShape.color(c, c, c); 
        currentShape.vertex(x, y, z, uv.x, uv.y);
    }

    private boolean isSolid(int x, int y, int z) {
        if (y < 0 || y >= Chunk.HEIGHT) return false;

        if (x >= 0 && x < Chunk.WIDTH && z >= 0 && z < Chunk.DEPTH) {
            return chunk.isBlockSolid(x, y, z);
        }

        Chunk neighbor = chunkManager.getChunk(
            chunk.getChunkX() + (x < 0 ? -1 : x >= Chunk.WIDTH ? 1 : 0),
            chunk.getChunkZ() + (z < 0 ? -1 : z >= Chunk.DEPTH ? 1 : 0)
        );

        if (neighbor == null || !neighbor.isDataReady()) return false;

        return neighbor.isBlockSolid(
            Math.floorMod(x, Chunk.WIDTH),
            y,
            Math.floorMod(z, Chunk.DEPTH)
        );
    }

    private int getBlockData(int x, int y, int z) {
        if (x >= 0 && x < Chunk.WIDTH && y >= 0 && y < Chunk.HEIGHT && z >= 0 && z < Chunk.DEPTH) {
            return chunk.getBlockId(x, y, z);
        }

        Chunk neighbor = chunkManager.getChunk(
            chunk.getChunkX() + (x < 0 ? -1 : x >= Chunk.WIDTH ? 1 : 0),
            chunk.getChunkZ() + (z < 0 ? -1 : z >= Chunk.DEPTH ? 1 : 0)
        );

        if (neighbor != null && neighbor.isDataReady()) {
            return neighbor.getBlockId(Math.floorMod(x, Chunk.WIDTH), y, Math.floorMod(z, Chunk.DEPTH));
        }

        return Blocks.AIR.getId();
    }

    public boolean shouldRender(int myId, int x, int y, int z) {
        if (y < 0) return false;
        if (y >= Chunk.HEIGHT) return true;
        int neighborId = getBlockData(x, y, z);

        // Spezielle Logik für Wasser:
        // Wasser zeigt Faces gegen Luft und andere Blöcke, aber nicht gegen Wasser.
        if (myId == Blocks.WATER.getId()) {
            return neighborId != Blocks.WATER.getId() && neighborId == Blocks.AIR.getId();
        }

        // Andere Blöcke zeigen Faces gegen Wasser (da Wasser transparent ist)
        if (neighborId == Blocks.WATER.getId()) return true;

        return !isSolid(x, y, z);
    }

    private int vertexAO(boolean s1, boolean s2, boolean c) {
        if (s1 && s2) return 0;
        return 3 - ( (s1?1:0) + (s2?1:0) + (c?1:0) );
    }

    private float aoBrightness(int ao) { 
        return 0.6f + (ao / 3.0f) * 0.4f; 
    }

    private void createBillBoard(int x, int y, int z) {
        // Billboards (Gras) landen immer im opaken Mesh (oder einem Cutout-Mesh, falls vorhanden)
        currentShape = opaqueShape;
        int[] uvIdx = textureAtlas.getUVIndices(chunk.getBlockId(x, y, z), 0);
        Vector2f[] vuv = {uvs.get(uvIdx[0]), uvs.get(uvIdx[1]), uvs.get(uvIdx[2]), uvs.get(uvIdx[3])};
        currentShape.color(0.9f, 0.9f, 0.9f);
        currentShape.vertex(radius+x, -radius-y, radius+z, vuv[0].x, vuv[0].y); currentShape.vertex(-radius+x, -radius-y, -radius+z, vuv[1].x, vuv[1].y);
        currentShape.vertex(-radius+x, radius-y, -radius+z, vuv[2].x, vuv[2].y); currentShape.vertex(radius+x, radius-y, radius+z, vuv[3].x, vuv[3].y);
        currentShape.vertex(radius+x, -radius-y, -radius+z, vuv[0].x, vuv[0].y); currentShape.vertex(-radius+x, -radius-y, radius+z, vuv[1].x, vuv[1].y);
        currentShape.vertex(-radius+x, radius-y, radius+z, vuv[2].x, vuv[2].y); currentShape.vertex(radius+x, radius-y, -radius+z, vuv[3].x, vuv[3].y);
    }
}