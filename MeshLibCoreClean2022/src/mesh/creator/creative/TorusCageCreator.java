package mesh.creator.creative;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.TorusCreator;
import mesh.modifier.ExtrudeModifier;
import mesh.modifier.SolidifyModifier;
import mesh.modifier.subdivision.CatmullClarkModifier;

public class TorusCageCreator implements IMeshCreator {

    private int subdivisions;
    private int majorSegments;
    private int minorSegments;
    private float thickness;
    private float majorRadius;
    private float minorRadius;
    private float extrude;
    private Mesh3D mesh;

    public TorusCageCreator() {
	subdivisions = 1;
	majorSegments = 24;
	minorSegments = 12;
	thickness = 0.05f;
	majorRadius = 1.0f;
	minorRadius = 0.5f;
	extrude = 0.8f;
    }

    @Override
    public Mesh3D create() {
	createTorus();
	createHoles();
	solidify();
	subdivide();
	return mesh;
    }

    private void createTorus() {
	TorusCreator creator = new TorusCreator();
	creator.setMajorRadius(majorRadius);
	creator.setMinorRadius(minorRadius);
	creator.setMajorSegments(majorSegments);
	creator.setMinorSegments(minorSegments);
	mesh = creator.create();
    }

    private void createHoles() {
	ExtrudeModifier modifier = new ExtrudeModifier();
	modifier.setScale(extrude);
	modifier.setAmount(0);
	modifier.setRemoveFaces(true);
	modifier.modify(mesh, mesh.getFaces());
    }

    private void solidify() {
	new SolidifyModifier(thickness).modify(mesh);
    }

    private void subdivide() {
	new CatmullClarkModifier(subdivisions).modify(mesh);
    }

    public int getSubdivisions() {
	return subdivisions;
    }

    public void setSubdivisions(int subdivisions) {
	this.subdivisions = subdivisions;
    }

    public int getMajorSegments() {
	return majorSegments;
    }

    public void setMajorSegments(int majorSegments) {
	this.majorSegments = majorSegments;
    }

    public int getMinorSegments() {
	return minorSegments;
    }

    public void setMinorSegments(int minorSegments) {
	this.minorSegments = minorSegments;
    }

    public float getThickness() {
	return thickness;
    }

    public void setThickness(float thickness) {
	this.thickness = thickness;
    }

    public float getMajorRadius() {
	return majorRadius;
    }

    public void setMajorRadius(float majorRadius) {
	this.majorRadius = majorRadius;
    }

    public float getMinorRadius() {
	return minorRadius;
    }

    public void setMinorRadius(float minorRadius) {
	this.minorRadius = minorRadius;
    }

    public float getExtrude() {
	return extrude;
    }

    public void setExtrude(float extrude) {
	this.extrude = extrude;
    }

}
