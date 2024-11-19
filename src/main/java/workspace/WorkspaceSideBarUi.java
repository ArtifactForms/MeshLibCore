package workspace;

import workspace.laf.UiValues;
import workspace.render.Shading;
import workspace.ui.Color;
import workspace.ui.IActionListener;
import workspace.ui.UiCheckBox;
import workspace.ui.UiComponent;
import workspace.ui.UiLabel;
import workspace.ui.UiPanel;

public class WorkspaceSideBarUi extends UiComponent implements ModelListener {

    private int xOffset = 10;

    private int yOffset = 65;

    private UiCheckBox gridCheckBox;

    private UiCheckBox faceNormalsCheckBox;

    private UiCheckBox vertexNormalsCheckBox;

    private UiCheckBox wireFrameCheckBox;

    private UiCheckBox xAxisCheckBox;

    private UiCheckBox yAxisCheckBox;

    private UiCheckBox zAxisCheckBox;

    private UiCheckBox edgeCheckBox;

    private UiCheckBox smoothShadingCheckBox;

    private UiCheckBox loopCheckBox;

    private UiLabel label;

    private WorkspaceModel model;

    public WorkspaceSideBarUi(WorkspaceModel model) {
        this.model = model;
        this.model.addListener(this);
        createUI();
    }

    @Override
    public void onModelChanged() {
        gridCheckBox.setSelected(model.isGridVisible());
        faceNormalsCheckBox.setSelected(model.isFaceNormalsVisible());
        vertexNormalsCheckBox.setSelected(model.isVertexNormalsVisible());
        wireFrameCheckBox.setSelected(model.isWireframe());
        xAxisCheckBox.setSelected(model.isxAxisVisible());
        yAxisCheckBox.setSelected(model.isyAxisVisible());
        zAxisCheckBox.setSelected(model.iszAxisVisible());
        edgeCheckBox.setSelected(model.isEdgesVisible());
        smoothShadingCheckBox.setSelected(model.getShading() == Shading.SMOOTH);
        loopCheckBox.setSelected(model.isLoop());
    }

    private void createUI() {
        UiPanel panel = new UiPanel();
        panel.setWidth(200);
        panel.setHeight(500);
        panel.setBackground(new Color(77, 77, 77, 0));
        panel.add(getUiLabel());
        panel.add(getGridCheckBox());
        panel.add(getFaceNormalsCheckBox());
        panel.add(getVertexNormalsCheckBox());
        panel.add(getWireFrameCheckBox());
        panel.add(getXAxisCheckBox());
        panel.add(getYAxisCheckBox());
        panel.add(getZAxisCheckBox());
        panel.add(getEdgeCheckBox());
        panel.add(getSmoothShadingCheckBox());
        panel.add(getLoopCheckBox());
        add(panel);
    }

    protected UiLabel getUiLabel() {
        if (label != null)
            return label;

        label = new UiLabel();
        label.setX(xOffset);
        label.setY(yOffset);
        label.setTitle("Controls:");
        label.setBackground(new Color(0, 0, 0, 0));
        label.setForeground(UiValues.UI_ELEMENT_FOREGROUND);
        return label;
    }

    protected UiCheckBox getGridCheckBox() {
        if (gridCheckBox != null)
            return gridCheckBox;

        gridCheckBox = new UiCheckBox("Grid (G)");
        gridCheckBox.setX(xOffset);
        gridCheckBox.setY(yOffset + 20);
        gridCheckBox.setSelected(model.isGridVisible());
        gridCheckBox.setForeground(UiValues.UI_ELEMENT_FOREGROUND);
        gridCheckBox.setActionListener(new IActionListener() {

            @Override
            public void onActionPerformed() {
                model.setGridVisible(gridCheckBox.isSelected());
            }
        });

        return gridCheckBox;
    }

    protected UiCheckBox getFaceNormalsCheckBox() {
        if (faceNormalsCheckBox != null)
            return faceNormalsCheckBox;

        faceNormalsCheckBox = new UiCheckBox("Face Normals (N)");
        faceNormalsCheckBox.setX(xOffset);
        faceNormalsCheckBox.setY(yOffset + 40);
        faceNormalsCheckBox.setSelected(model.isFaceNormalsVisible());
        faceNormalsCheckBox.setForeground(UiValues.UI_ELEMENT_FOREGROUND);
        faceNormalsCheckBox.setActionListener(new IActionListener() {

            @Override
            public void onActionPerformed() {
                model.setFaceNormalsVisible(faceNormalsCheckBox.isSelected());
            }
        });

        return faceNormalsCheckBox;
    }

    protected UiCheckBox getVertexNormalsCheckBox() {
        if (vertexNormalsCheckBox != null)
            return vertexNormalsCheckBox;

        vertexNormalsCheckBox = new UiCheckBox("Vertex Normals (V)");
        vertexNormalsCheckBox.setX(xOffset);
        vertexNormalsCheckBox.setY(yOffset + 60);
        vertexNormalsCheckBox.setSelected(model.isVertexNormalsVisible());
        vertexNormalsCheckBox.setForeground(UiValues.UI_ELEMENT_FOREGROUND);
        vertexNormalsCheckBox.setActionListener(new IActionListener() {

            @Override
            public void onActionPerformed() {
                model.setVertexNormalsVisible(
                        vertexNormalsCheckBox.isSelected()
                );
            }
        });

        return vertexNormalsCheckBox;
    }

    protected UiCheckBox getWireFrameCheckBox() {
        if (wireFrameCheckBox != null)
            return wireFrameCheckBox;

        wireFrameCheckBox = new UiCheckBox("Wireframe (Z)");
        wireFrameCheckBox.setX(xOffset);
        wireFrameCheckBox.setY(yOffset + 80);
        wireFrameCheckBox.setSelected(model.isWireframe());
        wireFrameCheckBox.setForeground(UiValues.UI_ELEMENT_FOREGROUND);
        wireFrameCheckBox.setActionListener(new IActionListener() {

            @Override
            public void onActionPerformed() {
                model.setWireframe(wireFrameCheckBox.isSelected());
            }
        });

        return wireFrameCheckBox;
    }

    protected UiCheckBox getXAxisCheckBox() {
        if (xAxisCheckBox != null)
            return xAxisCheckBox;

        xAxisCheckBox = new UiCheckBox("X-Axis (1)");
        xAxisCheckBox.setX(xOffset);
        xAxisCheckBox.setY(yOffset + 100);
        xAxisCheckBox.setSelected(model.isxAxisVisible());
        xAxisCheckBox.setForeground(UiValues.UI_ELEMENT_FOREGROUND);
        xAxisCheckBox.setActionListener(new IActionListener() {

            @Override
            public void onActionPerformed() {
                model.setxAxisVisible(xAxisCheckBox.isSelected());
            }
        });

        return xAxisCheckBox;
    }

    protected UiCheckBox getYAxisCheckBox() {
        if (yAxisCheckBox != null)
            return yAxisCheckBox;

        yAxisCheckBox = new UiCheckBox("Y-Axis (2)");
        yAxisCheckBox.setX(xOffset);
        yAxisCheckBox.setY(yOffset + 120);
        yAxisCheckBox.setSelected(model.isyAxisVisible());
        yAxisCheckBox.setForeground(UiValues.UI_ELEMENT_FOREGROUND);
        yAxisCheckBox.setActionListener(new IActionListener() {

            @Override
            public void onActionPerformed() {
                model.setyAxisVisible(yAxisCheckBox.isSelected());
            }
        });

        return yAxisCheckBox;
    }

    protected UiCheckBox getZAxisCheckBox() {
        if (zAxisCheckBox != null)
            return zAxisCheckBox;

        zAxisCheckBox = new UiCheckBox("Z-Axis (3)");
        zAxisCheckBox.setX(xOffset);
        zAxisCheckBox.setY(yOffset + 140);
        zAxisCheckBox.setSelected(model.iszAxisVisible());
        zAxisCheckBox.setForeground(UiValues.UI_ELEMENT_FOREGROUND);
        zAxisCheckBox.setActionListener(new IActionListener() {

            @Override
            public void onActionPerformed() {
                model.setzAxisVisible(zAxisCheckBox.isSelected());
            }
        });

        return zAxisCheckBox;
    }

    protected UiCheckBox getEdgeCheckBox() {
        if (edgeCheckBox != null)
            return edgeCheckBox;

        edgeCheckBox = new UiCheckBox("Edges (E)");
        edgeCheckBox.setX(xOffset);
        edgeCheckBox.setY(yOffset + 160);
        edgeCheckBox.setSelected(model.isEdgesVisible());
        edgeCheckBox.setForeground(UiValues.UI_ELEMENT_FOREGROUND);
        edgeCheckBox.setActionListener(new IActionListener() {

            @Override
            public void onActionPerformed() {
                model.setEdgesVisible(edgeCheckBox.isSelected());
            }
        });

        return edgeCheckBox;
    }

    protected UiCheckBox getSmoothShadingCheckBox() {
        if (smoothShadingCheckBox != null)
            return smoothShadingCheckBox;

        smoothShadingCheckBox = new UiCheckBox("Shade Smooth (S)");
        smoothShadingCheckBox.setX(xOffset);
        smoothShadingCheckBox.setY(yOffset + 180);
        smoothShadingCheckBox.setSelected(model.getShading() == Shading.SMOOTH);
        smoothShadingCheckBox.setForeground(UiValues.UI_ELEMENT_FOREGROUND);
        smoothShadingCheckBox.setActionListener(new IActionListener() {

            @Override
            public void onActionPerformed() {
                model.setShading(
                        smoothShadingCheckBox.isSelected() ? Shading.SMOOTH
                                : Shading.FLAT
                );
            }
        });

        return smoothShadingCheckBox;

    }

    protected UiCheckBox getLoopCheckBox() {
        if (loopCheckBox != null)
            return loopCheckBox;

        loopCheckBox = new UiCheckBox("Loop");
        loopCheckBox.setX(xOffset);
        loopCheckBox.setY(yOffset + 200);
        loopCheckBox.setSelected(model.isLoop());
        loopCheckBox.setForeground(UiValues.UI_ELEMENT_FOREGROUND);
        loopCheckBox.setActionListener(new IActionListener() {

            @Override
            public void onActionPerformed() {
                model.setLoop(loopCheckBox.isSelected());
            }
        });

        return loopCheckBox;
    }

    @Override
    public boolean contains(int x, int y) {
        return true;
    }

}
