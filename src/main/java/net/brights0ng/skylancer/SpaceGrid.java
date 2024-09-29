package net.brights0ng.skylancer;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class SpaceGrid {
    private Vec3d origin; // The offset position relative to the world grid
    private Quaternionf rotation; // Orientation of the grid

    public SpaceGrid(Vec3d origin, Quaternionf rotation) {
        this.origin = origin;
        this.rotation = rotation;
    }

    public Vec3d getOrigin() {
        return origin;
    }

    public Quaternionf getRotation() {
        return rotation;
    }

    // Convert coordinates from this grid's space to world space
    public Vector3f toWorldCoordinates(BlockPos localCoordinates) {
        // Convert the Vec3d to JOML Vector3f
        Vector3f localVec = new Vector3f(
                (float) localCoordinates.getX(),
                (float) localCoordinates.getY(),
                (float) localCoordinates.getZ());

        // Apply the quaternion rotation to the vector
        localVec.rotate(rotation);

        return localVec ;
    }



}