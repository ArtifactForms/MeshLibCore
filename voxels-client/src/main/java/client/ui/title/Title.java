package client.ui.title;

public class Title {

  private final String title;

  private final String subtitle;

  private final float fadeInTime;

  private final float stayTime;

  private final float fadeOutTime;

  public Title(String title, String subtitle, float fadeInTime, float stayTime, float fadeOutTime) {
    this.title = title;
    this.subtitle = subtitle;
    this.fadeInTime = fadeInTime;
    this.stayTime = stayTime;
    this.fadeOutTime = fadeOutTime;
  }

  public float getFadeInTime() {
    return fadeInTime;
  }

  public float getFadeOutTime() {
    return fadeOutTime;
  }

  public float getStayTime() {
    return stayTime;
  }

  public String getText() {
    return title;
  }

  public String getSubtitle() {
    return subtitle;
  }
}
