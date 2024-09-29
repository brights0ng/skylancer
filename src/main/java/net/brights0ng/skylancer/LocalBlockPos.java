package net.brights0ng.skylancer;

import org.joml.Vector3f;

public class LocalBlockPos extends Vector3f {

    public final float x;
    public final float y;
    public final float z;

    public LocalBlockPos(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // Additional methods as needed, e.g., converting to BlockPos or other calculations
}
