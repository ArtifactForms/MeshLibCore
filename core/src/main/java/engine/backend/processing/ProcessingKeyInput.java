package engine.backend.processing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import engine.runtime.input.InputState;
import engine.runtime.input.Key;
import engine.runtime.input.KeyCharacterMapper;
import engine.runtime.input.KeyInput;
import engine.runtime.input.KeyListener;
import processing.core.PApplet;
import processing.event.KeyEvent;

public class ProcessingKeyInput implements KeyInput {

    private static final HashMap<Integer, Key> codedKeysMap = new HashMap<>();
    
    static {
        codedKeysMap.put(PApplet.SHIFT, Key.SHIFT);
        codedKeysMap.put(PApplet.ALT, Key.ALT);
        codedKeysMap.put(PApplet.CONTROL, Key.CTRL);
        codedKeysMap.put(PApplet.UP, Key.ARROW_UP);
        codedKeysMap.put(PApplet.DOWN, Key.ARROW_DOWN);
        codedKeysMap.put(PApplet.LEFT, Key.ARROW_LEFT);
        codedKeysMap.put(PApplet.RIGHT, Key.ARROW_RIGHT);
        
        codedKeysMap.put(97, Key.F1);
        codedKeysMap.put(98, Key.F2);
        codedKeysMap.put(99, Key.F3);
        codedKeysMap.put(100, Key.F4);
        codedKeysMap.put(101, Key.F5);
        codedKeysMap.put(102, Key.F6);
        codedKeysMap.put(103, Key.F7);
        codedKeysMap.put(104, Key.F8);
        codedKeysMap.put(105, Key.F9);
        codedKeysMap.put(106, Key.F10);
        codedKeysMap.put(107, Key.F11);
        codedKeysMap.put(108, Key.F12);
    }

    private final PApplet applet;
    private final InputState inputState = new InputState();
    private final List<KeyListener> listeners = new ArrayList<>();

    public ProcessingKeyInput(PApplet applet) {
        this.applet = applet;
        applet.registerMethod("keyEvent", this);
    }

    @Override
    public boolean isKeyPressed(Key key) {
        return inputState.isDown(key);
    }

    @Override
    public Collection<Key> getPressedKeys() {
        return new ArrayList<>(inputState.getDownKeys());
    }

    @Override
    public boolean wasKeyPressed(Key key) {
        return inputState.wasPressed(key);
    }

    @Override
    public boolean wasKeyReleased(Key key) {
        return inputState.wasReleased(key);
    }

    @Override
    public void updateKeyState() {
        if (!applet.focused) {
            inputState.clear();
        }
        inputState.nextFrame();
    }

    public void keyEvent(KeyEvent e) {
        Key key = Key.UNKNOWN;
        
        if (e.getKeyCode() == PApplet.ESC)
            key = Key.ESCAPE;
        else if (e.getKey() == ' ') key = Key.SPACE;
        else if (e.getKey() == '\n' || e.getKeyCode() == PApplet.ENTER) key = Key.ENTER;
        else if (e.getKey() == '\b' || e.getKeyCode() == PApplet.BACKSPACE) key = Key.BACKSPACE;
        else if (e.getKeyCode() != 0) key = codedKeysMap.getOrDefault(e.getKeyCode(), 
        		KeyCharacterMapper.getMappedKey(e.getKeyCode()));
        else key = KeyCharacterMapper.getMappedKey(e.getKey());

        if (key == Key.UNKNOWN && e.getAction() != KeyEvent.TYPE) return;

        switch (e.getAction()) {
            case KeyEvent.PRESS -> handlePress(key, e);
            case KeyEvent.RELEASE -> handleRelease(key, e);
            case KeyEvent.TYPE -> handleTyped(key, e);
        }
    }
    
    private void handlePress(Key key, KeyEvent e) {
        inputState.onKeyPressed(key);
        fireKeyPressed(new engine.runtime.input.KeyEvent(key, e.getKey()));
    }

    private void handleRelease(Key key, KeyEvent e) {
        inputState.onKeyReleased(key);
        fireKeyReleased(new engine.runtime.input.KeyEvent(key, e.getKey()));
    }

    private void handleTyped(Key key, KeyEvent e) {
        fireKeyTyped(new engine.runtime.input.KeyEvent(key, e.getKey()));
    }

    @Override
    public void addKeyListener(KeyListener listener) {
        if (listener != null) listeners.add(listener);
    }

    @Override
    public void removeKeyListener(KeyListener listener) {
        listeners.remove(listener);
    }

    protected void fireKeyPressed(engine.runtime.input.KeyEvent e) {
        for (KeyListener l : listeners) l.onKeyPressed(e);
    }

    protected void fireKeyReleased(engine.runtime.input.KeyEvent e) {
        for (KeyListener l : listeners) l.onKeyReleased(e);
    }

    protected void fireKeyTyped(engine.runtime.input.KeyEvent e) {
        for (KeyListener l : listeners) l.onKeyTyped(e);
    }
}