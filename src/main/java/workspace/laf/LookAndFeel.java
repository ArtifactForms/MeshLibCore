package workspace.laf;

import workspace.ui.Color;

public class LookAndFeel {

    public static void setup() {
        setupGizmo();
        setupAxis();
        setupMenu();
        UiValues.put(
                UiConstants.KEY_EDITOR_BACKGROUND_COLOR, new Color(60, 60, 60)
        );
        UiValues.put(UiConstants.KEY_GRID_COLOR, new Color(74, 74, 74));
        UiValues.put(
                UiConstants.KEY_EDITOR_WIREFRAME_COLOR, new Color(241, 152, 45)
        );
    }

    private static void setupAxis() {
        UiValues.put(UiConstants.KEY_AXIS_X_COLOR, new Color(157, 67, 80));
        UiValues.put(UiConstants.KEY_AXIS_Y_COLOR, new Color(109, 148, 46));
        UiValues.put(UiConstants.KEY_AXIS_Z_COLOR, new Color(63, 112, 162));
    }

    private static void setupGizmo() {
        UiValues.put(
                UiConstants.KEY_GIZMO_AXIS_X_COLOR, new Color(221, 56, 79)
        );
        UiValues.put(
                UiConstants.KEY_GIZMO_AXIS_Y_COLOR, new Color(120, 181, 22)
        );
        UiValues.put(
                UiConstants.KEY_GIZMO_AXIS_Z_COLOR, new Color(44, 142, 252)
        );
        UiValues.put(
                UiConstants.KEY_GIZMO_CENTER_COLOR, new Color(200, 200, 200)
        );
    }

    private static void setupMenu() {
        UiValues.put(
                UiConstants.KEY_MENU_FOREGROUND_COLOR, new Color(151, 151, 151)
        );
        UiValues.put(
                UiConstants.KEY_MENU_BACKGROUND_COLOR, new Color(35, 35, 35)
        );
        UiValues.put(UiConstants.KEY_MENU_TEXT_SIZE, 12);
    }

}
