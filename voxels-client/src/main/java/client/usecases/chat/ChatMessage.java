package client.usecases.chat;

public class ChatMessage {

  private final String text;

  private final long timestamp;

  public ChatMessage(String text) {
    this.text = text;
    this.timestamp = System.currentTimeMillis();
  }

  public ChatMessage(String text, long timeStamp) {
    this.text = text;
    this.timestamp = timeStamp;
  }

  public String getText() {
    return text;
  }

  public long getTimestamp() {
    return timestamp;
  }
}
