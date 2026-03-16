package server.usecases.blockbreak;

public interface BlockBreakView {

  void displayBlockUpdate(int x, int y, int z, short id);

  void displayErrorMessage(String message);

  void displaySuccessMessage(String message);

  void displayBlockBrokenEffect();
}
