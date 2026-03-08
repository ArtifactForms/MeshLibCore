package common.network.packets;

import java.io.IOException;

import common.network.Packet;
import common.network.PacketBuffer;
import common.network.PacketIds;

public class SoundEffectPacket implements Packet {

  private String soundEffectId;

  public SoundEffectPacket() {}

  public SoundEffectPacket(String id) {
    this.soundEffectId = id;
  }

  @Override
  public void write(PacketBuffer out) throws IOException {
    out.writeString(soundEffectId);
  }

  @Override
  public void read(PacketBuffer in) throws IOException {
    this.soundEffectId = in.readString();
  }

  @Override
  public int getId() {
    return PacketIds.SOUND_EFFECT;
  }

  public String getSoundEffectId() {
    return soundEffectId;
  }
}
