package common.network.packets;

import java.io.IOException;
import java.util.UUID;

import common.network.Packet;
import common.network.PacketBuffer;
import common.network.PacketIds;

public class PrivateMessagePacket implements Packet {

  private UUID sender;

  private UUID target;

  private String message;

  public PrivateMessagePacket() {}

  public PrivateMessagePacket(UUID sender, UUID target, String message) {
    this.sender = sender;
    this.target = target;
    this.message = message;
  }

  @Override
  public void write(PacketBuffer out) throws IOException {
    out.writeUuid(sender);
    out.writeUuid(target);
    out.writeString(message);
  }

  @Override
  public void read(PacketBuffer in) throws IOException {
    this.sender = in.readUuid();
    this.target = in.readUuid();
    this.message = in.readString();
  }

  @Override
  public int getId() {
    return PacketIds.PRIVATE_MESSAGE;
  }

  public UUID getSender() {
    return sender;
  }

  public UUID getTarget() {
    return target;
  }

  public String getMessae() {
    return message;
  }
}
