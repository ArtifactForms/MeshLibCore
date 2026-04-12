package server.modules.edit;

import java.util.Collection;
import java.util.List;

import common.game.block.BlockRegistry;
import common.game.block.BlockType;
import common.world.Location;
import server.commands.AbstractCommand;
import server.commands.CommandArgument;
import server.commands.CommandContext;
import server.gateways.PlayerGateway;
import voxels.Voxel;
import voxels.VoxelCircleCreator;

public class CircleCommand extends AbstractCommand {

  private final PlayerGateway players;

  private final WorldEditConfig config;

  public CircleCommand(PlayerGateway players, WorldEditConfig config) {
    this.players = players;
    this.config = config;
  }

  @Override
  public String getPermission() {
    return "";
  }

  @Override
  public void execute(CommandContext ctx) {
    Location location = players.getLocation(ctx.getPlayer());

    List<String> args = ctx.getArgs();

    if (args.size() < 1) {
      ctx.reply("Missing argument: radius");
      return;
    }

    if (args.size() < 2) {
      ctx.reply("Missing argument: block_type");
      return;
    }

    int radius;

    try {
      radius = Integer.parseInt(args.get(0));
    } catch (NumberFormatException e) {
      ctx.reply("An error occured: Wrong number format.");
      return;
    }

    if (radius > config.getMaxCircleRadius()) {
      ctx.reply("Max circle radius: " + config.getMaxCircleRadius());
      return;
    }

    String name = args.get(1);
    BlockType blockType = BlockRegistry.get(name);
    if (blockType == null) {
      ctx.reply("Unknown block type: " + name);
      return;
    }
    short id = blockType.getId();

    ctx.reply("Start creating...");

    int bx = location.getBlockX();
    int by = location.getBlockY();
    int bz = location.getBlockZ();

    Voxel center = new Voxel(bx, by, bz);
    VoxelCircleCreator creator = new VoxelCircleCreator(center, radius);

    Collection<Voxel> voxels = creator.create();

    ChunkTransaction tx = new ChunkTransaction();

    for (Voxel v : voxels) {
      tx.setBlock(v.x(), v.y(), v.z(), id);
    }

    tx.commit(ctx.getServer().getWorld(), ctx.getServer());

    ctx.reply("Circle created with " + voxels.size() + " blocks.");
  }

  @Override
  public String getName() {
    return "/circle";
  }

  @Override
  public CommandArgument[] getArgumentLabels() {
    return new CommandArgument[] {
      new CommandArgument("radius", true), 
      new CommandArgument("block_type", true)
    };
  }

  @Override
  public String getDescription() {
    return "";
  }
}
