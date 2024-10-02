package net.brights0ng.skylancer.registries;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.brights0ng.skylancer.AngularVector;
import net.brights0ng.skylancer.LocalGrid;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.joml.Vector3f;

public class CommandRegistry {

    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, environment) -> {
             dispatcher.register(CommandManager.literal("skylancer")
                     .then(CommandManager.literal("createLocalGrid")
                         .then(CommandManager.argument("name", StringArgumentType.string())
                                 .then(CommandManager.argument("originx", FloatArgumentType.floatArg())
                                         .then(CommandManager.argument("originy", FloatArgumentType.floatArg())
                                                 .then(CommandManager.argument("originz", FloatArgumentType.floatArg())
                                                         .then(CommandManager.argument("rotationx", FloatArgumentType.floatArg())
                                                                 .then(CommandManager.argument("rotationy", FloatArgumentType.floatArg())
                                                                         .then(CommandManager.argument("rotationz", FloatArgumentType.floatArg())
                                                                                     .executes(context -> {
                                                                                         ServerCommandSource source = context.getSource();
                                                                                         String name = StringArgumentType.getString(context, "name");
                                                                                         source.sendFeedback(() -> Text.literal("Creating local grid..."), false);

                                                                                         Vector3f origin = new Vector3f(FloatArgumentType.getFloat(context, "originx"),
                                                                                                 FloatArgumentType.getFloat(context, "originy"),
                                                                                                 FloatArgumentType.getFloat(context, "originz"));

                                                                                         Vector3f rotation = new Vector3f(FloatArgumentType.getFloat(context, "rotationx"),
                                                                                                 FloatArgumentType.getFloat(context, "rotationy"),
                                                                                                 FloatArgumentType.getFloat(context, "rotationz"));

                                                                                         new LocalGrid(origin, rotation, name);
                                                                                         source.sendFeedback(() -> Text.literal("Local Grid " + name + " created"), false);
                                                                                         return 1;
                                                                                     })
                                                                         )
                                                                 )
                                                         )
                                                 )
                                         )
                                 )
                         )
                     )
                     .then(CommandManager.literal("createLocalBlock")
                             .then(CommandManager.argument("localGrid", StringArgumentType.string())
                                     .then(CommandManager.argument("name", StringArgumentType.string())
                                             .then(CommandManager.argument("blockType", StringArgumentType.string())
                                                     .then(CommandManager.argument("blockPosX", IntegerArgumentType.integer())
                                                             .then(CommandManager.argument("blockPosY", IntegerArgumentType.integer())
                                                                     .then(CommandManager.argument("blockPosZ", IntegerArgumentType.integer())
                                                                             .executes(context -> {
                                                                                 ServerCommandSource source = context.getSource();
                                                                                 source.sendFeedback(() -> Text.literal("Local Block being constructed..."), false);
                                                                                 LocalGrid local = LocalGrid.localGridMap.get(StringArgumentType.getString(context, "localGrid"));
                                                                                 Block block = Registries.BLOCK.get(Identifier.of("minecraft:" + StringArgumentType.getString(context, "blockType")));

                                                                                 local.createObject(StringArgumentType.getString(context,"name"),
                                                                                         new BlockPos(IntegerArgumentType.getInteger(context, "blockPosX"),
                                                                                         IntegerArgumentType.getInteger(context, "blockPosY"),
                                                                                         IntegerArgumentType.getInteger(context, "blockPosZ")), block);
                                                                                 source.sendFeedback(() -> Text.literal("Local Block made of" + StringArgumentType.getString(context, "blockType") + " created"), false);

                                                                                 return 1;
                                                                             })
                                                                     )
                                                             )
                                                     )
                                             )
                                     )
                             )
                     )
                     .then(CommandManager.literal("setRotation")
                             .then(CommandManager.argument("rotational", BoolArgumentType.bool())
                                     .then(CommandManager.argument("localGrid", StringArgumentType.string())
                                             .then(CommandManager.argument("x/pitch", FloatArgumentType.floatArg())
                                                     .then(CommandManager.argument("y/yaw", FloatArgumentType.floatArg())
                                                             .then(CommandManager.argument("z/roll", FloatArgumentType.floatArg())
                                                                     .executes(context ->{
                                                                         LocalGrid local =  LocalGrid.localGridMap.get(StringArgumentType.getString(context, "localGrid"));
                                                                         if(BoolArgumentType.getBool(context, "rotational")){
                                                                             local.gridPhysics.setAngularAcceleration(new AngularVector(FloatArgumentType.getFloat(context, "y/yaw"),
                                                                                     FloatArgumentType.getFloat(context, "x/pitch"),
                                                                                     FloatArgumentType.getFloat(context, "z/roll")));
                                                                         } else {
                                                                             local.gridPhysics.setLinearAcceleration(new Vector3f(FloatArgumentType.getFloat(context, "x/pitch"),
                                                                                     FloatArgumentType.getFloat(context, "y/yaw"),
                                                                                     FloatArgumentType.getFloat(context, "z/roll")));
                                                                         }
                                                                         return 1;
                                                                     })
                                                             )
                                                     )
                                             )
                                     )
                             )
                     )
             );
             });
        };
    }

