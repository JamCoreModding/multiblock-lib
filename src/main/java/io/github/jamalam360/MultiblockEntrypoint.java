package io.github.jamalam360;

import io.github.jamalam360.pattern.MultiblockResourceReloadListener;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

/**
 * @author Jamalam360
 */
public class MultiblockEntrypoint implements ModInitializer {
    @Override
    public void onInitialize() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new MultiblockResourceReloadListener());
    }
}
