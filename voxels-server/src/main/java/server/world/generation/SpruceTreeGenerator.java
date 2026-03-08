package server.world.generation;

import common.world.BlockType;

public class SpruceTreeGenerator implements TreeGenerator {

  @Override
  public void generate(TreeContext ctx) {

    int baseX = ctx.x;
    int baseY = ctx.y;
    int baseZ = ctx.z;

    int height = 8 + ctx.rng.nextInt(6);

    // trunk
    for (int y = 0; y < height; y++) {
      ctx.chunk.setBlockAt(BlockType.SPRUCE_WOOD, baseX, baseY + y, baseZ);
    }

    int crownStart = baseY + height / 3;

    int radius = 3;
    int layerSkip = 0;

    for (int y = baseY + height; y >= crownStart; y--) {

      if (layerSkip == 1) {
        layerSkip = 0;
        continue;
      }

      generateBranchLayer(ctx, baseX, y, baseZ, radius);

      layerSkip++;

      if (ctx.rng.nextFloat() < 0.6f) {
        radius--;
      }

      radius = Math.max(radius, 1);
    }

    generateTip(ctx, baseX, baseY + height, baseZ);
  }

  private void generateBranchLayer(TreeContext ctx, int cx, int y, int cz, int radius) {

    for (int x = -radius; x <= radius; x++) {
      for (int z = -radius; z <= radius; z++) {

        int dist = Math.abs(x) + Math.abs(z);

        if (dist > radius) continue;

        if (ctx.rng.nextFloat() < 0.15f) continue;

        ctx.chunk.setBlockAt(BlockType.SPRUCE_LEAF, cx + x, y, cz + z);

        if (dist == radius && ctx.rng.nextFloat() < 0.4f) {
          ctx.chunk.setBlockAt(BlockType.SPRUCE_LEAF, cx + x, y - 1, cz + z);
        }
      }
    }
  }

  private void generateTip(TreeContext ctx, int x, int y, int z) {

    int tip = 2 + ctx.rng.nextInt(2);

    for (int i = 1; i <= tip; i++) {
      ctx.chunk.setBlockAt(BlockType.SPRUCE_LEAF, x, y + i, z);
    }
  }
}
