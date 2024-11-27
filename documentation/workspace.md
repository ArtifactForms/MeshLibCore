# The Workspace: A Minimal 3D Mesh Viewer

The Workspace is a streamlined tool designed for visualizing and inspecting 3D models. It's tightly integrated
with the Processing environment, leveraging its powerful rendering capabilities. While it doesn't aspire
to be a comprehensive 3D modeling suite, it provides essential features for working with mesh data.

## Getting Started
To use the Workspace, simply create an instance and provide a reference to your Processing
sketch (PApplet). No immediate interaction with the Workspace is necessary in the draw() method.
The Workspace is ready to use with its default functionalities.

```java
package workspace.examples;

import processing.core.PApplet;
import workspace.Workspace;

public class WS_MinimalSetup extends PApplet {

	public static void main(String[] args) {
		PApplet.main(WS_MinimalSetup.class.getName());
	}

	private Workspace workspace;

	@Override
	public void settings() {
		size(1000, 1000, P3D);
	}

	@Override
	public void setup() {
		workspace = new Workspace(this);
	}

	@Override
	public void draw() {

	}

}
```

### Key Features

**Camera Control**

* **Zoom:** Scroll the mouse wheel.
* **Rotate:** Drag with the middle mouse button.
* **Pan:** Drag with the middle mouse button while holding Shift.
* **First-Person Navigation:** Use WASD keys.

**Scene Manipulation**
  
* **Reset Camera:** Press C.
* **Toggle UI:** Press Y.
* **Toggle Grid:** Press G.
* **Show/Hide Normals:** Press N for face normals, V for vertex normals.
* **Wireframe/Solid View:** Press Z.
* **Show/Hide Axes:** Press 1, 2, or 3.
* **Show/Hide Edges:** Press E.
* **Smooth/Flat Shading:** Press S.
* **First-Person Mode:** Press 4.
* **Toggle Rendering Loop:** Use the UI control.
