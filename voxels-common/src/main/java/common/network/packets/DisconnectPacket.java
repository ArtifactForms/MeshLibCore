package common.network.packets;

import java.io.IOException;

import common.network.Packet;
import common.network.PacketBuffer;
import common.network.PacketIds;
import common.network.ServerToClientPacket;

public class DisconnectPacket implements Packet, ServerToClientPacket {

  private String reason;

  public DisconnectPacket() {}

  public DisconnectPacket(String reason) {
    this.reason = reason;
  }

  @Override
  public void write(PacketBuffer out) throws IOException {
    out.writeString(reason);
  }

  @Override
  public void read(PacketBuffer in) throws IOException {
    reason = in.readString();
  }

  @Override
  public int getId() {
    return PacketIds.DISCONNECT;
  }

  public String getReason() {
    return reason;
  }
}
