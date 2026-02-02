package workspace.laf;

import math.Color;

public class LookAndFeel {

  public static void setup() {
    setupGizmo();
    setupAxis();
    setupMenu();
    UiValues.put(UiConstants.KEY_EDITOR_BACKGROUND_COLOR, Color.getColorFromInt(60, 60, 60));
    UiValues.put(UiConstants.KEY_GRID_COLOR, Color.getColorFromInt(74, 74, 74));
    UiValues.put(UiConstants.KEY_EDITOR_WIREFRAME_COLOR, Color.getColorFromInt(241, 152, 45));
  }

  private static void setupAxis() {
    UiValues.put(UiConstants.KEY_AXIS_X_COLOR, Color.getColorFromInt(157, 67, 80));
    UiValues.put(UiConstants.KEY_AXIS_Y_COLOR, Color.getColorFromInt(109, 148, 46));
    UiValues.put(UiConstants.KEY_AXIS_Z_COLOR, Color.getColorFromInt(63, 112, 162));
  }

  private static void setupGizmo() {
    UiValues.put(UiConstants.KEY_GIZMO_AXIS_X_COLOR, Color.getColorFromInt(221, 56, 79));
    UiValues.put(UiConstants.KEY_GIZMO_AXIS_Y_COLOR, Color.getColorFromInt(120, 181, 22));
    UiValues.put(UiConstants.KEY_GIZMO_AXIS_Z_COLOR, Color.getColorFromInt(44, 142, 252));
    UiValues.put(UiConstants.KEY_GIZMO_CENTER_COLOR, Color.getColorFromInt(200, 200, 200));
  }

  private static void setupMenu() {
    UiValues.put(UiConstants.KEY_MENU_FOREGROUND_COLOR, Color.getColorFromInt(151, 151, 151));
    UiValues.put(UiConstants.KEY_MENU_BACKGROUND_COLOR, Color.getColorFromInt(35, 35, 35));
    UiValues.put(UiConstants.KEY_MENU_TEXT_SIZE, 12);
  }
}
