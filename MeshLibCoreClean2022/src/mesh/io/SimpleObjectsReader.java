package mesh.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;

public class SimpleObjectsReader {
	
	private List<Vector3f> vertices;
	private Mesh3D mesh;
	private List<Mesh3D> meshes;

	protected void addVertex(String[] sArray) {
		float x = Float.parseFloat(sArray[1]);
		float y = Float.parseFloat(sArray[2]);
		float z = Float.parseFloat(sArray[3]);
		vertices.add(new Vector3f(x, y, z));
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

	public List<Mesh3D> read(File file) {
		meshes = new ArrayList<Mesh3D>();
		vertices = new ArrayList<Vector3f>();
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));

			String line = null;
			while ((line = in.readLine()) != null) {
				if (line.startsWith("o ")) {
					mesh = new Mesh3D();
					meshes.add(mesh);
				}
				if (line.startsWith("v ")) {
					String[] sArray = line.split(" ");
					addVertex(sArray);
				}
				if (line.startsWith("f ")) {
					String[] sArray = line.split(" ");
					addFace(sArray);
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (Mesh3D mesh : meshes) {
			for (Vector3f v : vertices) {
				mesh.add(new Vector3f(v));
			}
		}

		return meshes;
	}

}
