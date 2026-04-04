package client.rendering;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.modifier.transform.ScaleModifier;
import mesh.modifier.transform.TranslateModifier;

public class CharacterMeshCreator implements IMeshCreator {

  private final float headSize = 0.6f;

  private final float torsoW = 0.4f, torsoH = 0.5f, torsoD = 0.25f;
  
  private final float armW = 0.12f, armH = 0.4f;
  
  private final float legW = 0.15f, legH = 0.4f;

  private final float brimSize = 0.7f;

  private final float brimHeight = 0.05f;

  private final float hatBodySize = 0.45f;

  private final float hatBodyHeight = 0.4f;

  @Override
  public Mesh3D create() {
    CubeCreator creator = new CubeCreator(0.5f);
    float currentY = 0;

    // 1. LEGS
    Mesh3D legL = creator.create();
    new ScaleModifier(legW, legH, legW).modify(legL);
    new TranslateModifier(-0.12f, legH / 2, 0).modify(legL);

    Mesh3D legR = creator.create();
    new ScaleModifier(legW, legH, legW).modify(legR);
    new TranslateModifier(0.12f, legH / 2, 0).modify(legR);

    currentY = legH;

    // 2. TORSO
    Mesh3D torso = creator.create();
    new ScaleModifier(torsoW, torsoH, torsoD).modify(torso);
    new TranslateModifier(0, currentY + (torsoH / 2), 0).modify(torso);

    float armY = currentY + (torsoH / 2);
    float armOffset = (torsoW / 2) + (armW / 2);

    Mesh3D armL = creator.create();
    new ScaleModifier(armW, armH, armW).modify(armL);
    new TranslateModifier(-armOffset, armY, 0).modify(armL);

    Mesh3D armR = creator.create();
    new ScaleModifier(armW, armH, armW).modify(armR);
    new TranslateModifier(armOffset, armY, 0).modify(armR);

    currentY += torsoH;

    // 4. HEAD
    Mesh3D head = creator.create();
    new ScaleModifier(headSize, 0.5f, headSize).modify(head);
    new TranslateModifier(0, currentY + 0.25f, 0).modify(head);

    currentY += 0.5f;

    // 5. HAT
    Mesh3D hatBrim = creator.create();
    new ScaleModifier(brimSize, brimHeight, brimSize).modify(hatBrim);
    new TranslateModifier(0, currentY + (brimHeight / 2), 0).modify(hatBrim);

    currentY += brimHeight;

    Mesh3D hatBody = creator.create();
    new ScaleModifier(hatBodySize, hatBodyHeight, hatBodySize).modify(hatBody);
    new TranslateModifier(0, currentY + (hatBodyHeight / 2), 0).modify(hatBody);

    Mesh3D character = new Mesh3D();
    character.append(legL);
    character.append(legR);
    character.append(torso);
    character.append(armL);
    character.append(armR);
    character.append(head);
    character.append(hatBrim);
    character.append(hatBody);

    new ScaleModifier(1, -1, 1).modify(character);

    return character;
  }
}
