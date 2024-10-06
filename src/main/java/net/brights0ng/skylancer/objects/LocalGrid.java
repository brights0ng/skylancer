package net.brights0ng.skylancer.objects;

import net.brights0ng.skylancer.collision.AABB;
import net.brights0ng.skylancer.Skylancer;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.*;

import static net.brights0ng.skylancer.collision.CollisionPhysics.collisionSteps;

public class LocalGrid {

    public Map<UUID, PhysicsObject> gridObjectsMap = new HashMap<>();
    public GridPhysics gridPhysics;
    public UUID gridID;
    public Coordinate coordinate;
    public Coordinate preciseCoordinate;
    public Set<LocalGrid> neighbors = new HashSet<>();
    public Set<LocalGrid> closeNeighbors = new HashSet<>();


    public LocalGrid(Vector3f origin, Vector3f rotation) {
        gridID  = UUID.randomUUID();
        System.out.println("Creating grid " + gridID);
        this.gridPhysics = new GridPhysics(origin,this);
        this.gridPhysics.rotation = rotation;
        Skylancer.localGridMap.put(gridID, this);
        this.gridID = gridID;
        this.coordinate = new Coordinate(origin);
    }

    public Coordinate getOrigin() {
        return gridPhysics.renderedOrigin;
    }

    public Vector3f getRotation() {
        return gridPhysics.renderedRotation;
    }

    public void createObject(BlockPos blockPos, Block block) {
        try {
            PhysicsObject object = new PhysicsObject( this, blockPos, block);
            System.out.println("PhysicsObject created: " + object);
        } catch (Exception e) {
            System.err.println("Error creating object: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public PhysicsObject getObject(String name) {
        return gridObjectsMap.get(name);
    }

    // Convert coordinates from this grid's space to world space
    public static Vector3f toWorldCoordinates(Coordinate coord, LocalGrid grid) {
        // Convert the Coordinate to JOML Vector3f
        Vector3f localOrigin = new Vector3f(
                (float) coord.x,
                (float) coord.y,
                (float) coord.z);

        // Get the grid's origin in world space
        Vector3f gridOrigin = grid.getOrigin().toVector3f();

        // Translate localOrigin to the origin of the grid
        // First, we move localOrigin to the grid's origin for the rotation

        // Define the rotations
        float yaw = (float) Math.toRadians(grid.gridPhysics.rotation.y);
        float pitch = (float) Math.toRadians(grid.gridPhysics.rotation.x);
        float roll = (float) Math.toRadians(grid.gridPhysics.rotation.z);

        // Create a quaternion for the rotations
        Quaternionf rotation = new Quaternionf();
        rotation.rotateYXZ(yaw, pitch, roll); // Set the rotation based on yaw, pitch, and roll

        // Apply the quaternion rotation to the vector
        localOrigin.rotate(rotation);

        // Translate back to the grid's origin
        localOrigin.add(gridOrigin);

        return localOrigin;
    }

    public Vector3f getRenderedOrigin() {
        return gridPhysics.renderedOrigin.toVector3f();
    }


    public static class GridPhysics {
        public LocalGrid grid;
        public Coordinate renderedOrigin;
        public Vector3f origin;
        public Vector3f linearVelocity;
        public Vector3f linearAcceleration;
        public Vector3f rotation;
        public Vector3f renderedRotation;
        public AngularVector angularVelocity;
        public AngularVector angularAcceleration;
        public AABB gridBoundingBox = new AABB();
        public AABB[] stepBoundingBoxes = new AABB[collisionSteps+1];

        public GridPhysics(Vector3f currentPos, LocalGrid grid) {
            this.grid = grid;
            this.renderedOrigin = new Coordinate(currentPos);
            this.origin = currentPos;
            this.linearVelocity = new Vector3f(0, 0, 0);
            this.linearAcceleration = new Vector3f(0, 0, 0);
            this.renderedRotation = new Vector3f(0, 0, 0);
            this.rotation = new Vector3f(0, 0, 0);
            this.angularVelocity = new AngularVector(0, 0, 0);
            this.angularAcceleration = new AngularVector(0, 0, 0);
        }

        public void setLinearAcceleration(Vector3f linearAcceleration) {
            this.linearAcceleration = linearAcceleration;
        }

        public void setAngularAcceleration(AngularVector angularAcceleration) {
            this.angularAcceleration = angularAcceleration;
        }

        public void tickKinematics(){
            this.linearVelocity = new Vector3f(linearVelocity.add(linearAcceleration));
            this.origin = new Vector3f(origin.add(linearVelocity));
            this.renderedOrigin = new Coordinate(origin);

            this.angularVelocity.pitch += angularAcceleration.pitch;
            this.angularVelocity.yaw += angularAcceleration.yaw;
            this.angularVelocity.roll += angularAcceleration.roll;

            this.rotation = this.rotation.add(new Vector3f((angularVelocity.yaw),
                    (float) Math.toRadians(angularVelocity.pitch),
                    (float) Math.toRadians(angularVelocity.roll)));
            this.renderedRotation = rotation;

        }

        public void frameKinematics(float deltaTime){
            this.renderedOrigin.x = renderedOrigin.x + linearVelocity.x * deltaTime;
            this.renderedOrigin.y = renderedOrigin.y + linearVelocity.y * deltaTime;
            this.renderedOrigin.z = renderedOrigin.z + linearVelocity.z * deltaTime;

            this.renderedRotation = this.renderedRotation.add(new Vector3f((angularVelocity.yaw * deltaTime),
                    (float) Math.toRadians(angularVelocity.pitch * deltaTime),
                    (float) Math.toRadians(angularVelocity.roll * deltaTime)));
        }

        public void tickGridBoundingBox() {
            if (!grid.gridObjectsMap.isEmpty()) {
                float minX = Float.MAX_VALUE, maxX = Float.NEGATIVE_INFINITY;
                float minY = Float.MAX_VALUE, maxY = Float.NEGATIVE_INFINITY;
                float minZ = Float.MAX_VALUE, maxZ = Float.NEGATIVE_INFINITY;

                // Reuse this array for the corners of the block
                Coordinate[] corners = new Coordinate[8];

                for (PhysicsObject object : grid.gridObjectsMap.values()) {
                    // Initialize corners
                    corners[0] = new Coordinate(object.getBlockPos().getX()-.5f, object.getBlockPos().getY()-.5f, object.getBlockPos().getZ()-.5f);
                    corners[1] = new Coordinate(object.getBlockPos().getX()-.5f, object.getBlockPos().getY()-.5f, object.getBlockPos().getZ() + 1-.5f);
                    corners[2] = new Coordinate(object.getBlockPos().getX()-.5f, object.getBlockPos().getY() + 1-.5f, object.getBlockPos().getZ() + 1-.5f);
                    corners[3] = new Coordinate(object.getBlockPos().getX()-.5f, object.getBlockPos().getY() + 1-.5f, object.getBlockPos().getZ()-.5f);
                    corners[4] = new Coordinate(object.getBlockPos().getX() + 1-.5f, object.getBlockPos().getY()-.5f, object.getBlockPos().getZ()-.5f);
                    corners[5] = new Coordinate(object.getBlockPos().getX() + 1-.5f, object.getBlockPos().getY()-.5f, object.getBlockPos().getZ() + 1-.5f);
                    corners[6] = new Coordinate(object.getBlockPos().getX() + 1-.5f, object.getBlockPos().getY() + 1-.5f, object.getBlockPos().getZ() + 1-.5f);
                    corners[7] = new Coordinate(object.getBlockPos().getX() + 1-.5f, object.getBlockPos().getY() + 1-.5f, object.getBlockPos().getZ()-.5f);

                    for (Coordinate v : corners) {
                        Vector3f worldCoords = toWorldCoordinates(v, grid);
                        float x = worldCoords.x;
                        float y = worldCoords.y;
                        float z = worldCoords.z;

                        // Update min and max values based on block coordinates
                        minX = Math.min(minX, x);
                        maxX = Math.max(maxX, x);
                        minY = Math.min(minY, y);
                        maxY = Math.max(maxY, y);
                        minZ = Math.min(minZ, z);
                        maxZ = Math.max(maxZ, z);
                    }
                }

                // Check if valid values were found and create the AABB
                if (minX < Float.MAX_VALUE && minY < Float.MAX_VALUE && minZ < Float.MAX_VALUE) {
                    gridBoundingBox = new AABB(minX, minY, minZ, maxX, maxY, maxZ);
                }
            }
        }



        public void tickBlockPhysics() {
            this.tickKinematics();
            this.tickGridBoundingBox();
        }

        public void frameBlockPhysics(float deltaTime) {
            this.frameKinematics(deltaTime);
        }


    }
}