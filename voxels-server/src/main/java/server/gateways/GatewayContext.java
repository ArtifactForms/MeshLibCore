package server.gateways;

public record GatewayContext(
	    WorldGateway world,
	    EventGateway events,
	    PermissionGateway permissions,
	    InventoryGateway inventory,
	    ConfigGateway config,
	    CommandGateway commands,
	    PlayerGateway players,
	    MessageGateway messages,
	    ServerGateway server
	) {}
