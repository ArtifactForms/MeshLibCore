package workspace;

import math.Vector3f;
import mesh.Mesh3D;
import mesh.util.VertexNormals;
import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import workspace.laf.UiConstants;
import workspace.laf.UiValues;
import workspace.render.Mesh3DRenderer;
import workspace.render.ObjectSelectionRender;
import workspace.render.Shading;
import workspace.ui.Color;
import workspace.ui.ui3d.Grid3D;

public class Workspace extends Editor implements ModelListener {

	int vertices;

	int faces;

	private PApplet p;

	private Mesh3DRenderer renderer;

	private FirstPersonView firstPersonView;

	private ObjectSelectionRender selectionRender;

	private SceneObject selectedObject;

	private boolean select;
	
	private Grid3D grid;

	private GraphicsPImpl gImpl;

	public Workspace(PApplet p) {
		grid = new Grid3D(32, 32, 1);
		this.p = p;
		registerMethods();
		firstPersonView = new FirstPersonView(p);
		renderer = new Mesh3DRenderer(p);
		selectionRender = new ObjectSelectionRender(p);
		refreshLoopPreference();
		model.addListener(this);
		gImpl = new GraphicsPImpl(p);
	}

	private void registerMethods() {
		p.registerMethod("pre", this);
		p.registerMethod("draw", this);
		p.registerMethod("post", this);
		p.registerMethod("mouseEvent", this);
		p.registerMethod("keyEvent", this);
	}

	@Override
	public void onModelChanged() {
		super.onModelChanged();
		refreshLoopPreference();
		gizmo.setRotation(new Vector3f(model.getRotationX(), model.getRotationY(),
		    model.getRotationZ()));
		if (!isLoop())
			p.redraw();
	}

	protected void refreshLoopPreference() {
		if (!isLoop()) {
			p.noLoop();
		} else {
			p.loop();
		}
	}

	public void applyTransformations() {
		if (firstPersonView.isEnabled()) {
			firstPersonView.apply();
			p.scale(getScale());
		} else {
			p.translate(p.width / 2, p.height / 2);
			p.translate(getPanningX(), getPanningY());
			p.scale(getScale());
			p.rotateX(getRotationX());
			p.rotateY(getRotationY());
			p.rotateZ(getRotationZ());
		}
	}

	public void applyCamera() {
		firstPersonView.apply();
	}

	public void drawGrid() {
		grid.setVisible(model.isGridVisible());
		grid.render(gImpl);
	}

	protected void drawAxis(float size) {
		p.pushStyle();
		p.pushMatrix();

		p.noFill();
		p.strokeWeight(1.5f / getScale());

		if (isxAxisVisible()) {
			p.stroke(UiValues.getColor(UiConstants.KEY_AXIS_X_COLOR).getRGBA());
			p.line(size, 0, 0, 0, 0, 0);
			p.line(-size, 0, 0, 0, 0, 0);
		}

		if (isyAxisVisible()) {
			p.stroke(UiValues.getColor(UiConstants.KEY_AXIS_Y_COLOR).getRGBA());
			p.line(0, size, 0, 0, 0, 0);
			p.line(0, -size, 0, 0, 0, 0);
		}

		if (iszAxisVisible()) {
			p.stroke(UiValues.getColor(UiConstants.KEY_AXIS_Z_COLOR).getRGBA());
			p.line(0, 0, size, 0, 0, 0);
			p.line(0, 0, -size, 0, 0, 0);
		}

		p.popStyle();
		p.popMatrix();
	}

	public void pre() {
		resize(0, 0, p.width, p.height);
		vertices = 0;
		faces = 0;
		p.background(getBackground().getRGBA());
		p.lights();
		applyTransformations();
		p.strokeWeight(1 / getScale());
		drawGrid();
		drawAxis(2000);
	}

	protected void disableDepthTestFor2dDrawing() {
		p.hint(PApplet.DISABLE_DEPTH_TEST);
	}

	protected void enableDepthTestFor3dDrawing() {
		p.hint(PApplet.ENABLE_DEPTH_TEST);
	}

	protected void drawUI() {
		disableDepthTestFor2dDrawing();
		p.camera();
		p.noLights();
		rootUi.render(gImpl);
		enableDepthTestFor3dDrawing();
	}

	public void draw() {
		drawSelection();
		drawSceneObjects();

		if (selectedObject != null) {
			p.fill(255);
			renderer.drawFaces(selectedObject.getMesh());
		}

		drawUI();

		menu.setText(getInformationString());

		// Debug code
//		p.pushMatrix();
//		p.camera();
//		p.hint(PApplet.DISABLE_DEPTH_TEST);
//		selectionRender.drawColorBuffer();
//		p.hint(PApplet.ENABLE_DEPTH_TEST);
//		p.popMatrix();
	}

	private void drawSelection() {
		selectionRender.draw(sceneObjects);
	}

	public void drawSceneObjects() {
		for (SceneObject sceneObject : sceneObjects) {
			draw(sceneObject.getMesh(), sceneObject.getFillColor());
		}
	}

	public void draw(Mesh3D mesh, Color color) {
		p.pushStyle();
		vertices = mesh.vertices.size();
		faces = mesh.faces.size();

		if (!isWireframe()) {
			if (isEdgesVisible()) {
				p.stroke(0);
			} else {
				p.noStroke();
			}
			p.fill(color.getRed(), color.getGreen(), color.getBlue(),
			    color.getAlpha());
			renderer.drawFaces(mesh, mesh.faces, getShading());
		} else {
			p.stroke(
			    UiValues.getColor(UiConstants.KEY_EDITOR_WIREFRAME_COLOR).getRGBA());
			renderer.drawEdges(mesh);
		}

//        if (isEdgesVisible()) {
//        	p.noFill();
//            renderer.drawEdges(mesh);
//        }

		if (isFaceNormalsVisible()) {
			p.stroke(255);
			renderer.drawFaceNormals(mesh);
		}

		if (isVertexNormalsVisible()) {
			p.stroke(35, 97, 221);
			VertexNormals normals = new VertexNormals(mesh);
			renderer.drawVertexNormals(mesh, normals.getVertexNormals());
		}
		p.popStyle();
	}

	public void drawVertices(Mesh3D mesh) {
		p.pushStyle();
		p.stroke(255);
		p.fill(255);
		p.strokeWeight(0.08f);
		renderer.drawVertices(mesh);
		p.popStyle();
	}

	public void draw(Mesh3D mesh) {
		draw(mesh, new Color(220, 220, 220));
	}

	public void post() {
//		p.saveFrame("output/workspace/workspace_demo####.png");
	}

	protected void onMouseDragged() {
		if (p.mouseButton != 3)
			return;
		float rx = getRotationX() + (p.pmouseY - p.mouseY) * PApplet.TWO_PI / 1000;
		float ry = getRotationY() - (p.pmouseX - p.mouseX) * PApplet.TWO_PI / 1000;
		setRotation(rx, ry, 0);
	}

	protected void onShiftMouseDragged() {
		if (p.mouseButton != 3)
			return;
		float panningX = getPanningX() - ((p.pmouseX - p.mouseX) * 2);
		float panningY = getPanningY() - ((p.pmouseY - p.mouseY) * 2);
		setPanningX(panningX);
		setPanningY(panningY);
	}

	private void handleSelection(int x, int y) {
		SceneObject sceneObject = null;
		String sceneObjectName = selectionRender.getObject(x, y);

		if (sceneObjectName != null) {
			for (SceneObject o : sceneObjects) {
				if (o.getName().equals(sceneObjectName)) {
					sceneObject = o;
					break;
				}
			}
		}
		selectedObject = sceneObject;
	}

	/**
	 * 
	 * @param e
	 */
	public void mouseEvent(MouseEvent e) {
		int action = e.getAction();

		switch (action) {
		case MouseEvent.CLICK:
			select = true;
			handleMouseClicked(e.getX(), e.getY());
			break;
		case MouseEvent.DRAG:
			handleMouseDragged(e.getX(), e.getY());
			if (e.isShiftDown()) {
				onShiftMouseDragged();
				break;
			}
			onMouseDragged();
			break;
		case MouseEvent.WHEEL:
			handleMouseWheel(e.getCount());
			break;
		case MouseEvent.RELEASE:
			handleMouseReleased(e.getX(), e.getY());
			break;
		case MouseEvent.PRESS:
			handleMousePressed(e.getX(), e.getY());
			break;
		}
		// Model?
		if (!isLoop())
			p.redraw();
	}

	public void keyEvent(KeyEvent e) {
		if (!isUseKeyBindings())
			return;

		if (e.getAction() != KeyEvent.TYPE)
			return;

		switch (e.getKey()) {
		case '4':
			if (!firstPersonView.isEnabled()) {
				setLoop(true);
			} else {
				p.redraw();
			}
			firstPersonView.setEnabled(!firstPersonView.isEnabled());
			commands.getCommand('s').setEnabled(!firstPersonView.isEnabled());
			break;
		default:
			commands.execute(e.getKey());
			break;
		}
	}

	protected String getInformationString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Verts:");
		buffer.append(vertices);
		buffer.append(" | Faces:");
		buffer.append(faces);
		buffer.append(" | FPS:");
		buffer.append(p.frameRate);
		buffer.append(" | FrameCount:");
		buffer.append(p.frameCount);
		return buffer.toString();
	}

	public Mesh3DRenderer getRenderer() {
		return renderer;
	}

	public SceneObject getSceneObject(int mouseX, int mouseY) {
		String objectName = selectionRender.getObject(mouseX, mouseY);

		if (objectName == null)
			return null;
		for (SceneObject sceneObject : sceneObjects) {
			if (sceneObject.getName().equals(objectName)) {
				return sceneObject;
			}
		}
		return null;
	}

	public float getPanningX() {
		return model.getPanningX();
	}

	public void setPanningX(float panningX) {
		model.setPanningX(panningX);
	}

	public float getPanningY() {
		return model.getPanningY();
	}

	public void setPanningY(float panningY) {
		model.setPanningY(panningY);
	}

	public float getRotationX() {
		return model.getRotationX();
	}

	public float getRotationY() {
		return model.getRotationY();
	}

	public float getRotationZ() {
		return model.getRotationZ();
	}

	public void setRotation(float rx, float ry, float rz) {
		model.setRotation(rx, ry, rz);
	}

	public float getScale() {
		return model.getScale();
	}

	public void setScale(float scale) {
		model.setScale(scale);
	}

	public WorkspaceModel getModel() {
		return model;
	}

	public boolean isxAxisVisible() {
		return model.isxAxisVisible();
	}

	public void setxAxisVisible(boolean xAxisVisible) {
		model.setxAxisVisible(xAxisVisible);
	}

	public boolean isyAxisVisible() {
		return model.isyAxisVisible();
	}

	public void setyAxisVisible(boolean yAxisVisible) {
		model.setyAxisVisible(yAxisVisible);
	}

	public boolean iszAxisVisible() {
		return model.iszAxisVisible();
	}

	public void setzAxisVisible(boolean zAxisVisible) {
		model.setzAxisVisible(zAxisVisible);
	}

	public boolean isGridVisible() {
		return model.isGridVisible();
	}

	public void setGridVisible(boolean gridVisible) {
		model.setGridVisible(gridVisible);
	}

	public boolean isFaceNormalsVisible() {
		return model.isFaceNormalsVisible();
	}

	public void setFaceNormalsVisible(boolean faceNormalsVisible) {
		model.setFaceNormalsVisible(faceNormalsVisible);
	}

	public boolean isVertexNormalsVisible() {
		return model.isVertexNormalsVisible();
	}

	public void setVertexNormalsVisible(boolean vertexNormalsVisible) {
		model.setVertexNormalsVisible(vertexNormalsVisible);
	}

	public boolean isEdgesVisible() {
		return model.isEdgesVisible();
	}

	public void setEdgesVisible(boolean edgesVisible) {
		model.setEdgesVisible(edgesVisible);
	}

	public boolean isUiVisible() {
		return model.isUiVisible();
	}

	public void setUiVisible(boolean uiVisible) {
		model.setUiVisible(uiVisible);
	}

	public boolean isWireframe() {
		return model.isWireframe();
	}

	public void setWireframe(boolean wireframe) {
		model.setWireframe(wireframe);
	}

	public boolean isLoop() {
		return model.isLoop();
	}

	public void setLoop(boolean loop) {
		model.setLoop(loop);
	}

	public boolean isUseKeyBindings() {
		return model.isUseKeyBindings();
	}

	public void setUseKeyBindings(boolean useKeyBindings) {
		model.setUseKeyBindings(useKeyBindings);
	}

	public Color getBackground() {
		return model.getBackground();
	}

	public void setBackground(Color background) {
		model.setBackground(background);
	}

	public Shading getShading() {
		return model.getShading();
	}

	public void setShading(Shading shading) {
		model.setShading(shading);
	}

}
