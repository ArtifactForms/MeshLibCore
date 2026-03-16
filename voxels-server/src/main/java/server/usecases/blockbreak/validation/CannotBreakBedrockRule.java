package server.usecases.blockbreak.validation;

import server.usecases.blockbreak.BlockBreak.BlockBreakRequest;
import server.usecases.blockbreak.BlockBreak.BlockBreakResponse;

public class CannotBreakBedrockRule implements BlockBreakRule {

  private final short bedrockId;

  public CannotBreakBedrockRule(short bedrockId) {
    this.bedrockId = bedrockId;
  }

  @Override
  public boolean validate(BlockBreakRequest request, BlockBreakResponse response, short id) {
    if (id == bedrockId) {
      response.onCannotBreakBedrock(request.getX(), request.getY(), request.getZ(), id);
      return false;
    }
    return true;
  }
}
