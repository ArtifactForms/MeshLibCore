package common.world.structure;

public class StructureRotation {

    public static int[] rotate(int x, int z, int rotation) {

        return switch(rotation) {

            case 1 -> new int[]{ -z, x };   // 90°
            case 2 -> new int[]{ -x, -z };  // 180°
            case 3 -> new int[]{ z, -x };   // 270°

            default -> new int[]{ x, z };   // 0°
        };
    }
}