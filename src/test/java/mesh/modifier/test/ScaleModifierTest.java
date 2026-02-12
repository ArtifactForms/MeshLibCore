package mesh.modifier.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.platonic.IcosahedronCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.creator.primitives.QuadSphereCreator;
import mesh.modifier.IMeshModifier;
import mesh.modifier.transform.ScaleModifier;

public class ScaleModifierTest {

  @Test
  public void testImplementsModifierInterface() {
    ScaleModifier modifier = new ScaleModifier();
    assertTrue(modifier instanceof IMeshModifier);
  }

  @Test
  public void returnedReferenceIsNotNull() {
    assertNotNull(new ScaleModifier().modify(new Mesh3D()));
  }

  @Test
  public void testReturnsReference() {
    Mesh3D mesh0 = new CubeCreator().create();
    Mesh3D mesh1 = new ScaleModifier().modify(mesh0);
    assertTrue(mesh0 == mesh1);
  }

  @Test
  public void testDefaultScale() {
    ScaleModifier modifier = new ScaleModifier();
    assertEquals(1, modifier.getScaleX());
    assertEquals(1, modifier.getScaleY());
    assertEquals(1, modifier.getScaleZ());
  }

  @Test
  public void testScaleConstructor() {
    float expectedScale = 12.45f;
    ScaleModifier modifier = new ScaleModifier(expectedScale);
    assertEquals(expectedScale, modifier.getScaleX());
    assertEquals(expectedScale, modifier.getScaleY());
    assertEquals(expectedScale, modifier.getScaleZ());
  }

  @Test
  public void testGetSetScaleX() {
    float expectedScale = 123.4f;
    ScaleModifier modifier = new ScaleModifier();
    modifier.setScaleX(expectedScale);
    assertEquals(expectedScale, modifier.getScaleX());
  }

  @Test
  public void testGetSetScaleY() {
    float expectedScale = 24.5f;
    ScaleModifier modifier = new ScaleModifier();
    modifier.setScaleY(expectedScale);
    assertEquals(expectedScale, modifier.getScaleY());
  }

  @Test
  public void testGetSetScaleZ() {
    float expectedScale = 24.1223f;
    ScaleModifier modifier = new ScaleModifier();
    modifier.setScaleZ(expectedScale);
    assertEquals(expectedScale, modifier.getScaleZ());
  }

  @Test
  public void testScalesOriginalVertices() {
    Mesh3D mesh = new IcosahedronCreator().create();
    List<Vector3f> vertices = new ArrayList<Vector3f>(mesh.vertices);
    mesh.apply(new ScaleModifier(2.3f));
    for (int i = 0; i < vertices.size(); i++) {
      Vector3f expected = vertices.get(i);
      Vector3f actual = mesh.vertices.get(i);
      assertTrue(expected == actual);
    }
  }

  @Test
  public void testScaleQuadSphereScalar() {
    float scale = 134.3f;
    Mesh3D mesh = new QuadSphereCreator().create();
    List<Vector3f> unscaledVertices = new ArrayList<Vector3f>();
    for (Vector3f v : mesh.vertices) {
      unscaledVertices.add(new Vector3f(v));
    }
    ScaleModifier modifier = new ScaleModifier(scale);
    modifier.modify(mesh);
    for (int i = 0; i < unscaledVertices.size(); i++) {
      Vector3f unscaled = unscaledVertices.get(i);
      Vector3f expected = unscaled.mult(scale);
      Vector3f actual = mesh.vertices.get(i);
      assertEquals(expected, actual);
    }
  }

  @Test
  public void testScaleQuadSphereXYZ() {
    float scaleX = 134.3f;
    float scaleY = 12.3f;
    float scaleZ = 1234.3f;
    Mesh3D mesh = new QuadSphereCreator().create();
    List<Vector3f> unscaledVertices = new ArrayList<Vector3f>();
    for (Vector3f v : mesh.vertices) {
      unscaledVertices.add(new Vector3f(v));
    }
    ScaleModifier modifier = new ScaleModifier(scaleX, scaleY, scaleZ);
    modifier.modify(mesh);
    for (int i = 0; i < unscaledVertices.size(); i++) {
      Vector3f unscaled = unscaledVertices.get(i);
      Vector3f expected = unscaled.mult(scaleX, scaleY, scaleZ);
      Vector3f actual = mesh.vertices.get(i);
      assertEquals(expected, actual);
    }
  }

  @Test
  public void testScaleQuadSphereSetX() {
    float scaleX = 234.3f;
    Mesh3D mesh = new QuadSphereCreator().create();
    List<Vector3f> unscaledVertices = new ArrayList<Vector3f>();
    for (Vector3f v : mesh.vertices) {
      unscaledVertices.add(new Vector3f(v));
    }
    ScaleModifier modifier = new ScaleModifier();
    modifier.setScaleX(scaleX);
    modifier.modify(mesh);
    for (int i = 0; i < unscaledVertices.size(); i++) {
      Vector3f unscaled = unscaledVertices.get(i);
      Vector3f expected = unscaled.mult(scaleX, 1, 1);
      Vector3f actual = mesh.vertices.get(i);
      assertEquals(expected, actual);
    }
  }

  @Test
  public void testScaleQuadSphereSetY() {
    float scaleY = 234.323f;
    Mesh3D mesh = new QuadSphereCreator().create();
    List<Vector3f> unscaledVertices = new ArrayList<Vector3f>();
    for (Vector3f v : mesh.vertices) {
      unscaledVertices.add(new Vector3f(v));
    }
    ScaleModifier modifier = new ScaleModifier();
    modifier.setScaleY(scaleY);
    modifier.modify(mesh);
    for (int i = 0; i < unscaledVertices.size(); i++) {
      Vector3f unscaled = unscaledVertices.get(i);
      Vector3f expected = unscaled.mult(1, scaleY, 1);
      Vector3f actual = mesh.vertices.get(i);
      assertEquals(expected, actual);
    }
  }

  @Test
  public void testScaleQuadSphereSetZ() {
    float scaleZ = 21.34f;
    Mesh3D mesh = new QuadSphereCreator().create();
    List<Vector3f> unscaledVertices = new ArrayList<Vector3f>();
    for (Vector3f v : mesh.vertices) {
      unscaledVertices.add(new Vector3f(v));
    }
    ScaleModifier modifier = new ScaleModifier();
    modifier.setScaleZ(scaleZ);
    modifier.modify(mesh);
    for (int i = 0; i < unscaledVertices.size(); i++) {
      Vector3f unscaled = unscaledVertices.get(i);
      Vector3f expected = unscaled.mult(1, 1, scaleZ);
      Vector3f actual = mesh.vertices.get(i);
      assertEquals(expected, actual);
    }
  }
}
