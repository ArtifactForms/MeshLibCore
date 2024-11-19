package workspace;

import math.Mathf;
import math.Matrix3f;
import math.Matrix4f;
import math.Vector3f;
import processing.core.PApplet;
import processing.core.PMatrix;
import processing.event.KeyEvent;

public class FirstPersonView {

    private boolean enabled;

    private boolean left;

    private boolean right;

    private boolean forward;

    private boolean back;

    private boolean up;

    private boolean down;

    private float pitch = Mathf.PI;

    private float yaw = 0;

    private Vector3f eye = new Vector3f(-1000, 0, 1000);

    private float speed = 10;

    private PApplet context;

    public FirstPersonView(PApplet context) {
        this.context = context;
        context.registerMethod("pre", this);
        context.registerMethod("keyEvent", this);
    }

    public void pre() {
        if (!enabled)
            return;
        yaw = Mathf.map(context.mouseX, 0, context.width, Mathf.PI, -Mathf.PI);
        pitch = Mathf
                .map(context.mouseY, 0, context.height, -Mathf.PI, Mathf.PI);

//		if (pitch > 89)
//			pitch = 89;
//		if (pitch < -89)
//			pitch = -89;

        Vector3f front = new Vector3f();
        float x = Mathf.cos(Mathf.toRadians(yaw))
                * Mathf.cos(Mathf.toRadians(pitch));
        float y = Mathf.sin(Mathf.toRadians(pitch));
        float z = Mathf.cos(Mathf.toRadians(yaw))
                * Mathf.cos(Mathf.toRadians(pitch));
        front.set(x, y, z);

        Vector3f velocity = new Vector3f();

        if (left) {
            velocity.addLocal(-1, 0, 0);
        }

        if (right) {
            velocity.addLocal(1, 0, 0);
        }

        if (back) {
            velocity.addLocal(0, 0, 1);
        }

        if (forward) {
            velocity.addLocal(0, 0, -1);
        }

        velocity.multLocal(getRotationMatrix(yaw));

        eye.addLocal(velocity.mult(speed));
        eye.setY(-300);
    }

    public void apply() {
        Matrix4f m = Matrix4f.fpsViewRH(eye, pitch, yaw).transpose();
        PMatrix matrix = context.getMatrix();
        matrix.set(m.getValues());
        context.setMatrix(matrix);
    }

    public void keyEvent(KeyEvent key) {
        if (key.getAction() == KeyEvent.PRESS)
            onKeyPressed(key.getKey());
        if (key.getAction() == KeyEvent.RELEASE)
            onKeyReleased(key.getKey());
    }

    public void onKeyPressed(char key) {
        if (key == 'w' || key == 'W')
            forward = true;

        if (key == 's' || key == 'S')
            back = true;

        if (key == 'a' || key == 'A')
            left = true;

        if (key == 'd' || key == 'D')
            right = true;

        if (key == ' ')
            up = true;

        if (key == 'c' || key == 'C')
            down = true;
    }

    public void onKeyReleased(char key) {
        if (key == 'w' || key == 'W')
            forward = false;

        if (key == 's' || key == 'S')
            back = false;

        if (key == 'a' || key == 'A')
            left = false;

        if (key == 'd' || key == 'D')
            right = false;

        if (key == ' ')
            up = false;

        if (key == 'c' || key == 'C')
            down = false;
    }

    public Matrix3f getRotationMatrix(float angle) {
        Matrix3f m = new Matrix3f(
                Mathf.cos(angle), 0, Mathf.sin(angle), 0, 1, 0,
                -Mathf.sin(angle), 0, Mathf.cos(angle)
        );
        return m;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
