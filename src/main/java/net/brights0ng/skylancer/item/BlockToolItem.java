package net.brights0ng.skylancer.item;

import net.brights0ng.skylancer.Skylancer;
import net.brights0ng.skylancer.objects.LocalGrid;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.Registries;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.joml.Vector3f;

import static net.brights0ng.skylancer.item.GridToolItem.getGrid;
import static net.brights0ng.skylancer.item.GridToolItem.getKey;

public class BlockToolItem extends Item {
    public BlockToolItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        Block block = context.getWorld().getBlockState(context.getBlockPos()).getBlock();
        int x = context.getBlockPos().getX();
        int y = context.getBlockPos().getY()+1;
        int z = context.getBlockPos().getZ();

        LocalGrid grid = getGrid();

        grid.createObject(getBlockPos(x, y, z, grid.getOrigin().toVector3f()), Registries.BLOCK.get(Identifier.of("iron_block")));

        return super.useOnBlock(context);
    }

    public static BlockPos getBlockPos(int x, int y, int z, Vector3f origin) {
        return new BlockPos(Math.round(x - origin.x), Math.round(y - origin.y), Math.round(z - origin.z));


    }

}
