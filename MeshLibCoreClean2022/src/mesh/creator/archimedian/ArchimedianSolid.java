package mesh.creator.archimedian;

public enum ArchimedianSolid {

	ICOSIDODECAHEDRON(0, "ICOSIDODECAHEDRON"), 
	TRUNCATED_CUBOCTAHEDRON(1, "TRUNCATED CUBOCTAHEDRON"),
	TRUNCATED_ICOSIDODECAHEDRON(2, "TRUNCATED ICOSIDODECAHEDRON"), 
	CUBOCTAHEDRON(3, "CUBOCTAHEDRON"),
	RHOMBICUBOCTAHEDRON(4, "RHOMBICUBOCTAHEDRON"), 
	SNUB_CUBE(5, "SNUB CUBE"),
	RHOMBISOSIDODECAHEDRON(6, "RHOMBISOSIDODECAHEDRON"), 
	SNUB_DODECAHEDRON(7, "SNUB DODECAHEDRON"),
	TRUNCATED_TETRAHEDRON(8, "TRUNCATED TETRAHEDRON"), 
	TRUNCATED_OCTAHEDRON(9, "TRUNCATED OCTAHEDRON"),
	TRUNCATED_CUBE(10, "TRUNCATED CUBE"), 
	TRUNCATED_ICOSAHEDRON(11, "TRUNCATED ICOSAHEDRON"),
	TRUNCATED_DODECAHEDRON(12, "TRUNCATED DODECAHEDRON");

	private int number;
	private String name;

	private ArchimedianSolid(int number, String name) {
		this.number = number;
		this.name = name;
	}

	public int getNumber() {
		return number;
	}

	public String getName() {
		return name;
	}

	public static ArchimedianSolid getType(int number) {
		switch (number) {
		case 0:
			return ICOSIDODECAHEDRON;
		case 1:
			return TRUNCATED_CUBOCTAHEDRON;
		case 2:
			return TRUNCATED_ICOSIDODECAHEDRON;
		case 3:
			return CUBOCTAHEDRON;
		case 4:
			return RHOMBICUBOCTAHEDRON;
		case 5:
			return SNUB_CUBE;
		case 6:
			return RHOMBISOSIDODECAHEDRON;
		case 7:
			return SNUB_DODECAHEDRON;
		case 8:
			return TRUNCATED_TETRAHEDRON;
		case 9:
			return TRUNCATED_OCTAHEDRON;
		case 10:
			return TRUNCATED_CUBE;
		case 11:
			return TRUNCATED_ICOSAHEDRON;
		case 12:
			return TRUNCATED_DODECAHEDRON;
		default:
			return null;
		}
	}

}
