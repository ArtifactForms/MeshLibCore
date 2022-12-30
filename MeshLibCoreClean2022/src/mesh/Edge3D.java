package mesh;

public class Edge3D {

	public int fromIndex;
	public int toIndex;

	public Edge3D(int fromIndex, int toIndex) {
		this.fromIndex = fromIndex;
		this.toIndex = toIndex;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + fromIndex;
		result = prime * result + toIndex;
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
		Edge3D other = (Edge3D) obj;
		if (fromIndex != other.fromIndex)
			return false;
		if (toIndex != other.toIndex)
			return false;
		return true;
	}

	public Edge3D createPair() {
		return new Edge3D(toIndex, fromIndex);
	}

	@Override
	public String toString() {
		return "Edge3D [fromIndex=" + fromIndex + ", toIndex=" + toIndex + "]";
	}

}
