package demos.texture;

import math.Color;

public class Title {
  private final float fadeInTime;
  private final float fadeOutTime;
  private final float stayTime;
  private final int size;
  private final Color color;
  private final String text;

  private Title(Builder builder) {
    this.fadeInTime = builder.fadeInTime;
    this.fadeOutTime = builder.fadeOutTime;
    this.stayTime = builder.stayTime;
    this.size = builder.size;
    this.color = builder.color;
    this.text = builder.text;
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

  public int getSize() {
    return size;
  }

  public Color getColor() {
    return color;
  }

  public String getText() {
    return text;
  }

  public static class Builder {
    private float fadeInTime = 1.0f;
    private float fadeOutTime = 1.0f;
    private float stayTime = 2.0f;
    private int size = 100;
    private Color color = Color.WHITE;
    private String text = "Default Text";

    public Builder fadeInTime(float fadeInTime) {
      this.fadeInTime = fadeInTime;
      return this;
    }

    public Builder fadeOutTime(float fadeOutTime) {
      this.fadeOutTime = fadeOutTime;
      return this;
    }

    public Builder stayTime(float stayTime) {
      this.stayTime = stayTime;
      return this;
    }

    public Builder size(int size) {
      this.size = size;
      return this;
    }

    public Builder color(Color color) {
      this.color = color;
      return this;
    }

    public Builder text(String text) {
      this.text = text;
      return this;
    }

    public Title build() {
      return new Title(this);
    }
  }
}
