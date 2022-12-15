package mesh.creator.assets;

import math.Mathf;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.FillType;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CircleCreator;
import mesh.creator.primitives.CylinderCreator;
import mesh.creator.special.VariableCylinderCreator;

public class FlangePipeCreator implements IMeshCreator {

    private int segmentCount;

    private int boltCount;

    private int rotationSegments;

    private float flangeOuterRadius;

    private float flangeGrooveWidth;

    private float pipeRadius;

    private float flangeDepth;

    private float pipeSegmentLength;

    private float boltHeadPercantage;

    private float boltHeadHeight;

    private FillType boltCapFillType;

    private Mesh3D segment;

    private Mesh3D pipe;

    public FlangePipeCreator() {
	segmentCount = 1;
	boltCount = 8;
	rotationSegments = 16;
	flangeOuterRadius = 1;
	flangeGrooveWidth = 0.05f;
	pipeRadius = 0.7f;
	flangeDepth = 0.2f;
	pipeSegmentLength = 5;
	boltHeadPercantage = 0.8f;
	boltHeadHeight = 0.05f;
	boltCapFillType = FillType.N_GON;
    }

    @Override
    public Mesh3D create() {
	initializePipeMesh();
	createSegment();
	createSegments();
	translate();
	return pipe;
    }

    private void createSegments() {
	for (int i = 0; i < segmentCount; i++) {
	    Mesh3D segment = this.segment.copy();
	    segment.translateY(-calculateSegmentHeight() * i);
	    pipe.append(segment);
	}
    }

    private void initializePipeMesh() {
	pipe = new Mesh3D();
    }

    private void translate() {
	pipe.translateY(-calculateSegmentHeight() / 2f);
	pipe.translateY(calculateTotalHeight() / 2f);
	pipe.rotateZ(Mathf.HALF_PI);
    }

    private void createSegment() {
	initializeMesh();
	createFlange();
	createBottomBoltHeads();
	createTopBoltHeads();
	createBoltConnectors();
	segment.translateY(calculateSegmentHeight() / 2f);
    }

    private void initializeMesh() {
	segment = new Mesh3D();
    }

    private void createFlange() {
	VariableCylinderCreator creator = new VariableCylinderCreator();
	creator.setRotationSegments(rotationSegments);
	creator.setCapBottomFillType(FillType.NOTHING);
	creator.setCapTopFillType(FillType.NOTHING);
	creator.add(pipeRadius, 0);
	creator.add(pipeRadius, flangeGrooveWidth);
	creator.add(pipeRadius, 0);
	creator.add(flangeOuterRadius, 0);
	creator.add(flangeOuterRadius, flangeDepth);
	creator.add(pipeRadius, 0);
	creator.add(pipeRadius, pipeSegmentLength);
	creator.add(flangeOuterRadius, 0);
	creator.add(flangeOuterRadius, flangeDepth);
	creator.add(pipeRadius, 0);
	segment.append(creator.create());
    }

    private void createBottomBoltHeads() {
	Mesh3D bottom = createBoltHeads();
	bottom.translateY(-flangeDepth - flangeGrooveWidth - (boltHeadHeight / 2f));
	segment.append(bottom);
    }

    private void createTopBoltHeads() {
	Mesh3D bottom = createBoltHeads();
	bottom.rotateX(Mathf.PI);
	bottom.translateY(-flangeDepth - flangeGrooveWidth - pipeSegmentLength + (boltHeadHeight / 2f));
	segment.append(bottom);
    }

    private Mesh3D createBoltHeads() {
	int n = 6;
	Mesh3D bolts = new Mesh3D();

	CylinderCreator creator = new CylinderCreator();
	creator.setVertices(n);
	creator.setBottomCapFillType(FillType.NOTHING);
	creator.setTopCapFillType(boltCapFillType);
	creator.setBottomRadius(calculateBoltHeadRadius());
	creator.setTopRadius(calculateBoltHeadRadius());
	creator.setHeight(boltHeadHeight);

	CircleCreator circleCreator = new CircleCreator();
	circleCreator.setVertices(boltCount);
	circleCreator.setRadius((flangeOuterRadius + pipeRadius) / 2f);
	Mesh3D circle = circleCreator.create();

	for (int i = 0; i < boltCount; i++) {
	    Mesh3D bolt = creator.create();
	    Vector3f center = circle.getVertexAt(i);
	    bolt.translate(center);
	    bolts.append(bolt);
	}

	return bolts;
    }

    private void createBoltConnectors() {
	int n = 6;
	Mesh3D bolts = new Mesh3D();

	CylinderCreator creator = new CylinderCreator();
	creator.setVertices(n);
	creator.setBottomCapFillType(FillType.NOTHING);
	creator.setTopCapFillType(FillType.NOTHING);
	creator.setBottomRadius(calculateBoltHeadRadius());
	creator.setTopRadius(calculateBoltHeadRadius());
	creator.setHeight(flangeGrooveWidth);

	CircleCreator circleCreator = new CircleCreator();
	circleCreator.setVertices(boltCount);
	circleCreator.setRadius((flangeOuterRadius + pipeRadius) / 2f);
	Mesh3D circle = circleCreator.create();

	for (int i = 0; i < boltCount; i++) {
	    Mesh3D bolt = creator.create();
	    Vector3f center = circle.getVertexAt(i);
	    bolt.translate(center);
	    bolt.translateY(-flangeGrooveWidth / 2f);
	    bolts.append(bolt);
	}

	segment.append(bolts);
    }

    private float calculateBoltHeadRadius() {
	return (flangeOuterRadius - pipeRadius) / 2f * boltHeadPercantage;
    }

    private float calculateSegmentHeight() {
	return pipeSegmentLength + flangeDepth + flangeDepth + flangeGrooveWidth;
    }

    private float calculateTotalHeight() {
	return calculateSegmentHeight() * segmentCount;
    }

    public float getFlangeDepth() {
	return flangeDepth;
    }

    public void setFlangeDepth(float flangeDepth) {
	this.flangeDepth = flangeDepth;
    }

    public float getPipeRadius() {
	return pipeRadius;
    }

    public void setPipeRadius(float pipeRadius) {
	this.pipeRadius = pipeRadius;
    }

    public float getFlangeGrooveWidth() {
	return flangeGrooveWidth;
    }

    public void setFlangeGrooveWidth(float flangeGrooveWidth) {
	this.flangeGrooveWidth = flangeGrooveWidth;
    }

    public float getFlangeOuterRadius() {
	return flangeOuterRadius;
    }

    public void setFlangeOuterRadius(float flangeOuterRadius) {
	this.flangeOuterRadius = flangeOuterRadius;
    }

    public int getSegmentCount() {
	return segmentCount;
    }

    public void setSegmentCount(int segments) {
	this.segmentCount = segments;
    }

    public int getBoltCount() {
	return boltCount;
    }

    public void setBoltCount(int boltCount) {
	this.boltCount = boltCount;
    }

    public float getPipeSegmentLength() {
	return pipeSegmentLength;
    }

    public void setPipeSegmentLength(float pipeSegmentLength) {
	this.pipeSegmentLength = pipeSegmentLength;
    }

    public int getRotationSegments() {
	return rotationSegments;
    }

    public void setRotationSegments(int rotationSegments) {
	this.rotationSegments = rotationSegments;
    }

    public float getBoltHeadPercantage() {
	return boltHeadPercantage;
    }

    public void setBoltHeadPercantage(float boltHeadPercantage) {
	this.boltHeadPercantage = boltHeadPercantage;
    }

    public float getBoltHeadHeight() {
	return boltHeadHeight;
    }

    public void setBoltHeadHeight(float boltHeadHeight) {
	this.boltHeadHeight = boltHeadHeight;
    }

    public FillType getBoltCapFillType() {
	return boltCapFillType;
    }

    public void setBoltCapFillType(FillType boltCapFillType) {
	this.boltCapFillType = boltCapFillType;
    }

}
