package net.brights0ng.skylancer.objects;

import net.brights0ng.skylancer.collision.OBB;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;


public class PhysicsObject{

    private final BlockPos blockPos;
    private final LocalGrid localGrid;
    private final Block block;
    private final UUID blockID;
    private OBB boundingBox;

    public PhysicsObject(LocalGrid localGrid, BlockPos blockPos, Block block){
        this.blockID = UUID.randomUUID();
        localGrid.gridObjectsMap.put(blockID, this);

        this.localGrid = localGrid;
        this.blockPos = blockPos;
        this.block = block;
        this.boundingBox = new OBB(localGrid, this);
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public LocalGrid getLocalGrid() {
        return localGrid;
    }

    public Block getBlock() {
        return block;
    }

    public UUID getID() {
        return blockID;
    }

    public void updateBoundingBox(){
        boundingBox = new OBB(localGrid, this);
    }

    public OBB getBoundingBox() {
        return boundingBox;
    }

    public void clearBoundingBox(){
        boundingBox = null;
    }

}

