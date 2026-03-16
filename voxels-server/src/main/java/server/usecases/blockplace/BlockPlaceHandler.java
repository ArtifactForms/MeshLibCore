package server.usecases.blockplace;

import common.network.packets.BlockPlacePacket;
import server.network.ServerConnection;
import server.player.ServerPlayer;
import server.usecases.blockplace.BlockPlace.BlockPlaceRequest;
import server.usecases.blockplace.BlockPlace.BlockPlaceResponse;
import servers.views.BlockPlaceViewImpl;

public class BlockPlaceHandler {

  private final BlockPlace useCase;
  private final ServerConnection connection;

  public BlockPlaceHandler(ServerConnection connection, BlockPlace useCase) {
    this.connection = connection;
    this.useCase = useCase;
  }

  public void handle(BlockPlacePacket packet) {
    BlockPlaceView view = new BlockPlaceViewImpl(connection);
    BlockPlaceRequest request = createRequest(connection.getPlayer(), packet);
    BlockPlaceResponse response = new BlockPlacePresenter(view);

    useCase.execute(request, response);
  }

  private BlockPlaceRequest createRequest(ServerPlayer player, BlockPlacePacket packet) {

    BlockPlaceRequest request =
        new BlockPlaceRequestModel(
            player.getUuid(),
            packet.getX(),
            packet.getY(),
            packet.getZ(),
            packet.getBlockId(),
            player.getX(),
            player.getY(),
            player.getZ(),
            packet.getSelectedSlot());
    return request;
  }
}
