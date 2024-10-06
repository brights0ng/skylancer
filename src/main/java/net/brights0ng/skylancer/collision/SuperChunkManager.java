package net.brights0ng.skylancer.collision;

import net.brights0ng.skylancer.Skylancer;
import net.brights0ng.skylancer.objects.Coordinate;
import net.brights0ng.skylancer.objects.LocalGrid;
import org.joml.Vector3f;

import java.util.*;

public class SuperChunkManager {
    // Use synchronized map to prevent concurrent modifications
    private static final Map<Vector3f, SuperChunk> superChunks = Collections.synchronizedMap(new HashMap<>());

    public SuperChunkManager() {
        update(); // Call update initially if needed
    }

    public static void update() {
        synchronized (superChunks) {
            System.out.println("Updating superChunks and boundary boxes...; " + superChunks.size() + " superChunks currently exist.");
            List<Coordinate> chunksToRemove = new ArrayList<>();

            for (LocalGrid grid : Skylancer.localGridMap.values()) {
                grid.preciseCoordinate = grid.getOrigin().scale(0.005208333333333333f);
                grid.coordinate = grid.getOrigin().rawToSuper();

                if (!grid.gridObjectsMap.isEmpty()){
                    // Calculate the bounding box
                    AABB gridBoundingBox = grid.gridPhysics.gridBoundingBox;

                    // Determine the chunk coordinates that the bounding box intersects
                    int minChunkX = (int) Coordinate.rawToSuper(new Coordinate(gridBoundingBox.minX, gridBoundingBox.minY, gridBoundingBox.minZ)).x;
                    int maxChunkX = (int) Coordinate.rawToSuper(new Coordinate(gridBoundingBox.maxX, gridBoundingBox.maxY, gridBoundingBox.maxZ)).x;
                    int minChunkY = (int) Coordinate.rawToSuper(new Coordinate(gridBoundingBox.minX, gridBoundingBox.minY, gridBoundingBox.minZ)).y;
                    int maxChunkY = (int) Coordinate.rawToSuper(new Coordinate(gridBoundingBox.maxX, gridBoundingBox.maxY, gridBoundingBox.maxZ)).y;
                    int minChunkZ = (int) Coordinate.rawToSuper(new Coordinate(gridBoundingBox.minX, gridBoundingBox.minY, gridBoundingBox.minZ)).z;
                    int maxChunkZ = (int) Coordinate.rawToSuper(new Coordinate(gridBoundingBox.maxX, gridBoundingBox.maxY, gridBoundingBox.maxZ)).z;

                    // Create superChunks for all intersecting chunks
                    for (int x = minChunkX; x <= maxChunkX; x++) {
                        for (int y = minChunkY; y <= maxChunkY; y++) {
                            for (int z = minChunkZ; z <= maxChunkZ; z++) {
                                if (!(x == 0) && !(y == 0) && !(z == 0)) {
                                    Coordinate coord = new Coordinate(x, y, z);
                                    if (!superChunks.containsKey(coord.toVector3f())) {
                                        superChunks.put(coord.toVector3f(), new SuperChunk(coord));
                                        System.out.println("Adding superChunk: " + coord);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Check existing superChunks for grids
            for (SuperChunk superChunk : superChunks.values()) {
                if (!superChunk.testForGrids())  {
                    chunksToRemove.add(superChunk.getCoordinate()); // Mark for removal
                }
            }

            // Now remove the superChunks outside the iteration
            for (Coordinate coord : chunksToRemove) {
                superChunks.remove(coord.toVector3f());
                System.out.println("Removing superChunk: " + coord);
            }
        }
        for (LocalGrid grid  : Skylancer.localGridMap.values()){
            if (!grid.gridObjectsMap.isEmpty()){
            AABB gridBoundingBox = grid.gridPhysics.gridBoundingBox; // Get the grid's bounding box
                // Check current superChunk and adjacent superChunks
                for (SuperChunk superChunk : superChunks.values()) {
                    if (superChunk.intersects(gridBoundingBox)) {
                        // Perform collision detection between grids in this superChunk
                        for (LocalGrid otherGrid : superChunk.getOtherGrids(grid)) {
                            grid.neighbors.add(otherGrid);
                            System.out.println("Adding neighbor: " + otherGrid.gridID + " to" + grid.gridID);
                        }
                    }
                }
            }
        }
    }

    public static Map<Vector3f, SuperChunk> getSuperChunks() {
        return superChunks;
    }

}
