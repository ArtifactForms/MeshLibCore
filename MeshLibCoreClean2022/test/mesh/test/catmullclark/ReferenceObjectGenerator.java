package mesh.test.catmullclark;

import java.io.File;

import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;
import mesh.io.SimpleObjectWriter;
import mesh.modifier.subdivision.CatmullClarkModifier;

public class ReferenceObjectGenerator {

	public static void main(String[] args) throws Exception {
		for (int i = 0; i < 9; i++) {
			String str = "./Catmull_Clark_Reference_Level_#.obj";
			str = str.replace("#", i + "");
			File file = new File(str);
			Mesh3D cube = new CubeCreator().create();
			new CatmullClarkModifier(i).modify(cube);
			new SimpleObjectWriter(file).write(cube, "Catmull_Clark_Cube");
			System.out.println("Finished writing .obj file.");
		}
	}

}
