package common.network.packets;

import java.io.IOException;

import common.network.Packet;
import common.network.PacketBuffer;
import common.network.PacketIds;
import common.network.ServerToClientPacket;

/**
 * A network packet used to display a title and subtitle on the client screen.
 *
 * <p>This packet is typically sent from the server to the client to trigger a visual title overlay
 * (e.g., for events, notifications, or transitions).
 *
 * <p>The packet contains:
 *
 * <ul>
 *   <li>Main title text
 *   <li>Optional subtitle text
 *   <li>Fade-in duration (in ticks)
 *   <li>Display duration (in ticks)
 *   <li>Fade-out duration (in ticks)
 * </ul>
 *
 * <p>String fields may be {@code null}, in which case they will be handled accordingly by the
 * {@link PacketBuffer}.
 *
 * <p>All time values are measured in ticks and interpreted by the client.
 *
 * @see Packet
 * @see PacketBuffer
 * @see PacketIds#TITLE
 */
public class TitlePacket implements Packet, ServerToClientPacket {

  private String title;
  private String subtitle;

  private int fadeInTicks;
  private int stayTicks;
  private int fadeOutTicks;

  /** Default constructor used for deserialization. */
  public TitlePacket() {}

  /**
   * Creates a new {@code TitlePacket}.
   *
   * @param title the main title text (may be {@code null})
   * @param subtitle the subtitle text (may be {@code null})
   * @param fadeInTicks duration of the fade-in effect in ticks
   * @param stayTicks duration the title remains fully visible in ticks
   * @param fadeOutTicks duration of the fade-out effect in ticks
   */
  public TitlePacket(
      String title, String subtitle, int fadeInTicks, int stayTicks, int fadeOutTicks) {
    this.title = title;
    this.subtitle = subtitle;
    this.fadeInTicks = fadeInTicks;
    this.stayTicks = stayTicks;
    this.fadeOutTicks = fadeOutTicks;
  }

  /**
   * Writes this packet's data to the given {@link PacketBuffer}.
   *
   * @param out the buffer to write to
   * @throws IOException if an I/O error occurs during writing
   */
  @Override
  public void write(PacketBuffer out) throws IOException {
    out.writeString(title);
    out.writeString(subtitle);
    out.writeInt(fadeInTicks);
    out.writeInt(stayTicks);
    out.writeInt(fadeOutTicks);
  }

  /**
   * Reads this packet's data from the given {@link PacketBuffer}.
   *
   * @param in the buffer to read from
   * @throws IOException if an I/O error occurs during reading
   */
  @Override
  public void read(PacketBuffer in) throws IOException {
    this.title = in.readString();
    this.subtitle = in.readString();
    this.fadeInTicks = in.readInt();
    this.stayTicks = in.readInt();
    this.fadeOutTicks = in.readInt();
  }

  /**
   * Returns the unique packet ID used for network identification.
   *
   * @return the packet ID corresponding to {@link PacketIds#TITLE}
   */
  @Override
  public int getId() {
    return PacketIds.TITLE;
  }

  /** @return the main title text, or {@code null} if not set */
  public String getTitle() {
    return title;
  }

  /** @return the subtitle text, or {@code null} if not set */
  public String getSubtitle() {
    return subtitle;
  }

  /** @return fade-in duration in ticks */
  public int getFadeInTicks() {
    return fadeInTicks;
  }

  /** @return display duration in ticks */
  public int getStayTicks() {
    return stayTicks;
  }

  /** @return fade-out duration in ticks */
  public int getFadeOutTicks() {
    return fadeOutTicks;
  }
}
