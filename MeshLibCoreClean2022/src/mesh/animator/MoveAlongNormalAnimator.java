package mesh.animator;

import java.util.ArrayList;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;

public class MoveAlongNormalAnimator extends AbstractAnimator {

    private float lifeTime;
    private float time;
    private float speed;
    private Mesh3D mesh;
    private Face3D face;
    private ArrayList<Vector3f> originals;

    public MoveAlongNormalAnimator(Mesh3D mesh, Face3D face, float speed, float lifeTime) {
	this.mesh = mesh;
	this.face = face;
	this.speed = speed;
	this.originals = new ArrayList<Vector3f>();
	this.lifeTime = lifeTime;
	storeOriginals();
    }

    public MoveAlongNormalAnimator(Mesh3D mesh, Face3D face) {
	this.mesh = mesh;
	this.face = face;
	this.originals = new ArrayList<Vector3f>();
	storeOriginals();
    }

    private void storeOriginals() {
	for (int i = 0; i < face.indices.length; i++) {
	    Vector3f v = mesh.getVertexAt(face.indices[i]);
	    originals.add(new Vector3f(v));
	}
    }

    public void restore() {
	for (int i = 0; i < face.indices.length; i++) {
	    Vector3f v = mesh.getVertexAt(face.indices[i]);
	    Vector3f v0 = originals.get(i);
	    v.set(v0);
	}
	time = 0;
	setFinished(false);
    }

    @Override
    public void onUpdate(float tpf) {
	time += tpf;

	if (time >= lifeTime) {
	    setFinished(true);
	    return;
	}

	Vector3f normal = mesh.calculateFaceCenter(face);
	for (int i = 0; i < face.indices.length; i++) {
	    Vector3f v = mesh.getVertexAt(face.indices[i]);
	    v.addLocal(normal.mult(speed * tpf));
	}
    }

    public float getLifeTime() {
	return lifeTime;
    }

    public void setLifeTime(float lifeTime) {
	this.lifeTime = lifeTime;
    }

    public float getSpeed() {
	return speed;
    }

    public void setSpeed(float speed) {
	this.speed = speed;
    }

}
