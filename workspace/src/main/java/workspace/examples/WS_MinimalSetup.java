package workspace.examples;

import processing.core.PApplet;
import workspace.Workspace;

public class WS_MinimalSetup extends PApplet {

  public static void main(String[] args) {
    PApplet.main(WS_MinimalSetup.class.getName());
  }

  @Override
  public void settings() {
    size(1000, 1000, P3D);
  }

  @Override
  public void setup() {
    new Workspace(this);
  }

  @Override
  public void draw() {
    // Drawing goes here
  }
}
