package net.brights0ng.skylancer;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public class GridPhysics {
    public Vector3f renderedOrigin;
    public Vector3f predictedPos;
    public Vector3f origin;
    public Vector3f linearVelocity;
    public Vector3f linearAcceleration;
    public Vector3f rotationXYZ;
    public Quaternionf rotation;
    public Quaternionf renderedRotation;
    public AngularVector angularVelocity;
    public AngularVector angularAcceleration;

    public GridPhysics(Vector3f currentPos) {
        this.renderedOrigin = currentPos;
        this.predictedPos = currentPos;
        this.origin = currentPos;
        this.linearVelocity = new Vector3f(0, 0, 0);
        this.linearAcceleration = new Vector3f(0, 0, 0);
        this.rotation = new Quaternionf();
        this.renderedRotation = new Quaternionf();
        this.angularVelocity = new AngularVector(0, 0, 0);
        this.angularAcceleration = new AngularVector(0, 0, 0);
    }

    public void setLinearAcceleration(Vector3f linearAcceleration) {
        this.linearAcceleration = linearAcceleration;
    }

    public void setAngularAcceleration(AngularVector angularAcceleration) {
        this.angularAcceleration = angularAcceleration;
    }

    public void tickBlockPhysics() {
        this.linearVelocity = new Vector3f(linearVelocity.add(linearAcceleration));
        this.origin = new Vector3f(origin.add(linearVelocity));
        this.renderedOrigin = origin;

        this.angularVelocity.pitch += angularAcceleration.pitch;
        this.angularVelocity.yaw += angularAcceleration.yaw;
        this.angularVelocity.roll += angularAcceleration.roll;

        this.rotation.rotateYXZ((float) Math.toRadians(angularVelocity.yaw),
                (float) Math.toRadians(angularVelocity.pitch),
                (float) Math.toRadians(angularVelocity.roll));
        this.renderedRotation = rotation;
    }

    public void frameBlockPhysics(float deltaTime) {
        this.renderedOrigin.x = renderedOrigin.x + linearVelocity.x * deltaTime;
        this.renderedOrigin.y = renderedOrigin.y + linearVelocity.y * deltaTime;
        this.renderedOrigin.z = renderedOrigin.z + linearVelocity.z * deltaTime;

        this.renderedRotation = new Quaternionf(renderedRotation.rotateYXZ(
                (float) Math.toRadians(angularVelocity.yaw * deltaTime),
                (float) Math.toRadians(angularVelocity.pitch * deltaTime),
                (float) Math.toRadians(angularVelocity.roll * deltaTime)
        ));
        System.out.println(this.renderedOrigin);
        System.out.println(this.renderedRotation);

    }


}
