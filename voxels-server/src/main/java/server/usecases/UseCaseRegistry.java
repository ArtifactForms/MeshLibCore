package server.usecases;

import java.util.HashMap;
import java.util.Map;

import server.gateways.GatewayContext;
import server.usecases.blockbreak.BlockBreak;
import server.usecases.blockbreak.BlockBreakUseCase;
import server.usecases.blockplace.BlockPlace;
import server.usecases.blockplace.BlockPlaceUseCase;
import server.usecases.changegamemode.ChangeGameModeUseCase;
import server.usecases.changegamemode.ChangeGamemode;
import server.usecases.inventoryclear.InventoryClear;
import server.usecases.inventoryclear.InventoryClearUseCase;

public class UseCaseRegistry {

  private final Map<Class<?>, Object> useCases = new HashMap<>();

  public UseCaseRegistry(GatewayContext context) {
    register(BlockBreak.class, new BlockBreakUseCase(context));
    register(BlockPlace.class, new BlockPlaceUseCase(context));
    register(ChangeGamemode.class, new ChangeGameModeUseCase(context));
    register(InventoryClear.class, new InventoryClearUseCase(context));
  }

  private <T> void register(Class<T> type, T instance) {
    useCases.put(type, instance);
  }

  @SuppressWarnings("unchecked")
  public <T> T get(Class<T> type) {
    return (T) useCases.get(type);
  }
}
