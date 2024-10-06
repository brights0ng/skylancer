package net.brights0ng.skylancer;

import net.brights0ng.skylancer.collision.CollisionPhysics;
import net.brights0ng.skylancer.collision.SuperChunkManager;
import net.brights0ng.skylancer.item.ModItems;
import net.brights0ng.skylancer.objects.LocalGrid;
import net.brights0ng.skylancer.objects.SkylancerClientRenderer;
import net.brights0ng.skylancer.registries.CommandRegistry;
import net.brights0ng.skylancer.registries.LocalBlockRegistry;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Skylancer implements ModInitializer {
	public static final String MOD_ID = "skylancer";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static Map<UUID, LocalGrid> localGridMap = new HashMap<>();
	public SuperChunkManager superChunkManager;
	private final ScheduledExecutorService secondScheduler = Executors.newScheduledThreadPool(1);


	public void tickRegistry() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			new CollisionPhysics();

			for (LocalGrid grid : localGridMap.values()) {
				grid.gridPhysics.tickBlockPhysics();
			}

		});
	}

	public void frameRegistry() {
		WorldRenderEvents.AFTER_ENTITIES.register(context -> {
			new SkylancerClientRenderer(context);
		});
	}

	public void secondRegistry() {
		secondScheduler.scheduleAtFixedRate(() -> {
			try {
				// Code to execute every second
				SuperChunkManager.update();
			} catch (Exception e) {
				LOGGER.error("Error in scheduled update task", e);
			}
		}, 0, 1, TimeUnit.SECONDS);
	}


	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		CommandRegistry.registerCommands();
		LocalBlockRegistry.register();
		ModItems.registerItems();
		tickRegistry();
		frameRegistry();
        secondRegistry();

        LOGGER.info("Skylancer is loading...");

	}
}