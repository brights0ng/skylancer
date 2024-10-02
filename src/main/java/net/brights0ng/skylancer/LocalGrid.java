package net.brights0ng.skylancer;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class LocalGrid {
    public static Map<String, LocalGrid> localGridMap = new HashMap<>();

    public Map<String, PhysicsTest.PhysicsObject> gridObjectsMap = new HashMap<>();
    public GridPhysics gridPhysics;

    public LocalGrid(Vector3f origin, Vector3f rotation, String name) {
        System.out.println("Creating grid " + name);
        this.gridPhysics = new GridPhysics(origin);
        this.gridPhysics.rotationXYZ = rotation;
        localGridMap.put(name, this);
        System.out.println("Created grid!");

    }

    public Vector3f getOrigin() {
        return gridPhysics.renderedOrigin;
    }

    public Quaternionf getRotation() {
        return gridPhysics.renderedRotation;
    }

    public void createObject(String name, BlockPos blockPos, Block block) {
        PhysicsTest.PhysicsObject object = new PhysicsTest.PhysicsObject(name, this,  blockPos, block);
        gridObjectsMap.put(name, object);
    }

    public PhysicsTest.PhysicsObject getObject(String name) {
        return gridObjectsMap.get(name);
    }

    // Convert coordinates from this grid's space to world space
    public Vector3f toWorldCoordinates(PhysicsTest.PhysicsObject object) {
        // Convert the Vec3d to JOML Vector3f
        Vector3f localOrigin = new Vector3f(
                (float) object.blockPos.getX(),
                (float) object.blockPos.getY(),
                (float) object.blockPos.getZ());

        localOrigin.add(object.localGrid.getOrigin());

        float yaw = (float) Math.toRadians(object.localGrid.getRotation().y);
        float pitch = (float) Math.toRadians(object.localGrid.getRotation().x);
        float roll = (float) Math.toRadians(object.localGrid.getRotation().z);
        //
        Quaternionf rotation = new Quaternionf();
        rotation.rotateYXZ((float) Math.toRadians(yaw),
                (float) Math.toRadians(pitch),
                (float) Math.toRadians(roll));

        // Apply the quaternion rotationXYZ to the vector
        localOrigin.rotate(rotation);

        return localOrigin ;
    }

//    public Vector3f getV() {
//        return gridPhysics.linearVelocity;
//    }
//
//    public void setV(Vector3f linearVelocity) {
//        this.gridPhysics.linearVelocity = linearVelocity;
//    }
//
//    public void addV(Vector3f linearVelocity) {
//        this.gridPhysics.linearVelocity.add(linearVelocity);
//    }
//
//    public Vector3f getA() {
//        return gridPhysics.linearAcceleration;
//    }
//
//    public void setA(Vector3f linearAcceleration) {
//        this.gridPhysics.linearAcceleration = linearAcceleration;
//    }
//
//    public void updateOrigin(Vector3f renderedOrigin) {
//        this.gridPhysics.renderedOrigin = renderedOrigin;
//    }
//
//    public void updateRotation(Vector3f rotationXYZ) {
//        this.gridPhysics.rotationXYZ = rotationXYZ;
//    }

    public Vector3f getRenderedOrigin() {
        return gridPhysics.renderedOrigin;
    }





}