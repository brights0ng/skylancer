package net.brights0ng.skylancer.collision;

import net.brights0ng.skylancer.objects.LocalGrid;
import net.brights0ng.skylancer.Skylancer;
import net.brights0ng.skylancer.objects.PhysicsObject;
import org.joml.Vector3f;

public class CollisionPhysics {
    public static int collisionSteps = 4;

    public CollisionPhysics() {
        for (LocalGrid grid : Skylancer.localGridMap.values()) {
            grid.closeNeighbors.clear();
            if (!grid.gridObjectsMap.isEmpty()) {
                for (LocalGrid neighbor : grid.neighbors) {
                    neighborDistanceCheck(grid, neighbor);
                }
                if (!grid.closeNeighbors.isEmpty()) {
                    simpleCollisionCheck(grid);
                }
            }
        }
    }

    public static void neighborDistanceCheck(LocalGrid grid, LocalGrid neighbor) {
        Vector3f direction = new Vector3f(neighbor.gridPhysics.origin).sub(grid.gridPhysics.origin).normalize();

        float distance = grid.gridPhysics.origin.distance(neighbor.gridPhysics.origin);
        float hyp = grid.gridPhysics.gridBoundingBox.getHyp() + neighbor.gridPhysics.gridBoundingBox.getHyp();

        float projectionG;
        float  projectionN;
        if (grid.gridPhysics.linearVelocity.length() != 0){
             projectionG = grid.gridPhysics.linearVelocity.dot(direction);
        } else {
             projectionG = 0;
        }
        if (neighbor.gridPhysics.linearVelocity.length() != 0){
            projectionN = neighbor.gridPhysics.linearVelocity.dot(direction);
        } else {
            projectionN = 0;
        }

        if (distance < (hyp + Math.abs(projectionG) + Math.abs(projectionN))) {
            grid.closeNeighbors.add(neighbor);
        }
    }

    public static void simpleCollisionCheck(LocalGrid grid) {
        for (LocalGrid neighbor : grid.closeNeighbors) {
            if (grid.gridPhysics.gridBoundingBox.intersects(neighbor.gridPhysics.gridBoundingBox)) {
                handleCollision(grid, neighbor);
            }
        }
    }

    public static void advancedCollisionCheck(LocalGrid grid) {
        final float stepSize = 1f / collisionSteps;
        Vector3f currentPosition = new Vector3f(grid.gridPhysics.origin); // Create a new Vector3f to store currentPosition

        AABB[] stepBoundingBoxes = grid.gridPhysics.stepBoundingBoxes;
        stepBoundingBoxes[0] = grid.gridPhysics.gridBoundingBox; // Ensure the first bounding box is set

        for (int i = 0; i <= collisionSteps; i++) {
            // Update currentPosition based on the linear velocity
            currentPosition.add(grid.gridPhysics.linearVelocity.mul(stepSize));

            // Calculate the bounding box based on the current position
            AABB stepBoundingBox = calculateBoundingBox(currentPosition, stepBoundingBoxes[0]);
            stepBoundingBoxes[i] = stepBoundingBox; // Store the current step bounding box

            // Check for collisions with neighboring grids
            for (LocalGrid otherGrid : grid.closeNeighbors) {
                if (otherGrid != null) { // Ensure we're not checking against the same grid
                    AABB[] otherStepBoundingBoxes = otherGrid.gridPhysics.stepBoundingBoxes;

                    for (int j = 0; j <= collisionSteps; j++) {
                        if (otherStepBoundingBoxes[j] != null && stepBoundingBoxes[i] != null) {
                            // Check for intersection
                            if (stepBoundingBoxes[i].intersects(otherStepBoundingBoxes[j])) {
                                handleCollision(grid, otherGrid); // Implement your collision response
                                System.out.println("Collision detected between grids!");
                            }
                        }
                    }
                }
            }
        }
        grid.gridPhysics.stepBoundingBoxes = stepBoundingBoxes; // Update the grid's stepBoundingBoxes
    }


    private static AABB calculateBoundingBox(Vector3f position, AABB originalBoundingBox) {
        // Create a new AABB based on the new position and original dimensions
        return new AABB(
                position.x + originalBoundingBox.minX,
                position.y + originalBoundingBox.minY,
                position.z + originalBoundingBox.minZ,
                position.x + originalBoundingBox.maxX,
                position.y + originalBoundingBox.maxY,
                position.z + originalBoundingBox.maxZ
        );
    }

    private static void handleCollision(LocalGrid grid, LocalGrid otherGrid) {
        // Implement collision response logic, e.g., adjust positions, apply forces, etc.
        OBB[] gridOBBs = new OBB[grid.gridObjectsMap.size()];
        OBB[] otherGridOBBs = new OBB[otherGrid.gridObjectsMap.size()];
        int i = 0;
        int j = 0;
        for (PhysicsObject object : grid.gridObjectsMap.values()){
            object.updateBoundingBox();
            gridOBBs[i] = object.getBoundingBox();
            i++;
        }
        for (PhysicsObject object : otherGrid.gridObjectsMap.values()){
            object.updateBoundingBox();
            otherGridOBBs[j] = object.getBoundingBox();
            j++;
        }
        for (OBB gridOBB : gridOBBs){
            for (OBB otherGridOBB : otherGridOBBs){
                if (gridOBB.intersects(otherGridOBB)){
                    System.out.println("COLLISION!!!!!");
                }
            }
        }
    }
}
