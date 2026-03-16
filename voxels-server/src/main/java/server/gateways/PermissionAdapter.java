package server.gateways;

import java.util.UUID;

import server.permissions.PermissionService;

public class PermissionAdapter implements PermissionGateway {
    private final PermissionService service;

    public PermissionAdapter(PermissionService service) {
        this.service = service;
    }

    @Override
    public boolean hasPermission(UUID playerId, String permission) {
        // Hier könntest du sogar Logging oder Caching einbauen, 
        // ohne den Core zu berühren.
        return service.hasPermission(playerId, permission);
    }
}