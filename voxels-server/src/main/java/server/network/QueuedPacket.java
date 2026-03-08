package server.network;

import common.network.Packet;

public record QueuedPacket(ServerConnection connection, Packet packet) {}