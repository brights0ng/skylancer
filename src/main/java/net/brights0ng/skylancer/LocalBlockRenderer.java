package net.brights0ng.skylancer;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class LocalBlockRenderer {
    public void renderBlock(MatrixStack matrices, BlockPos blockPos, LocalGrid localGrid,
                            BlockState state, VertexConsumerProvider vertexConsumers, int light,
                            int overlay) {
        // Calculate world coordinates
        Vector3f l = localGrid.toWorldCoordinates(blockPos);
        LocalBlockPos localBlockPos = new LocalBlockPos(l.x,l.y,l.z);

        // Push the current matrix to the stack
        matrices.push();

        // Translate to world coordinates
        matrices.translate(localBlockPos.getX(), localBlockPos.getY(), localBlockPos.getZ());

        // Optional: Apply rotation (if necessary)
        // Create and apply rotation
        Quaternionf rotation = new Quaternionf();
        rotation.rotateYXZ(rotation.y, rotation.x, rotation.z);
        matrices.multiply(rotation); // Apply rotation        // Render the block using Minecraft's rendering methods
        MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(state, matrices, vertexConsumers, light, overlay);

        // Pop the matrix to restore the previous state
        matrices.pop();
    }
}