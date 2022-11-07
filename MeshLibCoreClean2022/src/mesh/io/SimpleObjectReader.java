package mesh.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;

public class SimpleObjectReader {

	private Mesh3D mesh;
	
	public Mesh3D read(File file) {
		initializeMesh();

		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = in.readLine()) != null) {
				processLine(line);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mesh;
	}
	
	private void processLine(String line) {
		if (line.startsWith("v ")) {
			String[] sArray = line.split(" ");
			addVertex(sArray);
		}
		
		if (line.startsWith("f ")) {
			String[] sArray = line.split(" ");
			addFace(sArray);
		}
	}
	
	private void initializeMesh() {
		mesh = new Mesh3D();
	}

	protected void addVertex(String[] sArray) {
		float x = Float.parseFloat(sArray[1]);
		float y = Float.parseFloat(sArray[2]);
		float z = Float.parseFloat(sArray[3]);
		mesh.add(new Vector3f(x, y, z));
	}

	protected void addFace(String[] sArray) {
		Face3D f = null;
		int[] iArray = new int[sArray.length - 1];
		for (int i = 0; i < iArray.length; i++) {
			iArray[i] = Integer.parseInt(sArray[i + 1]) - 1;
		}
		f = new Face3D(iArray);
		mesh.add(f);
	}
	
}
