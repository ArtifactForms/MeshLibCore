package mesh.selection;

import java.util.ArrayList;
import java.util.List;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;

public class FaceSelectionRules implements IFaceSelectionRule {

    public enum Mode {
        AND, OR
    }

    private Mode mode = Mode.AND;

    private boolean valid;

    private List<IFaceSelectionRule> rules;

    public FaceSelectionRules() {
        rules = new ArrayList<IFaceSelectionRule>();
    }

    @Override
    public boolean isValid(Mesh3D mesh, Face3D face) {
        valid = mode == Mode.AND ? true : false;

        for (IFaceSelectionRule rule : rules) {
            if (mode == Mode.AND)
                valid &= rule.isValid(mesh, face);
            if (mode == Mode.OR)
                valid |= rule.isValid(mesh, face);
        }

        return valid;
    }

    public void apply(FaceSelection selection, boolean invert) {
        selection.clear();
        selection.select(this);
        if (invert)
            selection.invert();
    }

    public void apply(FaceSelection selection) {
        apply(selection, false);
    }

    public void add(IFaceSelectionRule rule) {
        rules.add(rule);
    }

    public void isQuad() {
        add(new SelectFaceRuleVertexCount(4, CompareType.EQUALS));
    }

    public void isTriangle() {
        add(new SelectFaceRuleVertexCount(3, CompareType.EQUALS));
    }

    public void isNGon(int n) {
        add(new SelectFaceRuleVertexCount(n, CompareType.EQUALS));
    }

    public void centerDistanceToOrigin(Vector3f origin, CompareType compare,
            float distance) {
        add(new SelectFaceRuleCenterDistance(origin, compare, distance));
    }

    public void centerX(CompareType compare, float x) {
        add(new SelectFaceRuleCenterX(compare, x));
    }

    public void centerY(CompareType compare, float y) {
        add(new SelectFaceRuleCenterY(compare, y));
    }

    public void centerZ(CompareType compare, float z) {
        add(new SelectFaceRuleCenterZ(compare, z));
    }

    public void similarNormal(Vector3f normal, float threshold) {
        add(new SelectFaceRuleSimilarNormal(threshold, normal));
    }

    public void clear() {
        rules.clear();
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

}
