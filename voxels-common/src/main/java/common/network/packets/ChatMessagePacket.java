package common.network.packets;

import java.io.IOException;
import java.util.UUID;

import common.network.Packet;
import common.network.PacketBuffer;
import common.network.PacketIds;

public class ChatMessagePacket implements Packet {

  private UUID sender;
  private String message;

  public ChatMessagePacket() {}

  public ChatMessagePacket(String message) {
    this.message = message;
  }

  @Override
  public void write(PacketBuffer out) throws IOException {
    out.writeUuid(sender);
    out.writeString(this.message);
  }

  @Override
  public void read(PacketBuffer in) throws IOException {
    this.sender = in.readUuid();
    this.message = in.readString();
  }

  @Override
  public int getId() {
    return PacketIds.CHAT_MESSAGE;
  }

  public UUID getSender() {
    return sender;
  }

  public String getMessage() {
    return message;
  }
}
