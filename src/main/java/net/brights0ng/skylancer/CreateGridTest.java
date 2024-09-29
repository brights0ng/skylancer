package net.brights0ng.skylancer;

import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class CreateGridTest {
    ServerWorld world;
    Quaternionf combinedRotation = new Quaternionf().rotateXYZ(
            (float) Math.toRadians(30),  // Rotation around X (pitch)
            (float) Math.toRadians(45),  // Rotation around Y (yaw)
            (float) Math.toRadians(60)   // Rotation around Z (roll)
    );
    SpaceGrid spaceGrid = new SpaceGrid(new Vec3d(10.0, 64.0, 20.0),
            combinedRotation);

    BlockPos blockPos = new BlockPos(1,10,0);
    Vector3f vector3f = spaceGrid.toWorldCoordinates(blockPos);
    LocalBlockPos localBlockPos = new LocalBlockPos(vector3f.x(), vector3f.y(), vector3f.z());

}
