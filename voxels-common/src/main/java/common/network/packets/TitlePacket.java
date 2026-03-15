package common.network.packets;

import java.io.IOException;

import common.network.Packet;
import common.network.PacketBuffer;
import common.network.PacketIds;

public class TitlePacket implements Packet {

  private String title;
  private String subtitle;

  private int fadeInTicks;
  private int stayTicks;
  private int fadeOutTicks;

  public TitlePacket() {}

  public TitlePacket(
      String title, String subtitle, int fadeInTicks, int stayTicks, int fadeOutTicks) {
    this.title = title;
    this.subtitle = subtitle;
    this.fadeInTicks = fadeInTicks;
    this.stayTicks = stayTicks;
    this.fadeOutTicks = fadeOutTicks;
  }

  @Override
  public void write(PacketBuffer out) throws IOException {
    out.writeString(title);
    out.writeString(subtitle);
    out.writeInt(fadeInTicks);
    out.writeInt(stayTicks);
    out.writeInt(fadeOutTicks);
  }

  @Override
  public void read(PacketBuffer in) throws IOException {
    this.title = in.readString();
    this.subtitle = in.readString();
    this.fadeInTicks = in.readInt();
    this.stayTicks = in.readInt();
    this.fadeOutTicks = in.readInt();
  }

  @Override
  public int getId() {
    return PacketIds.TITLE;
  }

  public String getTitle() {
    return title;
  }

  public String getSubtitle() {
    return subtitle;
  }

  public int getFadeInTicks() {
    return fadeInTicks;
  }

  public int getStayTicks() {
    return stayTicks;
  }

  public int getFadeOutTicks() {
    return fadeOutTicks;
  }
}
