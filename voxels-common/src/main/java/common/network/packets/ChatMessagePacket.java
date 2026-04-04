package common.network.packets;

import java.io.IOException;

import common.network.Packet;
import common.network.PacketBuffer;
import common.network.PacketIds;

public class ChatMessagePacket implements Packet {

  private String message;

  public ChatMessagePacket() {}

  public ChatMessagePacket(String message) {
    this.message = message;
  }

  @Override
  public void write(PacketBuffer out) throws IOException {
    out.writeString(this.message);
  }

  @Override
  public void read(PacketBuffer in) throws IOException {
    this.message = in.readString();
  }

  @Override
  public int getId() {
    return PacketIds.CHAT_MESSAGE;
  }

  public String getMessage() {
    return message;
  }
}
