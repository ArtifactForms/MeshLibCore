package demos.jam26port.player;

import engine.components.Component;
import engine.components.Transform;
import math.Vector3f;

public interface MovementFilter extends Component {
  Vector3f filter(Transform transfor, Vector3f delta, float tpf);
}
