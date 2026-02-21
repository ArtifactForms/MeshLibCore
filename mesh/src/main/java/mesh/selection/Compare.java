package mesh.selection;

public class Compare {

    public static <T extends Number> boolean compare(CompareType compare, T a,
            T b) {
        if (compare == null)
            throw new IllegalArgumentException("Compare type cannot be null.");
        switch (compare) {
        case EQUALS:
            return a.doubleValue() == b.doubleValue();
        case LESS:
            return a.doubleValue() < b.doubleValue();
        case GREATER:
            return a.doubleValue() > b.doubleValue();
        case LESS_OR_EQUALS:
            return a.doubleValue() <= b.doubleValue();
        case GREATER_OR_EQUALS:
            return a.doubleValue() >= b.doubleValue();
        case NOT_EQUALS:
            return a.doubleValue() != b.doubleValue();
        default:
            throw new IllegalArgumentException("Invalid compare type.");
        }
    }

}