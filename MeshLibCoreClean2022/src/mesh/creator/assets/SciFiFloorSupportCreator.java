package mesh.creator.assets;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.selection.CompareType;
import mesh.selection.FaceSelection;
import mesh.selection.FaceSelectionRules;

public class SciFiFloorSupportCreator implements IMeshCreator {

	private int supportCount;
	private int segments;
	private float gap;
	private float width;
	private float radius;
	private float extendBottom;
	private float extendTop;
	private float extendFront;
	private float extendBack;
	private float mirrorGap;
	private boolean capBack;
	private boolean capBottom;
	private boolean capTop;
	private boolean mirror;
	private Mesh3D mesh;
	private Mesh3D support;

	public SciFiFloorSupportCreator() {
		supportCount = 1;
		segments = 5;
		gap = 0;
		width = 0.7f;
		radius = 2;
		extendBottom = 0.5f;
		extendTop = 0.2f;
		extendFront = 0.1f;
		extendBack = 0.1f;
		mirrorGap = 0;
		capBack = true;
		capBottom = true;
		capTop = true;
		mirror = false;
	}

	@Override
	public Mesh3D create() {
		initializeMesh();
		createSupport();
		createSupports();
		centerOnAxisX();
		mirror();
		return mesh;
	}

	private void mirror() {
		if (!mirror)
			return;
		mesh.translateZ(-getSupportDepth() / 2f - getMirrorGap() / 2f);
		Mesh3D mirrorMesh = mesh.copy();
		mirrorMesh.rotateY(Mathf.PI);
		mesh.append(mirrorMesh);
	}

	private void centerOnAxisX() {
		mesh.translateX(-(getTotalWidth() / 2f) + (getWidth() / 2f));
	}

	private void processCapBack() {
		if (capBack)
			return;
		FaceSelection selection = new FaceSelection(support);
		FaceSelectionRules rules = new FaceSelectionRules();
		rules.centerZ(CompareType.LESS_OR_EQUALS, -getSupportDepth() / 2f);
		rules.apply(selection);
		support.faces.removeAll(selection.getFaces());
	}

	private void processCapBottom() {
		if (capBottom)
			return;
		FaceSelection selection = new FaceSelection(support);
		FaceSelectionRules rules = new FaceSelectionRules();
		rules.centerY(CompareType.EQUALS, 0);
		rules.apply(selection);
		support.faces.removeAll(selection.getFaces());
	}

	private void processCapTop() {
		if (capTop)
			return;
		FaceSelection selection = new FaceSelection(support);
		FaceSelectionRules rules = new FaceSelectionRules();
		rules.centerY(CompareType.LESS_OR_EQUALS, -getSupportHeight());
		rules.apply(selection);
		support.faces.removeAll(selection.getFaces());
	}

	private void createSupports() {
		for (int i = 0; i < supportCount; i++) {
			Mesh3D support = this.support.copy();
			support.translateX(i * (width + gap));
			mesh.append(support);
		}
	}

	private void createSupport() {
		ArchCreator creator = new ArchCreator();
		creator.setRadius(radius);
		creator.setExtendLeft(extendBottom);
		creator.setExtendRight(extendTop);
		creator.setExtendBottom(extendFront);
		creator.setExtendTop(extendBack);
		creator.setDepth(width);
		creator.setSegments(segments);
		support = creator.create();
		support.rotateZ(-Mathf.HALF_PI);
		support.translateY(-radius - extendBottom);
		support.translateX(getSupportDepth() / 2f);
		support.rotateY(-Mathf.HALF_PI);
		processCapBack();
		processCapTop();
		processCapBottom();
	}

	private void initializeMesh() {
		mesh = new Mesh3D();
	}

	private float getTotalWidth() {
		return (getSupportCount() * getWidth()) + ((getSupportCount() - 1) * getGap());
	}

	private float getSupportDepth() {
		return radius + extendFront + extendBack;
	}

	private float getSupportHeight() {
		return radius + radius + extendBottom + extendTop;
	}

	public float getGap() {
		return gap;
	}

	public void setGap(float gap) {
		this.gap = gap;
	}

	public int getSupportCount() {
		return supportCount;
	}

	public void setSupportCount(int supportCount) {
		this.supportCount = supportCount;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public int getSegments() {
		return segments;
	}

	public void setSegments(int segments) {
		this.segments = segments;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public float getExtendBottom() {
		return extendBottom;
	}

	public void setExtendBottom(float extendBottom) {
		this.extendBottom = extendBottom;
	}

	public float getExtendTop() {
		return extendTop;
	}

	public void setExtendTop(float extendTop) {
		this.extendTop = extendTop;
	}

	public float getExtendFront() {
		return extendFront;
	}

	public void setExtendFront(float extendFront) {
		this.extendFront = extendFront;
	}

	public float getExtendBack() {
		return extendBack;
	}

	public void setExtendBack(float extendBack) {
		this.extendBack = extendBack;
	}

	public boolean isCapBack() {
		return capBack;
	}

	public void setCapBack(boolean capBack) {
		this.capBack = capBack;
	}

	public boolean isCapBottom() {
		return capBottom;
	}

	public void setCapBottom(boolean capBottom) {
		this.capBottom = capBottom;
	}

	public boolean isCapTop() {
		return capTop;
	}

	public void setCapTop(boolean capTop) {
		this.capTop = capTop;
	}

	public boolean isMirror() {
		return mirror;
	}

	public void setMirror(boolean mirror) {
		this.mirror = mirror;
	}

	public float getMirrorGap() {
		return mirrorGap;
	}

	public void setMirrorGap(float mirrorGap) {
		this.mirrorGap = mirrorGap;
	}

}
