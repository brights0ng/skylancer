package net.brights0ng.skylancer;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class CreateGridTest {
    static Quaternionf combinedRotation = new Quaternionf().rotateXYZ(
            (float) Math.toRadians(30),  // Rotation around X (pitch)
            (float) Math.toRadians(45),  // Rotation around Y (yaw)
            (float) Math.toRadians(60)   // Rotation around Z (roll)
    );
    static LocalGrid localGrid = new LocalGrid(new Vec3d(10.0, 64.0, 20.0),
            combinedRotation);

    public static LocalBlockPos testGridTranslation(BlockPos blockPos) {
        System.out.println(blockPos.toString());


        Vector3f vector3f = localGrid.toWorldCoordinates(blockPos);
        LocalBlockPos localBlockPos = new LocalBlockPos(vector3f.x, vector3f.y, vector3f.z);
        System.out.println(localBlockPos.toString());
        return localBlockPos;

    }
}
