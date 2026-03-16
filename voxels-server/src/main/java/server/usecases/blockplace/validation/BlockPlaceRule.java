package server.usecases.blockplace.validation;

import server.usecases.blockplace.BlockPlace.BlockPlaceRequest;
import server.usecases.blockplace.BlockPlace.BlockPlaceResponse;

public interface BlockPlaceRule {

  boolean validate(BlockPlaceRequest request, BlockPlaceResponse response, short id);
}
