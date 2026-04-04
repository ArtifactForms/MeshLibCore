package common.network.packets;

import java.io.IOException;

import common.network.Packet;
import common.network.PacketBuffer;
import common.network.PacketIds;

public class ActionBarPacket implements Packet {

  private String text;

  private int durationInTicks;

  public ActionBarPacket() {}

  public ActionBarPacket(String text, int durationInSeconds) {
    this.text = text;
    this.durationInTicks = durationInSeconds;
  }

  @Override
  public void write(PacketBuffer out) throws IOException {
    out.writeString(text);
    out.writeInt(durationInTicks);
  }

  @Override
  public void read(PacketBuffer in) throws IOException {
    this.text = in.readString();
    this.durationInTicks = in.readInt();
  }

  @Override
  public int getId() {
    return PacketIds.ACTION_BAR;
  }

  public String getText() {
    return text;
  }

  public int getDurationInTicks() {
    return durationInTicks;
  }
}
