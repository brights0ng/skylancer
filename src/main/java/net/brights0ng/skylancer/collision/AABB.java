package net.brights0ng.skylancer.collision;

import net.brights0ng.skylancer.objects.Coordinate;
import org.joml.Vector3f;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class AABB {
    public float minX, minY, minZ, maxX, maxY, maxZ;

    public AABB(){
        this.minX = 0;
        this.minY = 0;
        this.minZ = 0;
        this.maxX = 0;
        this.maxY = 0;
        this.maxZ = 0;
    }

    public AABB(float x1, float y1, float z1, float x2, float y2, float z2){
        this.minX = min(x1, x2);
        this.minY = min(y1, y2);
        this.minZ = min(z1, z2);
        this.maxX = max(x1, x2);
        this.maxY = max(y1, y2);
        this.maxZ = max(z1, z2);
    }

    public AABB(Vector3f v2, Vector3f v1){
        this.minX = min(v1.x, v2.x);
        this.minY = min(v1.y, v2.y);
        this.minZ = min(v1.z, v2.z);
        this.maxX = max(v1.x, v2.x);
        this.maxY = max(v1.y, v2.y);
        this.maxZ = max(v1.z, v2.z);


    }
    public AABB(Coordinate c1, Coordinate c2){
        this.minX = min(c1.x, c2.x);
        this.minY = min(c1.y, c2.y);
        this.minZ = min(c1.z, c2.z);
        this.maxX = max(c1.x, c2.x);
        this.maxY = max(c1.y, c2.y);
        this.maxZ = max(c1.z, c2.z);
    }

    public boolean intersects(AABB otherBoundingBox) {
        // Early exit if there is a non-collision on any axis
        if(otherBoundingBox != null){
            if (maxX < otherBoundingBox.minX || minX > otherBoundingBox.maxX ||
                    maxY < otherBoundingBox.minY || minY > otherBoundingBox.maxY ||
                    maxZ < otherBoundingBox.minZ || minZ > otherBoundingBox.maxZ) {
                return false; // No collision
            }}

        return true; // Collision detected
    }

    public String toString() {
        return "minX: " + minX + ", minY: " + minY + ", minZ: " + minZ + ", maxX: " + maxX + ", maxY: " + maxY + ", maxZ: " + maxZ;
    }

    public float getHyp(){
        return (float) Math.sqrt(Math.pow(maxX-minX, 2) + Math.pow(maxY-minY, 2) + Math.pow(maxZ-minZ, 2));
    }
}
