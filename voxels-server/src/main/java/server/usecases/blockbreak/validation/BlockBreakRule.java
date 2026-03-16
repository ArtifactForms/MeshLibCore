package server.usecases.blockbreak.validation;

import server.usecases.blockbreak.BlockBreak.BlockBreakRequest;
import server.usecases.blockbreak.BlockBreak.BlockBreakResponse;

public interface BlockBreakRule {

  boolean validate(BlockBreakRequest request, BlockBreakResponse response, short id);
}
