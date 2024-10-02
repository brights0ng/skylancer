package net.brights0ng.skylancer;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockRenderView;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Objects;

import static net.brights0ng.skylancer.LocalGrid.localGridMap;

public class SkylancerClientRenderer {
    private static long lastUpdateTime = System.nanoTime();


    public SkylancerClientRenderer() {
        // Register a callback to run your custom rendering
        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            long currentTime = System.nanoTime();

            // Calculate delta time in seconds
            float deltaTime = (currentTime - lastUpdateTime) * 20 / 1_000_000_000.0f; // Convert nanoseconds to seconds
            // Update the last update time
            lastUpdateTime = currentTime;

            for (LocalGrid grid : localGridMap.values()) {
                grid.gridPhysics.frameBlockPhysics(deltaTime);
            }

            renderCustomBlocks(context, deltaTime);
        });
    }

    private void renderCustomBlocks(WorldRenderContext context, float deltaTime) {
        for (LocalGrid localGrid : LocalGrid.localGridMap.values()){
            for (PhysicsTest.PhysicsObject object : localGrid.gridObjectsMap.values()){
                renderPhysicalBlock(context.matrixStack(), Objects.requireNonNull(context.consumers()), object, deltaTime);

            }
        }

    }

    private void renderPhysicalBlock(MatrixStack matrices, VertexConsumerProvider vertexConsumers, PhysicsTest.PhysicsObject object, float deltaTime) {
        MinecraftClient client = MinecraftClient.getInstance(); // Obtain the client instance
        ClientPlayerEntity player = client.player; // Obtain the player from the client
        BlockRenderView world = client.world; // Obtain the world from the client
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getCutout()); // Get the vertex consumer

        if (player != null) {
            // Get the camera position (the player's viewpoint)
            Vec3d cameraPos = client.gameRenderer.getCamera().getPos();

            // Push the matrix stack to save the current state
            matrices.push();

            // Translate the renderedOrigin of the rendering from the player to the world renderedOrigin
            matrices.translate(-cameraPos.getX(), -cameraPos.getY(), -cameraPos.getZ());

            // Translate the block's position from localGrid coordinates to worldGrid coordinates
            matrices.translate(object.localGrid.getRenderedOrigin().x,
                    object.localGrid.getRenderedOrigin().y,
                    object.localGrid.getRenderedOrigin().z);

            // Rotate the block
            Quaternionf rotation = object.localGrid.getRotation();
            matrices.multiply(rotation); // Apply the rotationXYZ

            // Translate the matrix to the block's position on the localGrid
            matrices.translate(object.blockPos.getX(),
                    object.blockPos.getY(),
                    object.blockPos.getZ());
//            matrices.translate(-.5,-1,-.5);



            // Render the block using the translated matrix
            client.getBlockRenderManager().renderBlock(
                    object.block.getDefaultState(),
                    BlockPos.ORIGIN, // Use the renderedOrigin since we've already translated
                    world,
                    matrices, // Pass the transformed matrix stack
                    vertexConsumer,
                    true,
                    player.getRandom()
            );

            matrices.pop(); // Restore the previous matrix state
        }
    }
}
