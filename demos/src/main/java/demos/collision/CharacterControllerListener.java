package demos.collision;

public interface CharacterControllerListener {

  void onStateChanged(CharacterControllerComponent.MovementState newState);

  void onJump();

  void onLand();
}
