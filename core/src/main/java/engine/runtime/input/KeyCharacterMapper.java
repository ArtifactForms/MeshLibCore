package engine.runtime.input;

import java.util.HashMap;

public class KeyCharacterMapper {

  private static HashMap<Character, Key> keyMap;

  static {
    keyMap = new HashMap<Character, Key>();

    keyMap.put('a', Key.A);
    keyMap.put('b', Key.B);
    keyMap.put('c', Key.C);
    keyMap.put('d', Key.D);
    keyMap.put('e', Key.E);
    keyMap.put('f', Key.F);
    keyMap.put('g', Key.G);
    keyMap.put('h', Key.H);
    keyMap.put('i', Key.I);
    keyMap.put('j', Key.J);
    keyMap.put('k', Key.K);
    keyMap.put('l', Key.L);
    keyMap.put('m', Key.M);
    keyMap.put('n', Key.N);
    keyMap.put('o', Key.O);
    keyMap.put('p', Key.P);
    keyMap.put('q', Key.Q);
    keyMap.put('r', Key.R);
    keyMap.put('s', Key.S);
    keyMap.put('t', Key.T);
    keyMap.put('u', Key.U);
    keyMap.put('v', Key.V);
    keyMap.put('w', Key.W);
    keyMap.put('x', Key.X);
    keyMap.put('y', Key.Y);
    keyMap.put('z', Key.Z);

    keyMap.put('A', Key.A);
    keyMap.put('B', Key.B);
    keyMap.put('C', Key.C);
    keyMap.put('D', Key.D);
    keyMap.put('E', Key.E);
    keyMap.put('F', Key.F);
    keyMap.put('G', Key.G);
    keyMap.put('H', Key.H);
    keyMap.put('I', Key.I);
    keyMap.put('J', Key.J);
    keyMap.put('K', Key.K);
    keyMap.put('L', Key.L);
    keyMap.put('M', Key.M);
    keyMap.put('N', Key.N);
    keyMap.put('O', Key.O);
    keyMap.put('P', Key.P);
    keyMap.put('Q', Key.Q);
    keyMap.put('R', Key.R);
    keyMap.put('S', Key.S);
    keyMap.put('T', Key.T);
    keyMap.put('U', Key.U);
    keyMap.put('V', Key.V);
    keyMap.put('W', Key.W);
    keyMap.put('X', Key.X);
    keyMap.put('Y', Key.Y);
    keyMap.put('Z', Key.Z);

    keyMap.put('0', Key.NUM_0);
    keyMap.put('1', Key.NUM_1);
    keyMap.put('2', Key.NUM_2);
    keyMap.put('3', Key.NUM_3);
    keyMap.put('4', Key.NUM_4);
    keyMap.put('5', Key.NUM_5);
    keyMap.put('6', Key.NUM_6);
    keyMap.put('7', Key.NUM_7);
    keyMap.put('8', Key.NUM_8);
    keyMap.put('9', Key.NUM_9);

    keyMap.put(' ', Key.SPACE);
    keyMap.put((char) 8, Key.BACKSPACE);
    keyMap.put((char) 9, Key.TAB);
    keyMap.put((char) 10, Key.ENTER);
    keyMap.put((char) 13, Key.RETURN);
    keyMap.put((char) 27, Key.ESCAPE);
    keyMap.put((char) 47, Key.BACK_SLASH);
    keyMap.put((char) 127, Key.DELETE);
  }

  public static Key getMappedKey(int keyCode) {
    // ASCII A–Z
    if (keyCode >= 65 && keyCode <= 90) {
      return Key.values()[Key.A.ordinal() + (keyCode - 65)];
    }

    // ASCII 0–9
    if (keyCode >= 48 && keyCode <= 57) {
      return Key.values()[Key.NUM_0.ordinal() + (keyCode - 48)];
    }

    return Key.UNKNOWN;
  }
}
