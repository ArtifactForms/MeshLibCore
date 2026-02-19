package engine.processing;

import java.util.HashMap;

import com.jogamp.newt.event.KeyEvent;

import engine.input.Key;

public class NewtKeyMapper {

  private static HashMap<Short, Key> keyMap;

  static {
    keyMap = new HashMap<Short, Key>();

    keyMap.put(KeyEvent.VK_A, Key.A);
    keyMap.put(KeyEvent.VK_B, Key.B);
    keyMap.put(KeyEvent.VK_C, Key.C);
    keyMap.put(KeyEvent.VK_D, Key.D);
    keyMap.put(KeyEvent.VK_E, Key.E);
    keyMap.put(KeyEvent.VK_F, Key.F);
    keyMap.put(KeyEvent.VK_G, Key.G);
    keyMap.put(KeyEvent.VK_H, Key.H);
    keyMap.put(KeyEvent.VK_I, Key.I);
    keyMap.put(KeyEvent.VK_J, Key.J);
    keyMap.put(KeyEvent.VK_K, Key.K);
    keyMap.put(KeyEvent.VK_L, Key.L);
    keyMap.put(KeyEvent.VK_M, Key.M);
    keyMap.put(KeyEvent.VK_N, Key.N);
    keyMap.put(KeyEvent.VK_O, Key.O);
    keyMap.put(KeyEvent.VK_P, Key.P);
    keyMap.put(KeyEvent.VK_Q, Key.Q);
    keyMap.put(KeyEvent.VK_R, Key.R);
    keyMap.put(KeyEvent.VK_S, Key.S);
    keyMap.put(KeyEvent.VK_T, Key.T);
    keyMap.put(KeyEvent.VK_U, Key.U);
    keyMap.put(KeyEvent.VK_V, Key.V);
    keyMap.put(KeyEvent.VK_W, Key.W);
    keyMap.put(KeyEvent.VK_X, Key.X);
    keyMap.put(KeyEvent.VK_Y, Key.Y);
    keyMap.put(KeyEvent.VK_Z, Key.Z);

    keyMap.put(KeyEvent.VK_F1, Key.F1);
    keyMap.put(KeyEvent.VK_F1, Key.F1);
    keyMap.put(KeyEvent.VK_F1, Key.F1);
    keyMap.put(KeyEvent.VK_F1, Key.F1);
    keyMap.put(KeyEvent.VK_F1, Key.F1);
    keyMap.put(KeyEvent.VK_F1, Key.F1);
    keyMap.put(KeyEvent.VK_F1, Key.F1);
    keyMap.put(KeyEvent.VK_F1, Key.F1);
    keyMap.put(KeyEvent.VK_F1, Key.F1);
    keyMap.put(KeyEvent.VK_F1, Key.F1);
    keyMap.put(KeyEvent.VK_F1, Key.F1);
    keyMap.put(KeyEvent.VK_F1, Key.F1);
  }

  public static Key mapKeyCode(int keyCode) {
    return keyMap.getOrDefault(keyCode, Key.UNKNOWN);
  }
}
