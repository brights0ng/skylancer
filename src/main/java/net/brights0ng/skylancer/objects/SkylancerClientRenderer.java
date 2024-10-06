package net.brights0ng.skylancer.objects;

import net.brights0ng.skylancer.Skylancer;
import net.brights0ng.skylancer.collision.AABB;
import net.brights0ng.skylancer.collision.OBB;
import net.brights0ng.skylancer.collision.SuperChunk;
import net.brights0ng.skylancer.collision.SuperChunkManager;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
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

import static net.brights0ng.skylancer.Skylancer.localGridMap;

public class SkylancerClientRenderer {
    public static long lastUpdateTime = System.nanoTime();

    public SkylancerClientRenderer(WorldRenderContext context) {
        long currentTime = System.nanoTime();
        float deltaTime = (currentTime - lastUpdateTime) * 20 / 1_000_000_000.0f; // Convert nanoseconds to seconds
        lastUpdateTime = currentTime;

        for (LocalGrid grid : localGridMap.values()) {
            grid.gridPhysics.frameBlockPhysics(deltaTime);
        }

        renderCustomBlocks(context, deltaTime);
        renderBoundingBoxes(context.matrixStack());
    }

    private void renderCustomBlocks(WorldRenderContext context, float deltaTime) {
        for (LocalGrid localGrid : Skylancer.localGridMap.values()) {
            for (PhysicsObject object : localGrid.gridObjectsMap.values()) {
                renderPhysicalBlock(context.matrixStack(), Objects.requireNonNull(context.consumers()), object, deltaTime);
            }
        }
    }

    private void renderPhysicalBlock(MatrixStack matrices, VertexConsumerProvider vertexConsumers, PhysicsObject object, float deltaTime) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        BlockRenderView world = client.world;
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getCutout());

        if (player != null) {
            Vec3d cameraPos = client.gameRenderer.getCamera().getPos();

            matrices.push();
            matrices.translate(-cameraPos.getX(), -cameraPos.getY(), -cameraPos.getZ());
            matrices.translate(object.getLocalGrid().getOrigin().x,
                    object.getLocalGrid().getOrigin().y,
                    object.getLocalGrid().getOrigin().z);

            // Rotate the block
            float yaw = (float) Math.toRadians(object.getLocalGrid().gridPhysics.renderedRotation.y);
            float pitch = (float) Math.toRadians(object.getLocalGrid().gridPhysics.renderedRotation.x);
            float roll = (float) Math.toRadians(object.getLocalGrid().gridPhysics.renderedRotation.z);
            Quaternionf rotation = new Quaternionf();
            rotation.rotateYXZ(yaw, pitch, roll);
            matrices.multiply(rotation);

            matrices.translate(object.getBlockPos().getX()-.5,
                    object.getBlockPos().getY()-.5,
                    object.getBlockPos().getZ()-.5);

            client.getBlockRenderManager().renderBlock(
                    object.getBlock().getDefaultState(),
                    BlockPos.ORIGIN,
                    world,
                    matrices,
                    vertexConsumer,
                    true,
                    player.getRandom()
            );
            matrices.pop();
        }
    }

    private static void renderBoundingBoxes(MatrixStack matrices) {
        for (SuperChunk superChunk : SuperChunkManager.getSuperChunks().values()) {
            drawBoundingBox(matrices, superChunk.getBoundingBox(), new float[]{1.0f, 0.0f, 0.0f}); // Red for superChunk
        }

        for (LocalGrid grid : Skylancer.localGridMap.values()) {
            if (grid.gridPhysics.gridBoundingBox != null) {
                drawBoundingBox(matrices, grid.gridPhysics.gridBoundingBox, new float[]{0.0f, 1.0f, 0.0f}); // Green for grid
            }
        }

        for (LocalGrid grid : Skylancer.localGridMap.values()) {
            for (PhysicsObject object : grid.gridObjectsMap.values()) {
                if (object.getBoundingBox() != null) {
                    drawBoundingBoxOBB(matrices, object.getBoundingBox(), new float[]{0.0f, 0.2f, 1.0f}); // Blue for block
                }
            }
        }
    }

    private static void drawBoundingBox(MatrixStack matrices, AABB boundingBox, float[] color) {
        Vec3d min = new Vec3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        Vec3d max = new Vec3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        drawBoxFromCorners(matrices, min, max, color);
    }

    private static void drawBoundingBoxOBB(MatrixStack matrices, OBB boundingBox, float[] color) {
        Vector3f[] vertices2 = boundingBox.getVertices();
        Vec3d[] vertices = new Vec3d[vertices2.length];
        for (int i = 0; i < vertices2.length; i++) {
            vertices[i] = new Vec3d(vertices2[i].x, vertices2[i].y, vertices2[i].z);
        }
        Vec3d c = MinecraftClient.getInstance().gameRenderer.getCamera().getPos();

        Vec3d[] corners = {
                new Vec3d(0,2,5),
                new Vec3d(0,2,6),
                new Vec3d(0,3,6),
                new Vec3d(0,3,5),
                new Vec3d(1,2,5),
                new Vec3d(1,2,6),
                new Vec3d(1,3,6),
                new Vec3d(1,3,5)
        };

        // Define the edges of the OBB for proper visualization
        int[][] edges = {
                {0,1} ,               {1,2} ,              {2,3} ,               {3,0},
                {4,5} , {5,1}, {1,5}, {5,6}, {6,2}, {2,6}, {6,7}, {7,3}, {3,7},  {7,4}
        };


        VertexConsumer vertexConsumer = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().getBuffer(RenderLayer.getLineStrip());

        for (int[] edge : edges) {
            addLine(vertexConsumer, matrices, vertices[edge[0]].subtract(c).toVector3f(), vertices[edge[1]].subtract(c).toVector3f(), color);
        }
    }

    private static void drawBoxFromCorners(MatrixStack matrices, Vec3d min, Vec3d max, float[] color) {
        Vec3d c = MinecraftClient.getInstance().gameRenderer.getCamera().getPos();
        Vec3d[] corners = {
                new Vec3d(min.x - c.x, min.y - c.y, min.z - c.z),
                new Vec3d(min.x - c.x, min.y - c.y, max.z - c.z),
                new Vec3d(min.x - c.x, max.y - c.y, min.z - c.z),
                new Vec3d(min.x - c.x, max.y - c.y, max.z - c.z),
                new Vec3d(max.x - c.x, min.y - c.y, min.z - c.z),
                new Vec3d(max.x - c.x, min.y - c.y, max.z - c.z),
                new Vec3d(max.x - c.x, max.y - c.y, min.z - c.z),
                new Vec3d(max.x - c.x, max.y - c.y, max.z - c.z)
        };

        int[][] edges = {
                {0, 1}, {1, 3}, {3, 2}, {2, 0}, {0, 4}, {4, 6}, {6, 2},
                {2, 6}, {6, 7}, {7, 3}, {3, 7}, {7, 5}, {5, 1}, {1, 5},
                {5, 4}
        };

        VertexConsumer vertexConsumer = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().getBuffer(RenderLayer.getLineStrip());

        for (int[] edge : edges) {
            addLine(vertexConsumer, matrices, corners[edge[0]].toVector3f(), corners[edge[1]].toVector3f(), color);
        }
    }

    private static void addLine(VertexConsumer vertexConsumer, MatrixStack matrices, Vector3f start, Vector3f end, float[] color) {
        float r = color[0];
        float g = color[1];
        float b = color[2];
        float a = 1.0f; // Full opacity

        // Set up the start vertex
        vertexConsumer.vertex(matrices.peek().getPositionMatrix(), start.x, start.y, start.z)
                .color(r, g, b, a)
                .normal(0, 1, 0); // Adjust normal as needed

        // Set up the end vertex
        vertexConsumer.vertex(matrices.peek().getPositionMatrix(), end.x, end.y, end.z)
                .color(r, g, b, a)
                .normal(0, 1, 0); // Adjust normal as needed
    }
}
