package mesh.animator;

import math.Vector3f;
import mesh.Mesh3D;

public class Morpher extends AbstractAnimator {

    private float maxDistance = 0;

    private float minDistance = 0;

    private float speed = 60;

    private int count;

    private Mesh3D mesh;

    private Mesh3D source;

    private Mesh3D target;

    public Morpher(Mesh3D source, Mesh3D target) {
        this.source = source;
        this.target = target;
        this.mesh = source.copy();
        calculateDistanceValues();
    }

    private void calculateDistanceValues() {
        for (int i = 0; i < source.vertices.size(); i++) {
            Vector3f v0 = mesh.getVertexAt(i);
            Vector3f v1 = target.getVertexAt(i);
            float distance = v0.distance(v1);
            maxDistance = distance > maxDistance ? distance : maxDistance;
            minDistance = distance < minDistance ? distance : minDistance;
        }
    }

    private float getSpeedForVertexAt(int i) {
        Vector3f v0 = source.getVertexAt(i);
        Vector3f v1 = target.getVertexAt(i);
        float distance0 = v0.distance(v1);
        return speed * (distance0 / maxDistance);
    }

    @Override
    public void onUpdate(float tpf) {
        for (int i = 0; i < source.vertices.size(); i++) {
            Vector3f v0 = mesh.getVertexAt(i);
            Vector3f target = this.target.vertices.get(i);

            float speed = getSpeedForVertexAt(i) * tpf;

            Vector3f position = new Vector3f(v0);
            Vector3f tmp = new Vector3f(position);
            Vector3f velocity = new Vector3f();

            velocity = this.target.getVertexAt(i).subtract(this.mesh.getVertexAt(i)).normalize().mult(speed);

            tmp.addLocal(velocity);

            if (position.getX() <= target.getX() && tmp.getX() >= target.getX())
                tmp.setX(target.getX());

            if (position.getX() >= target.getX() && tmp.getX() <= target.getX())
                tmp.setX(target.getX());

            if (position.getY() <= target.getY() && tmp.getY() >= target.getY())
                tmp.setY(target.getY());

            if (position.getY() >= target.getY() && tmp.getY() <= target.getY())
                tmp.setY(target.getY());

            if (position.getZ() <= target.getZ() && tmp.getZ() >= target.getZ())
                tmp.setZ(target.getZ());

            if (position.getZ() >= target.getZ() && tmp.getZ() <= target.getZ())
                tmp.setZ(target.getZ());

            v0.set(tmp.getX(), tmp.getY(), tmp.getZ());

            if (tmp.equals(target)) {
                count++;
            }

            if (count == source.vertices.size()) {
                setFinished(true);
            }
        }

        count = 0;
    }

    public void toggle() {
        Mesh3D tmp = source;
        source = target;
        target = tmp;
        mesh = source.copy();
        calculateDistanceValues();
    }

    public Mesh3D getMesh() {
        return mesh;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public void restore() {
        this.mesh = source.copy();
        calculateDistanceValues();
        setFinished(false);
    }

}
