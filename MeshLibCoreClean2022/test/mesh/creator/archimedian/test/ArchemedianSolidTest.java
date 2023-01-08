package mesh.creator.archimedian.test;

import java.util.HashSet;

import org.junit.Assert;
import org.junit.Test;

import mesh.creator.archimedian.ArchimedianSolid;

public class ArchemedianSolidTest {
	
	@Test
	public void getNames() {
		for (ArchimedianSolid solid : ArchimedianSolid.values()) {
			String expected = solid.name().replace("_", " ");
			String actual = solid.getName();
			Assert.assertEquals(expected, actual);
		}
	}
	
	@Test
	public void getTypeAtIndexMinusOneReturnsNull() {
		ArchimedianSolid solid = ArchimedianSolid.getType(-1);
		Assert.assertNull(solid);
	}
	
	@Test
	public void getTypeAtNegativeIndexReturnsNull() {
		int randomNegativeIndex = (int) -(Math.random() * 5000); 
		ArchimedianSolid solid = ArchimedianSolid.getType(randomNegativeIndex);
		Assert.assertNull(solid);
	}
	
	@Test
	public void getTypeAtIndexThirteenReturnsNull() {
		ArchimedianSolid solid = ArchimedianSolid.getType(13);
		Assert.assertNull(solid);
	}
	
	@Test
	public void getTypeAtIndexGreaterThanTwelveReturnsNull() {
		int randomIndex = (int) (Math.random() * 5000) + 12; 
		ArchimedianSolid solid = ArchimedianSolid.getType(randomIndex);
		Assert.assertNull(solid);
	}
	
	@Test
	public void getTypeBetweenZeroAndTelveReturnsThirteenDifferentValues() {
		HashSet<ArchimedianSolid> solidsSet = new HashSet<>();
		for (int i = 0; i < 13; i++)
			solidsSet.add(ArchimedianSolid.getType(i));
		Assert.assertEquals(13, solidsSet.size());
	}
	
	@Test
	public void hasThirteenValues() {
		Assert.assertEquals(13, ArchimedianSolid.values().length);
	}
	
	@Test
	public void getNumberReturnsIndex() {
		for (int i = 0; i < 13; i++)
			Assert.assertEquals(i, ArchimedianSolid.getType(i).getNumber());
	}
	
	@Test
	public void getTypeAtIndexZero() {
		ArchimedianSolid expected = ArchimedianSolid.ICOSIDODECAHEDRON;
		Assert.assertEquals(expected, ArchimedianSolid.getType(0));
	}
	
	@Test
	public void getTypeAtIndexOne() {
		ArchimedianSolid expected = ArchimedianSolid.TRUNCATED_CUBOCTAHEDRON;
		Assert.assertEquals(expected, ArchimedianSolid.getType(1));
	}
	
	@Test
	public void getTypeAtIndexTwo() {
		ArchimedianSolid expected = ArchimedianSolid.TRUNCATED_ICOSIDODECAHEDRON;
		Assert.assertEquals(expected, ArchimedianSolid.getType(2));
	}
	
	@Test
	public void getTypeAtIndexThree() {
		ArchimedianSolid expected = ArchimedianSolid.CUBOCTAHEDRON;
		Assert.assertEquals(expected, ArchimedianSolid.getType(3));
	}
	
	@Test
	public void getTypeAtIndexFour() {
		ArchimedianSolid expected = ArchimedianSolid.RHOMBICUBOCTAHEDRON;
		Assert.assertEquals(expected, ArchimedianSolid.getType(4));
	}
	
	@Test
	public void getTypeAtIndexFive() {
		ArchimedianSolid expected = ArchimedianSolid.SNUB_CUBE;
		Assert.assertEquals(expected, ArchimedianSolid.getType(5));
	}
	
	@Test
	public void getTypeAtIndexSix() {
		ArchimedianSolid expected = ArchimedianSolid.RHOMBISOSIDODECAHEDRON;
		Assert.assertEquals(expected, ArchimedianSolid.getType(6));
	}
	
	@Test
	public void getTypeAtIndexSeven() {
		ArchimedianSolid expected = ArchimedianSolid.SNUB_DODECAHEDRON;
		Assert.assertEquals(expected, ArchimedianSolid.getType(7));
	}
	
	@Test
	public void getTypeAtIndexEight() {
		ArchimedianSolid expected = ArchimedianSolid.TRUNCATED_TETRAHEDRON;
		Assert.assertEquals(expected, ArchimedianSolid.getType(8));
	}
	
	@Test
	public void getTypeAtIndexNine() {
		ArchimedianSolid expected = ArchimedianSolid.TRUNCATED_OCTAHEDRON;
		Assert.assertEquals(expected, ArchimedianSolid.getType(9));
	}
	
	@Test
	public void getTypeAtIndexTen() {
		ArchimedianSolid expected = ArchimedianSolid.TRUNCATED_CUBE;
		Assert.assertEquals(expected, ArchimedianSolid.getType(10));
	}
	
	@Test
	public void getTypeAtIndexEleven() {
		ArchimedianSolid expected = ArchimedianSolid.TRUNCATED_ICOSAHEDRON;
		Assert.assertEquals(expected, ArchimedianSolid.getType(11));
	}
	
	@Test
	public void getTypeAtIndexTwelve() {
		ArchimedianSolid expected = ArchimedianSolid.TRUNCATED_DODECAHEDRON;
		Assert.assertEquals(expected, ArchimedianSolid.getType(12));
	}
		
}
