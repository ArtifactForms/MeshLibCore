package demos.jam26.ui;

import demos.jam26.assets.AssetRefs;
import engine.components.AbstractComponent;
import engine.components.RenderableComponent;
import engine.render.Graphics;
import engine.resources.Font;
import engine.resources.Image;
import engine.resources.ResourceManager;
import engine.runtime.input.Input;
import engine.runtime.input.Key;
import engine.runtime.input.MouseInput;
import math.Color;

public class StartScreenComponent extends AbstractComponent implements RenderableComponent {

  private Input input;
  private Image image;

  private float blinkTime = 0f;
  private float blinkSpeed = 2.5f; // higher = faster blinking

  public StartScreenComponent(Input input) {
    this.input = input;
    image = ResourceManager.getInstance().loadImage(AssetRefs.IMAGE_MENU_PATH);
  }

  @Override
  public void onUpdate(float tpf) {
    blinkTime += tpf;

    if (input.isMousePressed(MouseInput.LEFT) || input.wasKeyPressed(Key.SPACE)) {
      setActive(false);
    }
  }

  private float getBlinkAlpha() {
    return 0.5f + 0.5f * (float) Math.sin(blinkTime * blinkSpeed);
  }

  @Override
  public void render(Graphics g) {
    // IMPORTANT:
    // This start screen is a fullscreen modal UI.
    // We must clear the framebuffer here because other UI elements
    // (reticle, minimap border, health bar outline, etc.) use stroke/draw
    // calls (drawRect, drawOval) instead of fills.
    //
    // Without clearing, those previously rendered stroke-only elements
    // would remain visible "through" this screen.
    g.clear(Color.BLACK);

    g.drawImage(image, 0, 0, g.getWidth(), g.getHeight());

    // title
    g.setColor(new Color(1f, 1f, 1f, getBlinkAlpha()));
    g.setFont(new Font("monogram-extended", 90, Font.PLAIN));
    g.textCentered("CLICK TO START", g.getHeight() * 0.8f);
  }
}
