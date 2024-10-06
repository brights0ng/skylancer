package net.brights0ng.skylancer.collision;

import net.brights0ng.skylancer.objects.Coordinate;
import net.brights0ng.skylancer.objects.LocalGrid;
import net.brights0ng.skylancer.objects.PhysicsObject;
import net.brights0ng.skylancer.utils.MathUtils;
import net.minecraft.util.math.BlockPos;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class OBB {
    private Coordinate center; // Center of the OBB
    private Vector3f halfExtents; // Half-lengths in each direction (X, Y, Z)
    private Vector3f[] axes = new Vector3f[3]; // The axes of the OBB for SAT
    private Vector3f[] vertices; // The vertices of the OBB for SAT
    private Vector3f rotationXYZ; // Rotation of the OBB
    private Quaternionf rotation;
    private Matrix3f rotationMatrix = new Matrix3f();
    private PhysicsObject object;

    public OBB(LocalGrid grid, PhysicsObject obj) {
        this.center = new Coordinate(LocalGrid.toWorldCoordinates(new Coordinate(obj.getBlockPos().getX(), obj.getBlockPos().getY(), obj.getBlockPos().getZ()), grid));
        this.halfExtents = new Vector3f(.5f, .5f, .5f);
        this.rotationXYZ = grid.gridPhysics.rotation;
        this.object = obj;

        // Calculate the rotation matrix from the quaternion
        rotationMatrix.rotationXYZ(rotationXYZ.x, rotationXYZ.y, rotationXYZ.z);
        rotation = new Quaternionf();
        rotation.rotateYXZ(rotationXYZ.y, rotationXYZ.x, rotationXYZ.z);

        // Set the axes based on the rotation matrix
        axes[0] = new Vector3f(1, 0, 0).rotate(rotation); // X-axis
        axes[1] = new Vector3f(0, 1, 0).rotate(rotation); // Y-axis
        axes[2] = new Vector3f(0, 0, 1).rotate(rotation); // Z-axis

        // Calculate vertices
        this.vertices = generateVertices(); // Ensure vertices are calculated here
    }

    public Coordinate getCenter() {
        return center;
    }

    public Vector3f getHalfExtents() {
        return halfExtents;
    }

    // Method to calculate vertices
    public Vector3f[] generateVertices() {
        BlockPos pos = object.getBlockPos();
        Vector3f[] localVertices = MathUtils.vertexes(new Vector3f(pos.getX(), pos.getY(), pos.getZ()));
        this.vertices = new Vector3f[localVertices.length];
        for (int i = 0; i < localVertices.length; i++) {
            // Apply rotation and translation
//            this.vertices[i] = new Vector3f(localVertices[i]).rotate(rotation).add(center.x, center.y, center.z);
            this.vertices[i] = LocalGrid.toWorldCoordinates(new Coordinate(localVertices[i]), object.getLocalGrid());

        }
        return this.vertices;
    }

    public Vector3f[] getVertices() {
        return this.vertices;
    }

    // Collision detection method
    public boolean intersects(OBB other) {
        Vector3f[] axes = new Vector3f[15]; // 6 from the two OBBs + 9 from cross products
        System.arraycopy(this.axes, 0, axes, 0, 3); // This OBB's axes
        System.arraycopy(other.axes, 0, axes, 3, 3); // Other OBB's axes

        // Add cross product axes
        int index = 6; // Start after the first 6 axes
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Vector3f cross = new Vector3f(this.axes[i]).cross(other.axes[j]);
                axes[index++] = cross; // Store the cross product in the axes array
            }
        }

        // Now check for overlap on each axis
        for (Vector3f axis : axes) {
            float[] thisProjection = projectOntoAxis(this, axis);
            float[] otherProjection = projectOntoAxis(other, axis);

            // Check for overlap
            if (!overlap(thisProjection, otherProjection)) {
                return false; // Separating axis found, no collision
            }
        }

        return true; // No separating axis found, they intersect
    }

    private float[] projectOntoAxis(OBB obb, Vector3f axis) {
        Vector3f[] vertices = obb.generateVertices();
        float min = Float.MAX_VALUE;
        float max = Float.MIN_VALUE;

        for (Vector3f vertex : vertices) {
            float projection = vertex.dot(axis);
            if (projection < min) min = projection;
            if (projection > max) max = projection;
        }

        return new float[]{min, max};
    }

    private boolean overlap(float[] proj1, float[] proj2) {
        return proj1[0] <= proj2[1] && proj2[0] <= proj1[1];
    }
}
