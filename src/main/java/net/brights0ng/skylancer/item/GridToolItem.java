package net.brights0ng.skylancer.item;

import com.mojang.brigadier.context.CommandContext;
import net.brights0ng.skylancer.Skylancer;
import net.brights0ng.skylancer.objects.AngularVector;
import net.brights0ng.skylancer.objects.LocalGrid;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class GridToolItem extends Item {
    public GridToolItem(Settings settings) {
        super(settings);
    }
    public static int getKey = 1;
    public static Map<Integer, LocalGrid> toolGridMap = new HashMap<>();

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        Block block = world.getBlockState(context.getBlockPos()).getBlock();
        int x = context.getBlockPos().getX();
        int y = context.getBlockPos().getY();
        int z = context.getBlockPos().getZ();
        int i = 0;
        for(LocalGrid grid : Skylancer.localGridMap.values()){
            if (Objects.equals(grid.gridPhysics.origin, new Vector3f(x+.5f, y + 1.5f, z+.5f))){
                i = 1;
                break;
            }
        }
        if(i != 1){
            LocalGrid grid = new LocalGrid(new Vector3f(x+.5f, y+1.5f, z+.5f),new Vector3f());
            grid.createObject(new BlockPos(0,0,0),block);
            grid.gridPhysics.angularVelocity = new AngularVector(0,20,0);
            toolGridMap.put(toolGridMap.size()+1, grid);
        }
        return super.useOnBlock(context);
    }

    public static void switchGrid(ItemUsageContext context){
        if(getKey >= toolGridMap.size()){
            getKey = 1;
        } else {
            getKey ++;
        }
        ServerCommandSource source = context.getPlayer().getCommandSource();
        source.sendFeedback(() -> Text.literal("Key: " + getKey), false);
    }

    public static LocalGrid getGrid(){
        return toolGridMap.get(getKey);
    }



}
