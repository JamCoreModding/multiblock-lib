package io.github.jamalam360.components;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;
import net.minecraft.util.Identifier;

/**
 * @author Jamalam360
 */
public class CardinalComponentsInit implements WorldComponentInitializer {
    public static final ComponentKey<MultiblockProviderImpl> PROVIDER = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier("multiblocklib", "multiblock_provider"), MultiblockProviderImpl.class);

    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
        registry.register(PROVIDER, MultiblockProviderImpl::new);
    }
}
