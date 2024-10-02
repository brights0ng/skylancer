package net.brights0ng.skylancer.registries;

import net.brights0ng.skylancer.LocalGrid;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

import static net.brights0ng.skylancer.LocalGrid.localGridMap;

public class PhysicsRegistry {
    private static long lastUpdateTime = System.nanoTime();


    public static void registerPhysicsTick () {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            for (LocalGrid grid : localGridMap.values()) {

                grid.gridPhysics.tickBlockPhysics();
            }

        });
    }

}
