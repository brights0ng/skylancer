package net.brights0ng.skylancer;

import org.joml.Vector3f;

public class LocalBlockPos {
    private float x;
    private float y;
    private float z;

    public LocalBlockPos(float x, float y, float z) {
        System.out.println("Constructor called with: " + x + " " + y + " " + z);
        this.x = x;
        this.y = y;
        this.z = z;
        System.out.println("Instance variables set to: " + (int) this.x + " " + (int) this.y + " " + (int) this.z);
    }

    public String toString() {
        return "LocalBlockPos{" + "x=" + (int) x + ", y=" + (int) y + ", z=" + (int) z + '}';
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }
}
