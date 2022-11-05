package mesh.selection;

public class Compare {
	
	public static boolean compare(CompareType compare, float a, float b) {
		switch (compare) {
		case EQUALS:
			return a == b;
		case LESS:
			return a < b;
		case GREATER:
			return a > b;
		case LESS_OR_EQUALS:
			return a <= b;
		case GREATER_OR_EQUALS:
			return a >= b;
		case NOT_EQUALS:
			return a != b;
		default:
			throw new IllegalArgumentException(compare.toString());
		}
	}
	
	public static boolean compare(CompareType compare, int a, int b) {
		switch (compare) {
		case EQUALS:
			return a == b;
		case LESS:
			return a < b;
		case GREATER:
			return a > b;
		case LESS_OR_EQUALS:
			return a <= b;
		case GREATER_OR_EQUALS:
			return a >= b;
		case NOT_EQUALS:
			return a != b;
		default:
			throw new IllegalArgumentException(compare.toString());
		}
	}
	
	public static boolean compare(CompareType compare, double a, double b) {
		switch (compare) {
		case EQUALS:
			return a == b;
		case LESS:
			return a < b;
		case GREATER:
			return a > b;
		case LESS_OR_EQUALS:
			return a <= b;
		case GREATER_OR_EQUALS:
			return a >= b;
		case NOT_EQUALS:
			return a != b;
		default:
			throw new IllegalArgumentException(compare.toString());
		}
	}

}
