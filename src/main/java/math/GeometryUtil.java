package math;

/**
 * A collection of common analytical geometry.
 * 
 * @author Simon
 * @version 0.2, 30 May 2016
 * 
 */
public class GeometryUtil {

    /**
     * Calculates the centroid (center of mass) of a triangle defined by three
     * points.
     *
     * The centroid is computed as the average of the x, y, and z coordinates of
     * the three vertices.
     *
     * @param a The first vertex of the triangle.
     * @param b The second vertex of the triangle.
     * @param c The third vertex of the triangle.
     * @return The centroid of the triangle as a new Vector3f.
     */
    public static Vector3f calculateCentroid(Vector3f a, Vector3f b,
            Vector3f c) {
        float x = (a.x + b.x + c.x) / 3f;
        float y = (a.y + b.y + c.y) / 3f;
        float z = (a.z + b.z + c.z) / 3f;
        return new Vector3f(x, y, z);
    }

    // public static Vector2f calculateIntersection(Ray2D ray, Bounds2D bounds)
    // {
    // return null;
    // }

    // public static Vector2f calculateIntersection(Ray2D ray, Bounds2D bounds)
    // {
    // float a = bounds.getWidth();
    // float b = bounds.getHeight();
    // float x0 = ray.origin.x;
    // float y0 = ray.origin.y;
    // float rx = bounds.getCenterX();
    // float ry = bounds.getCenterY();
    // float vx = ray.direction.x;
    // float vy = ray.direction.y;
    // float t = (a * (x0 - rx) + b * (y0 - ry)) / (a * vx + b * vy);
    //
    // return null;
    // }

    /**
     * Calculates the center of the polygon specified by the four points a, b, c
     * and d.
     * 
     * @param a the first point of the polygon
     * @param b the second point of the polygon
     * @param c the third point of the polygon
     * @param d the fourth point of the polygon
     * @return the center point of the polygon as {@link Vector2f}
     */
    public static Vector2f calculateCenter(Vector2f a, Vector2f b, Vector2f c,
            Vector2f d) {
        // Vector2f center = a.add(b).add(c).add(d).mult(0.25f);
        // return center;
        // Implemented to avoid to many object creations
        float x = (a.x + b.x + c.x + d.x) * 0.25f;
        float y = (a.y + b.y + c.y + d.y) * 0.25f;
        return new Vector2f(x, y);
    }

    /**
     * Calculates the center of the polygon specified by the four points a, b, c
     * and d.
     * 
     * @param a the first point of the polygon
     * @param b the second point of the polygon
     * @param c the third point of the polygon
     * @param d the fourth point of the polygon
     * @return the center point of the polygon as {@link Vector3f}
     */
    public static Vector3f calculateCenter(Vector3f a, Vector3f b, Vector3f c,
            Vector3f d) {
        // Vector2f center = a.add(b).add(c).add(d).mult(0.25f);
        // return center;
        // Implemented to avoid to many object creations
        float x = (a.x + b.x + c.x + d.x) * 0.25f;
        float y = (a.y + b.y + c.y + d.y) * 0.25f;
        float z = (a.z + b.z + c.z + d.z) * 0.25f;
        return new Vector3f(x, y, z);
    }

    /**
     * Calculates the point on a circle.
     * 
     * @param center the center of the circle
     * @param radius the radius of the circle
     * @param angle  the angle of the point to return in radians
     * @param cw     true to rotate clockwise, false to rotate counterclockwise
     *               (ccw)
     * @return the newly created point
     */
    public static Vector2f pointOnCircle(Vector2f center, float radius,
            float angle, boolean cw) {
        angle = cw ? angle : -angle;
        float x = (float) (center.x + radius * Math.cos(angle));
        float y = (float) (center.y + radius * Math.sin(angle));
        return new Vector2f(x, y);
    }

    /**
     * Calculates the distribution point of a line segment.
     * 
     * @param start  the start point of the line segment
     * @param end    the end point of the line segment
     * @param lambda the part ratio
     * @return the the distribution point
     */
    public Vector2f getDistributionPoint(Vector2f start, Vector2f end,
            float lambda) {
        float scalar = 1f / (1f + lambda);
        return start.add(end.mult(lambda)).mult(scalar);
    }

    /**
     * Calculates the main emphasis of a triangle.
     * 
     * @param a the first point of the triangle
     * @param b the second point of the triangle
     * @param c the third point of the triangle
     * @return the main emphasis of the triangle described by a,b,c
     */
    public static Vector2f getMainEmphasis(Vector2f a, Vector2f b, Vector2f c) {
        Vector2f m = a.add(b).add(c).mult(Mathf.ONE_THIRD);
        return m;
    }

    /**
     * Returns the midpoint of the line segment.
     * 
     * @param start the start point of the line segment
     * @param end   the end point of the line segment
     * @return the midpoint of the line segment
     */
    public static Vector2f getMidpoint(Vector2f start, Vector2f end) {
        // M = 1/2 * (A + B)
        Vector2f m = start.add(end).mult(0.5f);
        return m;
    }

    /**
     * Returns the midpoint of the line segment.
     * 
     * @param start the start point of the line segment
     * @param end   the end point of the line segment
     * @return the midpoint of the line segment
     */
    public static Vector3f getMidpoint(Vector3f start, Vector3f end) {
        // M = 1/2 * (A + B)
        Vector3f m = start.add(end).mult(0.5f);
        return m;
    }

    public static double angleBetweenVectors(Vector3f v1, Vector3f v2) {
        double dotProduct = v1.dot(v2);
        double magnitude1 = v1.length();
        double magnitude2 = v2.length();

        if (magnitude1 == 0 || magnitude2 == 0) {
            throw new IllegalArgumentException(
                    "Vectors cannot have zero length");
        }

        double cosTheta = dotProduct / (magnitude1 * magnitude2);
        double angle = Math.acos(cosTheta);

        return angle;
    }

}
