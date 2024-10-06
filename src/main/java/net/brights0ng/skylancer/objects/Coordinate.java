package net.brights0ng.skylancer.objects;

import net.minecraft.util.math.BlockPos;
import org.joml.Vector3f;

public class Coordinate {
    public float x;
    public float y;
    public float z;

    public Coordinate(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Coordinate(Vector3f v){
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    public Coordinate(BlockPos v){
        this.x = v.getX();
        this.y = v.getY();
        this.z = v.getZ();
    }

    public Coordinate(){
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Coordinate scale(float s) {
        return new Coordinate(this.x * s, this.y * s, this.z * s);
    }

    public Coordinate add(Coordinate c){
        this.x += c.x;
        this.y += c.y;
        this.z += c.z;
        return this;
    }

    public Vector3f toVector3f(){
        return new Vector3f(this.x, this.y, this.z);
    }

    public Coordinate rawToSuper(){
        Coordinate coordinate = this.scale(0.0052083333f);
        if (coordinate.x < 0){
            coordinate.x = (int) Math.floor(coordinate.x);
        } else {
            coordinate.x = (int) Math.floor(coordinate.x) + 1;
        }
        if (coordinate.y < 0){
            coordinate.y = (int) Math.floor(coordinate.y);
        } else {
            coordinate.y = (int) Math.floor(coordinate.y) + 1;
        }
        if (coordinate.z < 0){
            coordinate.z = (int) Math.floor(coordinate.z);
        } else {
            coordinate.z = (int) Math.floor(coordinate.z) + 1;
        }
        return coordinate;
    }

    public static Coordinate rawToSuper(Coordinate c){
        Coordinate coordinate = c.scale(0.0052083333f);
        if (coordinate.x < 0){
            coordinate.x = (int) Math.floor(coordinate.x);
        } else {
            coordinate.x = (int) Math.floor(coordinate.x) + 1;
        }
        if (coordinate.y < 0){
            coordinate.y = (int) Math.floor(coordinate.y);
        } else {
            coordinate.y = (int) Math.floor(coordinate.y) + 1;
        }
        if (coordinate.z < 0){
            coordinate.z = (int) Math.floor(coordinate.z);
        } else {
            coordinate.z = (int) Math.floor(coordinate.z) + 1;
        }
        return coordinate;
    }

    public String toString(){
        return "[" + x + ", " + y + ", " + z + "]";
    }

    public boolean equals(Coordinate c){
        Vector3f v1 = this.toVector3f();
        Vector3f v2 = c.toVector3f();
        return v1.equals(v2);
    }

    public Vector3f toVector3f(float scale){
        return new Vector3f(this.x * scale, this.y * scale, this.z * scale);
    }
}
