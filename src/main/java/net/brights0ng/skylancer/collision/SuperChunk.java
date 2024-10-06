package net.brights0ng.skylancer.collision;

import net.brights0ng.skylancer.Skylancer;
import net.brights0ng.skylancer.objects.Coordinate;
import net.brights0ng.skylancer.objects.LocalGrid;

import java.util.ArrayList;
import java.util.List;


public class SuperChunk {
    private final AABB boundingBox; // The bounding box for collision detection
    private List<LocalGrid> grids; // Local grids within this superChunk
    private final Coordinate coordinate;

    public SuperChunk(Coordinate coordinate) {
        this.coordinate = coordinate;
        this.boundingBox = new AABB(
                (coordinate.x-(Math.abs(coordinate.x)/coordinate.x))*192,
                (coordinate.y-(Math.abs(coordinate.y)/coordinate.y))*192,
                (coordinate.z-(Math.abs(coordinate.z)/coordinate.z))*192,
                (coordinate.x)*192,
                (coordinate.y)*192,
                (coordinate.z)*192
        );
        this.grids = new ArrayList<>();
        System.out.println("SuperChunk created at " + coordinate.toString() + " with bounding box: " + boundingBox.toString());
    }

    public void addGrid(LocalGrid grid) {
        grids.add(grid);
    }

    public List<LocalGrid> getGrids() {
        return grids;
    }

    public List<LocalGrid> getOtherGrids(LocalGrid grid){

        List<LocalGrid> grids2 = new ArrayList<>(this.grids);
        grids2.remove(grid);

        return grids2;

    }

    public void clearGrids() {
        grids.clear();
    }

    public Coordinate getCoordinate(){
        return coordinate;
    }

    public boolean testForGrids() {
        for (LocalGrid grid : Skylancer.localGridMap.values()) {
            if (grid.gridPhysics.gridBoundingBox.intersects(boundingBox) && !grids.contains(grid) && !grid.gridObjectsMap.isEmpty()) {
                grids.add(grid);
                System.out.println("Grid found in superChunk for coordinate: " + coordinate);
            }
//            if (grid.coordinate.equals(coordinate) && !grids.contains(grid)) {
//                grids.add(grid);
////                System.out.println("Grid found in superChunk for coordinate: " + coordinate);
//            }
        }

        if (grids.isEmpty()) {
            System.out.println("No grids found in superChunk for coordinate: " + coordinate);
            return false;
        } else {
            return true;
        }
    }



    public boolean intersects(AABB otherBoundingBox) {
        return boundingBox.intersects(otherBoundingBox);
    }

    public AABB getBoundingBox() {
        return boundingBox;
    }
}