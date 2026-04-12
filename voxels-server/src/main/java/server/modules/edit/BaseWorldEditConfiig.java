package server.modules.edit;

public class BaseWorldEditConfiig implements WorldEditConfig {

  @Override
  public int getMaxCircleRadius() {
    return 1000;
  }

  @Override
  public int getMaxDiscRadius() {
    return 100;
  }

  @Override
  public int getMaxSphereRadius() {
    return 100;
  }
}
