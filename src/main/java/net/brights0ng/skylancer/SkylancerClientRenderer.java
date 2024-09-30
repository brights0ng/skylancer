package net.brights0ng.skylancer;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;

public class SkylancerClientRenderer {
    public SkylancerClientRenderer() {
        // Register a callback to run your custom rendering
        WorldRenderEvents.AFTER_ENTITIES.register(context -> renderCustomBlocks(context.matrixStack(), context.consumers()));
    }

    private void renderCustomBlocks(MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
        // Here, define the logic for where you want to render your block
        BlockPos blockPos = new BlockPos(0, 10, 0); // Position of your custom block

        // Implement your rendering logic, e.g., using a method like renderPhysicalBlock()
        renderPhysicalBlock(matrices, vertexConsumers, blockPos);
    }

    private void renderPhysicalBlock(MatrixStack matrices, VertexConsumerProvider vertexConsumers, BlockPos blockPos) {
        // Your custom rendering code
        matrices.push();

        // Translate to the block's position
        matrices.translate(blockPos.getX(), blockPos.getY(), blockPos.getZ());

        // Use Minecraft’s BlockRenderManager to render the block visually
        MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(
                LocalBlockRegistry.LOCAL_BLOCK.getDefaultState(),
                matrices,
                vertexConsumers,
                15728880, // Example light value, adjust as necessary
                OverlayTexture.DEFAULT_UV
        );

        matrices.pop();
    }
}