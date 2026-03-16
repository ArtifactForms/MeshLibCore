package server.usecases.blockplace;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import common.game.block.Blocks;
import server.events.events.BlockPlaceEvent;
import server.gateways.EventGateway;
import server.gateways.GatewayContext;
import server.gateways.PermissionGateway;
import server.gateways.WorldGateway;
import server.usecases.blockplace.validation.BlockPlaceRule;
import server.usecases.blockplace.validation.ItemMatchRule;
import server.usecases.blockplace.validation.PermissionRule;

public class BlockPlaceUseCase implements BlockPlace {

  private WorldGateway worldGateway;
  private EventGateway eventGateway;
  private PermissionGateway permissionGateway;

  private final List<BlockPlaceRule> rules = new ArrayList<>();

  public BlockPlaceUseCase(GatewayContext context) {
    worldGateway = context.world();
    eventGateway = context.events();
    permissionGateway = context.permissions();

    rules.add(new PermissionRule(permissionGateway));
    rules.add(new ItemMatchRule(context.inventory()));
    //    rules.add(new DistanceRule(5.0));
    //    rules.add(new PlacementCollisionRule());
    //    rules.add(new BlockReplaceableRule()); // Prüft, ob an der Stelle Luft/Gras ist
  }

  @Override
  public void execute(BlockPlaceRequest request, BlockPlaceResponse response) {
    UUID player = request.getPlayer();
    int x = request.getX();
    int y = request.getY();
    int z = request.getZ();
    short blockId = request.getBlockId();

    short currentBlock = worldGateway.getBlock(x, y, z);

    if (isInvalid(request, response, currentBlock)) {
      return;
    }

    if (currentBlock != Blocks.AIR.getId()) {
      response.onCancelled(x, y, z, currentBlock);
      return;
    }

    BlockPlaceEvent event = new BlockPlaceEvent(player, x, y, z, blockId);
    eventGateway.fire(event);

    if (event.isCancelled()) {
      response.onCancelled(x, y, z, currentBlock);
      return;
    }

    worldGateway.setBlock(x, y, z, blockId);
    response.onBlockPlaced(x, y, z, blockId);
  }

  private boolean isInvalid(BlockPlaceRequest request, BlockPlaceResponse response, short id) {
    for (BlockPlaceRule rule : rules) {
      if (!rule.validate(request, response, id)) {
        return true;
      }
    }
    return false;
  }
}
