package net.brights0ng.skylancer.utils;

import net.brights0ng.skylancer.objects.Coordinate;
import org.joml.Vector3f;

public class MathUtils {

    public static float lerp(float start, float end, float t) {
        return start + (end - start) * t;
    }

    public static Vector3f[] vertexes(Vector3f center) {
        Vector3f[] coords = new Vector3f[8];
        float halfSize = 0.5f; // Assuming half-extents of 0.5f in all directions

        coords[0] = new Vector3f(center.x - halfSize, center.y - halfSize, center.z - halfSize); // Min corner
        coords[1] = new Vector3f(center.x - halfSize, center.y - halfSize, center.z + halfSize);
        coords[2] = new Vector3f(center.x - halfSize, center.y + halfSize, center.z + halfSize);
        coords[3] = new Vector3f(center.x - halfSize, center.y + halfSize, center.z - halfSize);
        coords[4] = new Vector3f(center.x + halfSize, center.y - halfSize, center.z - halfSize);
        coords[5] = new Vector3f(center.x + halfSize, center.y - halfSize, center.z + halfSize);
        coords[6] = new Vector3f(center.x + halfSize, center.y + halfSize, center.z + halfSize);
        coords[7] = new Vector3f(center.x + halfSize, center.y + halfSize, center.z - halfSize);

        return coords;
    }
}
