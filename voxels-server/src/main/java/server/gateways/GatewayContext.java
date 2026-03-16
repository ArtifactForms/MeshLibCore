package server.gateways;

public record GatewayContext(
	    WorldGateway world,
	    EventGateway events,
	    PermissionGateway permissions,
	    InventoryGateway inventory
//	    PlayerGateway players,
	) {}
