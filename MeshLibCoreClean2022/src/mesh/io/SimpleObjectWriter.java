package mesh.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;

/**
 * 
 * @author - Simon
 * @version 0.2, 21 June 2016
 */
public class SimpleObjectWriter {
	
	public static void write(Mesh3D mesh, String name) {		
		StringBuilder s = new StringBuilder();
		s.append("o ");
		s.append(name);
		s.append("\n");
		
		// vertices
		for (Vector3f v : mesh.vertices) {
			s.append("v ");
			s.append(v.x);
			s.append(" ");
			s.append(v.y);
			s.append(" ");
			s.append(v.z);
			s.append("\n");
		}
		
		// faces
		for (Face3D f : mesh.faces) {
			s.append("f ");
			for (int i = 0; i < f.indices.length; i++) {
				s.append(f.indices[i] + 1);
				s.append(" ");
			}
			s.append("\n");
		}
		
		try {
			File file = new File("./MyShape.obj");
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			out.write(s.toString());
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Finished writing .obj file.");
	}
	
	public static void writeMesh(Mesh3D mesh) {
		write(mesh, "Object");
	}

}
