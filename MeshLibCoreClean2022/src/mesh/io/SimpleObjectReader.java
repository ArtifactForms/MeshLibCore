package mesh.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;

/**
 * 
 * @author - Simon
 * @version 0.2, 21 June 2016
 */
public class SimpleObjectReader {

	private Mesh3D mesh;

	protected void addVertex(String[] sArray) {
		float x = Float.parseFloat(sArray[1]);
		float y = Float.parseFloat(sArray[2]);
		float z = Float.parseFloat(sArray[3]);
		mesh.add(new Vector3f(x, -y, z));
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

	public Mesh3D read(File file) {
		mesh = new Mesh3D();

		try {
			BufferedReader in = new BufferedReader(new FileReader(file));

			String line = null;
			while ((line = in.readLine()) != null) {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		for (Face3D f : mesh.faces) {
//			System.out.println("mesh.add(new Face3D("
//					+ Arrays.toString(f.indices).replace("[", "")
//							.replace("]", "") + "));");
//		}

		return mesh;
	}

}
