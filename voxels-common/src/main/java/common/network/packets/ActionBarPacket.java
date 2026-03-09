package common.network.packets;

import java.io.IOException;

import common.network.Packet;
import common.network.PacketBuffer;
import common.network.PacketIds;

public class ActionBarPacket implements Packet {

  private String text;
  private float durationInSeconds;

  public ActionBarPacket() {}

  public ActionBarPacket(String text, float durationInSeconds) {
    this.text = text;
    this.durationInSeconds = durationInSeconds;
  }

  @Override
  public void write(PacketBuffer out) throws IOException {
    out.writeString(text);
    out.writeFloat(durationInSeconds);
  }

  @Override
  public void read(PacketBuffer in) throws IOException {
    this.text = in.readString();
    this.durationInSeconds = in.readFloat();
  }

  @Override
  public int getId() {
    return PacketIds.ACTION_BAR;
  }

  public String getText() {
    return text;
  }

  public float getDurationInSeconds() {
    return durationInSeconds;
  }
}
