package net.brights0ng.skylancer.registries;

import net.brights0ng.skylancer.CreateGridTest;
import net.brights0ng.skylancer.LocalBlockPos;
import net.brights0ng.skylancer.LocalGrid;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.function.Supplier;

public class CommandRegistry {

    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, environment) -> {
             dispatcher.register(CommandManager.literal("skylancer").executes(context -> {
                 ServerCommandSource source = context.getSource();
                 BlockPos blockPos = source.getPlayer().getBlockPos();
                 LocalBlockPos localBlockPos = CreateGridTest.testGridTranslation(blockPos);
                 source.sendFeedback(() -> Text.literal("Your position on the local grid is: " + blockPos.toString()), false);
                 source.sendFeedback(() -> Text.literal("Your position on the world grid is: " + localBlockPos.toString()), false);
                 return 1;
             }));
        });
    }
}
