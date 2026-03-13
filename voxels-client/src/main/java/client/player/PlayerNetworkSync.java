package client.player;

import client.network.ClientNetwork;
import common.network.packets.PlayerMovePacket;
import math.Vector3f;

public class PlayerNetworkSync {

    private final ClientPlayer player;
    private final ClientNetwork network;

    public PlayerNetworkSync(ClientPlayer player, ClientNetwork network) {
        this.player = player;
        this.network = network;
    }

    public void update() {

        if(network == null) return;

        Vector3f pos = player.getPosition();

        network.send(
            new PlayerMovePacket(
                pos.x,
                pos.y,
                pos.z,
                player.getYaw(),
                player.getPitch()
            )
        );
    }
}