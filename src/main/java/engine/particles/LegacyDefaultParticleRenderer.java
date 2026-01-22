package engine.particles;

import java.util.Collection;

import math.Color;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;
import workspace.ui.Graphics;

public class LegacyDefaultParticleRenderer implements LegacyParticleRenderer {

  private Mesh3D mesh;

  public LegacyDefaultParticleRenderer() {
    mesh = new CubeCreator(0.1f).create();
  }

  @Override
  public void render(Graphics g, Collection<LegacyParticle> particles) {
    g.setColor(Color.WHITE);
    for (LegacyParticle particle : particles) {
      Vector3f position = particle.getPosition();
      g.pushMatrix();
      g.translate(position.x, position.y, position.z);
//      			g.fillOval(0, 0, 0.1f, 0.1f);
      g.fillFaces(mesh);
      g.popMatrix();
    }
  }

  @Override
  public void initialize() {
    // TODO Auto-generated method stub

  }

  @Override
  public void cleanup() {
    // TODO Auto-generated method stub

  }
}
