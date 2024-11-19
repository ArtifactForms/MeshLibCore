package workspace.ui;

public class MouseEvent {

    private int mouseX;

    private int mouseY;

    private int previousMouseX;

    private int previousMouseY;

    public MouseEvent(int mouseX, int mouseY, int previousMouseX,
            int previousMouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.previousMouseX = previousMouseX;
        this.previousMouseY = previousMouseY;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public int getPreviousMouseX() {
        return previousMouseX;
    }

    public int getPreviousMouseY() {
        return previousMouseY;
    }

}
