package demos.voxels.client.event;

public class MessageSentEvent implements Event {

  private final String message;

  public MessageSentEvent(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public String getEventType() {
    return "MESSAGE_SENT";
  }
}
