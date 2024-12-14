package workspace;

import java.util.ArrayList;
import java.util.List;

import math.Mathf;
import workspace.laf.UiConstants;
import workspace.laf.UiValues;
import workspace.render.Shading;
import workspace.ui.Color;

public class WorkspaceModel {

	private float panningX;

	private float panningY;

	private float rotationX;

	private float rotationY;

	private float rotationZ;

	private float scale;

	private float minScale;

	private float maxScale;

	private boolean xAxisVisible;

	private boolean yAxisVisible;

	private boolean zAxisVisible;

	private boolean gridVisible;

	private boolean faceNormalsVisible;

	private boolean vertexNormalsVisible;

	private boolean edgesVisible;

	private boolean uiVisible;

	private boolean wireframe;

	private boolean loop;

	private boolean useKeyBindings;

	private Shading shading;

	private Color background;

	private List<ModelListener> listeners;

	public WorkspaceModel() {
		scale = 100;
		minScale = 1;
		maxScale = 1000;
		rotationX = Mathf.toRadians(-30);
		rotationY = Mathf.toRadians(30);
		xAxisVisible = false;
		yAxisVisible = false;
		zAxisVisible = false;
		gridVisible = false;
		faceNormalsVisible = false;
		vertexNormalsVisible = false;
		edgesVisible = true;
		uiVisible = true;
		wireframe = false;
		loop = false;
		useKeyBindings = true;
		shading = Shading.FLAT;
		background = UiValues.getColor(UiConstants.KEY_EDITOR_BACKGROUND_COLOR);
		listeners = new ArrayList<ModelListener>();
	}

	public float getPanningX() {
		return panningX;
	}

	public void setPanningX(float panningX) {
		if (this.panningX == panningX)
			return;
		this.panningX = panningX;
		fireChangeEvent();
	}

	public float getPanningY() {
		return panningY;
	}

	public void setPanningY(float panningY) {
		if (this.panningY == panningY)
			return;
		this.panningY = panningY;
		fireChangeEvent();
	}

	public float getRotationX() {
		return rotationX;
	}

	public void setRotationX(float rotationX) {
		if (this.rotationX == rotationX)
			return;
		this.rotationX = rotationX;
		fireChangeEvent();
	}

	public float getRotationY() {
		return rotationY;
	}

	public void setRotationY(float rotationY) {
		if (this.rotationY == rotationY)
			return;
		this.rotationY = rotationY;
		fireChangeEvent();
	}

	public float getRotationZ() {
		return rotationZ;
	}

	public void setRotationZ(float rotationZ) {
		if (this.rotationZ == rotationZ)
			return;
		this.rotationZ = rotationZ;
		fireChangeEvent();
	}

	public void setRotation(float rx, float ry, float rz) {
		if (rotationX == rx && rotationY == ry && rotationZ == rz)
			return;
		rotationX = rx;
		rotationY = ry;
		rotationZ = rz;
		fireChangeEvent();
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		scale = Mathf.clamp(scale, getMinScale(), getMaxScale());
		if (this.scale == scale)
			return;
		this.scale = scale;
		fireChangeEvent();
	}

	public float getMinScale() {
		return minScale;
	}

	public void setMinScale(float minScale) {
		if (this.minScale == minScale)
			return;
		this.minScale = minScale;
		fireChangeEvent();
	}

	public float getMaxScale() {
		return maxScale;
	}

	public void setMaxScale(float maxScale) {
		if (this.maxScale == maxScale)
			return;
		this.maxScale = maxScale;
		fireChangeEvent();
	}

	public boolean isxAxisVisible() {
		return xAxisVisible;
	}

	public void setxAxisVisible(boolean xAxisVisible) {
		if (this.xAxisVisible == xAxisVisible)
			return;
		this.xAxisVisible = xAxisVisible;
		fireChangeEvent();
	}

	public boolean isyAxisVisible() {
		return yAxisVisible;
	}

	public void setyAxisVisible(boolean yAxisVisible) {
		if (this.yAxisVisible == yAxisVisible)
			return;
		this.yAxisVisible = yAxisVisible;
		fireChangeEvent();
	}

	public boolean iszAxisVisible() {
		return zAxisVisible;
	}

	public void setzAxisVisible(boolean zAxisVisible) {
		if (this.zAxisVisible == zAxisVisible)
			return;
		this.zAxisVisible = zAxisVisible;
		fireChangeEvent();
	}

	public boolean isGridVisible() {
		return gridVisible;
	}

	public void setGridVisible(boolean gridVisible) {
		if (this.gridVisible == gridVisible)
			return;
		this.gridVisible = gridVisible;
		fireChangeEvent();
	}

	public boolean isFaceNormalsVisible() {
		return faceNormalsVisible;
	}

	public void setFaceNormalsVisible(boolean faceNormalsVisible) {
		if (this.faceNormalsVisible == faceNormalsVisible)
			return;
		this.faceNormalsVisible = faceNormalsVisible;
		fireChangeEvent();
	}

	public boolean isVertexNormalsVisible() {
		return vertexNormalsVisible;
	}

	public void setVertexNormalsVisible(boolean vertexNormalsVisible) {
		if (this.vertexNormalsVisible == vertexNormalsVisible)
			return;
		this.vertexNormalsVisible = vertexNormalsVisible;
		fireChangeEvent();
	}

	public boolean isEdgesVisible() {
		return edgesVisible;
	}

	public void setEdgesVisible(boolean edgesVisible) {
		if (this.edgesVisible == edgesVisible)
			return;
		this.edgesVisible = edgesVisible;
		fireChangeEvent();
	}

	public boolean isUiVisible() {
		return uiVisible;
	}

	public void setUiVisible(boolean uiVisible) {
		if (this.uiVisible == uiVisible)
			return;
		this.uiVisible = uiVisible;
		fireChangeEvent();
	}

	public boolean isWireframe() {
		return wireframe;
	}

	public void setWireframe(boolean wireframe) {
		if (this.wireframe == wireframe)
			return;
		this.wireframe = wireframe;
		fireChangeEvent();
	}

	public boolean isLoop() {
		return loop;
	}

	public void setLoop(boolean loop) {
		if (this.loop == loop)
			return;
		this.loop = loop;
		fireChangeEvent();
	}

	public Shading getShading() {
		return shading;
	}

	public void setShading(Shading shading) {
		if (this.shading == shading)
			return;
		this.shading = shading;
		fireChangeEvent();
	}

	public Color getBackground() {
		return background;
	}

	public void setBackground(Color background) {
		if (this.background.equals(background))
			return;
		this.background = background;
		fireChangeEvent();
	}

	public boolean isUseKeyBindings() {
		return useKeyBindings;
	}

	public void setUseKeyBindings(boolean useKeyBindings) {
		if (this.useKeyBindings == useKeyBindings)
			return;
		this.useKeyBindings = useKeyBindings;
		fireChangeEvent();
	}

	public void fireChangeEvent() {
		for (ModelListener l : listeners) {
			l.onModelChanged();
		}
	}

	public void addListener(ModelListener listener) {
		if (listener == null)
			return;
		listeners.add(listener);
	}

}
