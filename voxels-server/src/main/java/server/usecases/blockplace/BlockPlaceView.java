package server.usecases.blockplace;

public interface BlockPlaceView {

  void displayBlockUpdate(int x, int y, int z, short id);
  
  void broadcastBlockUpdate(int x, int y, int z, short id);

  void displayErrorMessage(String message);

  void displaySuccessMessage(String message);

  void displayBlockPlacedEffect();
}
