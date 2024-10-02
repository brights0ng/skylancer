package net.brights0ng.skylancer;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

public class PhysicsTest {

    public static class PhysicsObject{

        BlockPos blockPos;
        LocalGrid localGrid;
        Block block;

        public PhysicsObject(String name, LocalGrid localGrid, BlockPos blockPos, Block block){

            this.localGrid = localGrid;
            this.blockPos = blockPos;
            this.block = block;
        }

    }
}
