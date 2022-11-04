package mesh;

public class Pair {

	public int a;
	public int b;
	
	public Pair(int a, int b) {
		super();
		this.a = a;
		this.b = b;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + a;
		result = prime * result + b;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pair other = (Pair) obj;
		if (a != other.a)
			return false;
		if (b != other.b)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Pair [a=" + a + ", b=" + b + "]";
	}
	
}
