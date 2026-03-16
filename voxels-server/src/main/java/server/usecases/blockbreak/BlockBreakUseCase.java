package server.usecases.blockbreak;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import common.game.block.Blocks;
import server.events.events.BlockBreakEvent;
import server.gateways.EventGateway;
import server.gateways.GatewayContext;
import server.gateways.PermissionGateway;
import server.gateways.WorldGateway;
import server.usecases.blockbreak.validation.BlockBreakRule;
import server.usecases.blockbreak.validation.CannotBreakBedrockRule;
import server.usecases.blockbreak.validation.DistanceRule;
import server.usecases.blockbreak.validation.PermissionRule;

public class BlockBreakUseCase implements BlockBreak {

  private WorldGateway worldGateway;
  private EventGateway eventGateway;
  private PermissionGateway permissionGateway;
  private List<BlockBreakRule> rules;

  public BlockBreakUseCase(GatewayContext gateways) {
    this.worldGateway = gateways.world();
    this.eventGateway = gateways.events();
    this.permissionGateway = gateways.permissions();
    initializeRules();
  }

  private void initializeRules() {
    this.rules = new ArrayList<>();
    rules.add(new CannotBreakBedrockRule(Blocks.BEDROCK.getId()));
    rules.add(new DistanceRule(7));
    rules.add(new PermissionRule(permissionGateway));
  }

  @Override
  public void execute(BlockBreakRequest request, BlockBreakResponse response) {
    UUID player = request.getPlayer();
    int x = request.getX();
    int y = request.getY();
    int z = request.getZ();

    short oldId = worldGateway.getBlock(x, y, z);

    if (isInvalid(request, response, oldId)) {
      return;
    }

    BlockBreakEvent event = new BlockBreakEvent(player, x, y, z);
    eventGateway.fire(event);

    if (event.isCancelled()) {
      response.onCancelled(x, y, z, oldId);
      return;
    }

    worldGateway.setBlock(x, y, z, Blocks.AIR.getId());

    response.onBlockBroken(x, y, z, Blocks.AIR.getId());
  }

  private boolean isInvalid(BlockBreakRequest request, BlockBreakResponse response, short id) {
    for (BlockBreakRule rule : rules) {
      if (!rule.validate(request, response, id)) {
        return true;
      }
    }
    return false;
  }
}
