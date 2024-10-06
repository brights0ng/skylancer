package net.brights0ng.skylancer.item;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static net.brights0ng.skylancer.item.GridToolItem.switchGrid;

public class SwitchToolItem extends Item {
    public SwitchToolItem(Settings settings) {
        super(settings);
    }

    private int lastX = 0;
    private int lastY = 0;
    private int lastZ = 0;

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        if (lastX != pos.getX() || lastY != pos.getY() || lastZ != pos.getZ()){
            switchGrid(context);
        }
        lastX = pos.getX();
        lastY = pos.getY();
        lastZ = pos.getZ();
        return super.useOnBlock(context);
    }
}
