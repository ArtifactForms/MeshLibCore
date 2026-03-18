package common.network;

/**
 * A data transfer object (DTO) representing public server information.
 * This record is immutable and sent from the server to the client 
 * during the handshake or server-list ping phase.
 *
 * @param maxPlayers    The maximum number of concurrent players allowed.
 * @param motd          The "Message of the Day" displayed in the server browser.
 * @param viewDistance  The chunk radius that the server will send to the client.
 */
public record ServerInfo(
    int maxPlayers,
    String motd,
    int viewDistance
) {}